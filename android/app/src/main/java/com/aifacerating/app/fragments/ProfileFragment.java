package com.aifacerating.app.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aifacerating.app.R;
import com.aifacerating.app.utils.HistoryItem;
import com.aifacerating.app.utils.HistoryManager;

import java.util.List;

public class ProfileFragment extends Fragment {

    private LinearLayout layoutHistoryList;
    private TextView tvHistoryCount;
    private TextView tvAvgScore;
    private TextView tvEmptyHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        layoutHistoryList = view.findViewById(R.id.layout_history_list);
        tvHistoryCount = view.findViewById(R.id.tv_history_count);
        tvAvgScore = view.findViewById(R.id.tv_avg_score);
        tvEmptyHistory = view.findViewById(R.id.tv_empty_history);

        loadHistoryData();

        return view;
    }

    private void loadHistoryData() {
        if (getContext() == null) return;
        List<HistoryItem> list = HistoryManager.getHistory(getContext());

        tvHistoryCount.setText(String.valueOf(list.size()));

        if (list.isEmpty()) {
            tvEmptyHistory.setVisibility(View.VISIBLE);
            tvAvgScore.setText("--");
            return;
        }

        tvEmptyHistory.setVisibility(View.GONE);

        int totalScore = 0;
        for (HistoryItem item : list) {
            totalScore += item.getScore();
            addHistoryCard(item);
        }

        int avg = totalScore / list.size();
        tvAvgScore.setText(String.valueOf(avg));
    }

    private void addHistoryCard(HistoryItem item) {
        LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setBackgroundResource(R.drawable.bg_liquid_glass);
        card.setPadding(24, 24, 24, 24);
        card.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        card.setLayoutParams(params);

        // Thumbnail Image
        ImageView ivThumb = new ImageView(getContext());
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(120, 120);
        imgParams.setMargins(0, 0, 24, 0);
        ivThumb.setLayoutParams(imgParams);
        ivThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivThumb.setBackgroundResource(R.drawable.bg_liquid_glass_accent);

        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            try {
                ivThumb.setImageURI(Uri.parse(item.getImageUri()));
            } catch (Exception e) {
                ivThumb.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            ivThumb.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Details (Title & Date)
        LinearLayout details = new LinearLayout(getContext());
        details.setOrientation(LinearLayout.VERTICAL);
        details.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView tvTitle = new TextView(getContext());
        tvTitle.setText(item.getTitle());
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(16);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvDate = new TextView(getContext());
        tvDate.setText(item.getDate());
        tvDate.setTextColor(getResources().getColor(R.color.white_transparent, null));
        tvDate.setTextSize(12);
        tvDate.setPadding(0, 4, 0, 0);

        details.addView(tvTitle);
        details.addView(tvDate);

        // Score Badge
        TextView tvScore = new TextView(getContext());
        tvScore.setText(item.getScore() + " Ball");
        tvScore.setTextSize(18);
        tvScore.setTypeface(null, android.graphics.Typeface.BOLD);
        
        if (item.getScore() >= 85) {
            tvScore.setTextColor(getResources().getColor(R.color.colorAccent, null));
        } else if (item.getScore() >= 70) {
            tvScore.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        } else if (item.getScore() >= 50) {
            tvScore.setTextColor(Color.parseColor("#FFD700"));
        } else {
            tvScore.setTextColor(Color.parseColor("#FF5252"));
        }

        card.addView(ivThumb);
        card.addView(details);
        card.addView(tvScore);

        layoutHistoryList.addView(card);
    }
}
