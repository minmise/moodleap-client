package com.moodleap.client.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moodleap.client.DatabaseManager;
import com.moodleap.client.MainActivity;
import com.moodleap.client.R;
import com.moodleap.client.UserManager;
import com.moodleap.client.db.Converters;
import com.moodleap.client.db.entity.Mood;
import com.moodleap.client.db.entity.MoodWithTags;
import com.moodleap.client.db.entity.Tag;
import com.moodleap.client.db.entity.TagWithUsage;
import com.moodleap.client.db.repository.MoodRepository;
import com.moodleap.client.db.repository.TagRepository;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsFragment extends Fragment {

    private LinearLayout tagStatsContainer;
    private RecyclerView moodHistoryRecycler;
    private MoodAdapter moodAdapter;
    private Button btnToCSV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        tagStatsContainer = view.findViewById(R.id.tagStatsContainer);
        moodHistoryRecycler = view.findViewById(R.id.moodHistoryRecycler);
        btnToCSV = view.findViewById(R.id.exportCsvButton);

        moodAdapter = new MoodAdapter();
        moodHistoryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        moodHistoryRecycler.setAdapter(moodAdapter);

        observeData();

        return view;
    }

    private void shareFile(File file) {
        Uri uri = FileProvider.getUriForFile(
                requireContext(),
                requireActivity().getPackageName() + ".provider",
                file
        );

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(intent, "Send CSV"));
    }

    private void observeData() {
        TagRepository tagRepository = DatabaseManager.getTagRepository();
        MoodRepository moodRepository = DatabaseManager.getMoodRepository();

        tagRepository.getTagsWithUsageCount(UserManager.getUid(requireContext())).observe(getViewLifecycleOwner(), tags -> {
            tagStatsContainer.removeAllViews();
            for (TagWithUsage tagWU : tags) {
                TextView tv = new TextView(getContext());
                tv.setText(tagWU.tag.title + " (" + tagWU.usageCount.toString() + ")");
                tv.setPadding(16, 8, 16, 8);
                tv.setBackgroundResource(android.R.drawable.alert_dark_frame);
                tagStatsContainer.addView(tv);
            }
        });

        moodRepository.getMoodsWithTagsByUserId(UserManager.getUid(requireContext())).observe(getViewLifecycleOwner(), moods -> {
            moodAdapter.setMoods(moods);
            btnToCSV.setOnClickListener(v -> {
                try {
                    StringBuilder csvData = new StringBuilder();
                    csvData.append("Date,Mood,Tags\n");
                    for (MoodWithTags mood : moods) {
                        csvData.append(Converters.toLocalDateTime(mood.mood.timestamp)).append(",")
                                .append(mood.mood.emotion).append(",")
                                .append(TextUtils.join("; ", mood.tags.stream().map(t->t.title).collect(Collectors.toList()))).append("\n");
                    }
                    String fileName = "moods_" + System.currentTimeMillis() + ".csv";
                    File file = new File(requireActivity().getExternalFilesDir(null), fileName);
                    FileWriter writer = new FileWriter(file);
                    writer.append(csvData.toString());
                    writer.flush();
                    writer.close();
                    Toast.makeText(requireContext(), "File saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    shareFile(file);

                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Export error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

}
