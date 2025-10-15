package com.moodleap.client.ui.main;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.moodleap.client.DatabaseManager;
import com.moodleap.client.MainActivity;
import com.moodleap.client.R;
import com.moodleap.client.UserManager;
import com.moodleap.client.db.Converters;
import com.moodleap.client.db.entity.Mood;
import com.moodleap.client.db.entity.MoodTag;
import com.moodleap.client.db.entity.Tag;
import com.moodleap.client.db.repository.TagRepository;
import com.moodleap.client.requests.SyncWorker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MoodEntryFragment extends Fragment {

    private TextView selectedMood;
    private List<Tag> selectedTags = new ArrayList<>();
    private LinearLayout tagContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mood_entry, container, false);

        setupMoodOptions(view);
        setupTags(view);

        Button btnCreate = view.findViewById(R.id.btnCreateMood);
        btnCreate.setOnClickListener(v -> createMood());

        return view;

    }

    private void setupMoodOptions(View view) {
        int[] ids = {R.id.tvAwful, R.id.tvBad, R.id.tvNormal, R.id.tvGood, R.id.tvGreat};

        for (int id : ids) {
            TextView tv = view.findViewById(id);
            tv.setTextSize(15);
            tv.setOnClickListener(v -> {
                if (selectedMood != null) selectedMood.setPaintFlags(0);
                selectedMood = (TextView) v;
                selectedMood.setPaintFlags(selectedMood.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            });
        }
    }

    private void setupTags(View view) {
        tagContainer = view.findViewById(R.id.tagContainer);
        DatabaseManager.getTagRepository().getTags().observe(getViewLifecycleOwner(), tags -> {
            Log.d("TAG_OBSERVATION", "started");
            tagContainer.removeAllViews();
            for (Tag tag : tags) {
                TextView tv = new TextView(getContext());
                tv.setText(tag.title);
                Log.d("TAG_OBSERVATION", "setted " + tag.title);
                tv.setPadding(12, 8, 12, 8);
                tv.setBackgroundResource(android.R.drawable.btn_default_small);
                tv.setTextColor(Color.BLACK);
                tv.setOnClickListener(v -> {
                    if (selectedTags.contains(tag)) {
                        selectedTags.remove(tag);
                        tv.setPaintFlags(0);
                    } else {
                        selectedTags.add(tag);
                        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    }
                });
                tagContainer.addView(tv);
            }
            Log.d("TAG_OBSERVATION", "ended");
        });
    }

    private void createMood() {
        if (selectedMood == null) {
            Toast.makeText(getContext(), "Choose mood", Toast.LENGTH_SHORT).show();
            return;
        }

        String moodValue = selectedMood.getText().toString();
        Long emotion = MoodConverter.stringToLong(moodValue);
        Long timestamp = Converters.fromLocalDateTime(LocalDateTime.now());
        Mood mood = new Mood();
        mood.emotion = emotion;
        mood.timestamp = timestamp;
        mood.userId = UserManager.getUid(requireContext());
        DatabaseManager.getMoodRepository().insert(mood, id -> {
            //Log.d("SYNC_DEBUG", id.toString());
            for (Tag tag : selectedTags) {
                MoodTag moodTag = new MoodTag();
                moodTag.moodId = id;
                moodTag.tagId = tag.id;
                Log.d("SYNC_DEBUG", moodTag.toString());
                DatabaseManager.getMoodTagRepository().insert(moodTag);
            }
            Toast.makeText(getContext(),
                    "Mood created: " + moodValue + "\ntags: " + selectedTags,
                    Toast.LENGTH_SHORT).show();
            selectedTags.clear();
            WorkRequest syncRequest = new OneTimeWorkRequest.Builder(SyncWorker.class).build();
            WorkManager.getInstance(requireContext()).enqueue(syncRequest);

        });
        /* //Log.d("SYNC_DEBUG", id.toString());
        for (Tag tag : selectedTags) {
            MoodTag moodTag = new MoodTag();
            moodTag.moodId = id;
            moodTag.tagId = tag.id;
            Log.d("SYNC_DEBUG", moodTag.toString());
            MainActivity.getMoodTagRepository().insert(moodTag);
        }

        Toast.makeText(getContext(),
                "Mood created: " + moodValue + "\ntags: " + selectedTags,
                Toast.LENGTH_SHORT).show();

        WorkRequest syncRequest = new OneTimeWorkRequest.Builder(SyncWorker.class).build();
        WorkManager.getInstance(requireContext()).enqueue(syncRequest);

         */
    }

}
