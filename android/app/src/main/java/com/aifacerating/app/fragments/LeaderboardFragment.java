package com.aifacerating.app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.aifacerating.app.R;

public class LeaderboardFragment extends Fragment {

    private LinearLayout layoutList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        layoutList = view.findViewById(R.id.layout_leaderboard_list);

        loadMockData();

        return view;
    }

    private void loadMockData() {
        String[] names = {"Aziza", "Kamron", "Shaxzoda", "Dilmurod", "Zuhra", "Bekzod"};
        int[] scores = {98, 95, 92, 89, 87, 81};
        String[] genders = {"Ayol", "Erkak", "Ayol", "Erkak", "Ayol", "Erkak"};

        for (int i = 0; i < names.length; i++) {
            addLeaderboardItem(i + 1, names[i], scores[i], genders[i]);
        }
    }

    private void addLeaderboardItem(int rank, String name, int score, String gender) {
        LinearLayout item = new LinearLayout(getContext());
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setBackgroundResource(R.drawable.bg_liquid_glass);
        item.setPadding(32, 48, 32, 48);
        item.setGravity(Gravity.CENTER_VERTICAL);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 24);
        item.setLayoutParams(params);

        // Rank
        TextView tvRank = new TextView(getContext());
        tvRank.setText("#" + rank);
        tvRank.setTextColor(getResources().getColor(R.color.colorAccent, null));
        tvRank.setTextSize(20);
        tvRank.setTypeface(null, android.graphics.Typeface.BOLD);
        tvRank.setPadding(0, 0, 32, 0);

        // Name
        TextView tvName = new TextView(getContext());
        tvName.setText(name);
        tvName.setTextColor(Color.WHITE);
        tvName.setTextSize(18);
        tvName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        // Score
        TextView tvScore = new TextView(getContext());
        tvScore.setText(score + " Ball");
        tvScore.setTextColor(Color.WHITE);
        tvScore.setTextSize(18);
        tvScore.setTypeface(null, android.graphics.Typeface.BOLD);

        item.addView(tvRank);
        item.addView(tvName);
        item.addView(tvScore);

        layoutList.addView(item);
    }
}
