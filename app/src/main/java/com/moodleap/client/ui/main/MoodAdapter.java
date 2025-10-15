package com.moodleap.client.ui.main;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moodleap.client.MainActivity;
import com.moodleap.client.R;
import com.moodleap.client.db.Converters;
import com.moodleap.client.db.entity.Mood;
import com.moodleap.client.db.entity.MoodWithTags;
import com.moodleap.client.db.entity.Tag;

import java.util.ArrayList;
import java.util.List;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {
    private List<MoodWithTags> moods = new ArrayList<>();

    public void setMoods(List<MoodWithTags> newMoods) {
        this.moods = newMoods;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mood_card, parent, false);
        return new MoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
        MoodWithTags item = moods.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return moods.size();
    }

    static class MoodViewHolder extends RecyclerView.ViewHolder {
        TextView moodTitle, moodDate, moodTags;

        MoodViewHolder(@NonNull View itemView) {
            super(itemView);
            moodTitle = itemView.findViewById(R.id.moodTitle);
            moodDate = itemView.findViewById(R.id.moodDate);
            moodTags = itemView.findViewById(R.id.moodTags);
        }

        void bind(MoodWithTags item) {
            moodTitle.setText(MoodConverter.longToString(item.mood.emotion));
            moodDate.setText(Converters.toLocalDateTime(item.mood.timestamp).toString());
            List<String> tagNames = new ArrayList<>();
            for (Tag tag : item.tags) {
                tagNames.add(tag.title);
            }
            moodTags.setText("Tags: " + TextUtils.join(", ", tagNames));
        }
    }
}
