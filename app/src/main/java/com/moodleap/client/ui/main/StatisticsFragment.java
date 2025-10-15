package com.moodleap.client.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moodleap.client.MainActivity;
import com.moodleap.client.R;
import com.moodleap.client.db.entity.Mood;
import com.moodleap.client.db.entity.Tag;
import com.moodleap.client.db.entity.TagWithUsage;
import com.moodleap.client.db.repository.MoodRepository;
import com.moodleap.client.db.repository.TagRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private LinearLayout tagStatsContainer;
    private RecyclerView moodHistoryRecycler;
    private MoodAdapter moodAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        tagStatsContainer = view.findViewById(R.id.tagStatsContainer);
        moodHistoryRecycler = view.findViewById(R.id.moodHistoryRecycler);

        moodAdapter = new MoodAdapter();
        moodHistoryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        moodHistoryRecycler.setAdapter(moodAdapter);

        observeData();

        return view;
    }

    private void observeData() {
        TagRepository tagRepository = MainActivity.getTagRepository();
        MoodRepository moodRepository = MainActivity.getMoodRepository();

        tagRepository.getTagsWithUsageCount(MainActivity.getUid(requireContext())).observe(getViewLifecycleOwner(), tags -> {
            tagStatsContainer.removeAllViews();
            for (TagWithUsage tagWU : tags) {
                TextView tv = new TextView(getContext());
                tv.setText(tagWU.tag.title + " (" + tagWU.usageCount.toString() + ")");
                tv.setPadding(16, 8, 16, 8);
                tv.setBackgroundResource(android.R.drawable.alert_dark_frame);
                tagStatsContainer.addView(tv);
            }
        });

        moodRepository.getMoodsWithTagsByUserId(MainActivity.getUid(requireContext())).observe(getViewLifecycleOwner(), moods -> {
            moodAdapter.setMoods(moods);
        });
    }

}
