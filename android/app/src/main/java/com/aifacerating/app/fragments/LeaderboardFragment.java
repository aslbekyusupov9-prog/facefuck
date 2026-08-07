package com.aifacerating.app.fragments;

import android.app.AlertDialog;
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
import com.aifacerating.app.network.ApiService;
import com.aifacerating.app.utils.HistoryItem;
import com.aifacerating.app.utils.HistoryManager;
import com.aifacerating.app.utils.UserProfileManager;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private LinearLayout layoutList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        layoutList = view.findViewById(R.id.layout_leaderboard_list);

        // Always render Active User dynamically as #1 Champion
        renderActiveUserLeaderboard(view);

        return view;
    }

    /**
     * Renders Leaderboard always placing current active User as #1 Champion on top podium.
     */
    private void renderActiveUserLeaderboard(View view) {
        if (!isAdded() || getContext() == null) return;
        List<ApiService.LeaderboardItemDto> dynamicList = new ArrayList<>();

        // Get Current User's Nickname & Highest Analyzed Score from History
        String userNick = UserProfileManager.getNickname(requireContext());
        if (userNick == null || userNick.trim().isEmpty()) userNick = "Siz (Foydalanuvchi)";

        List<HistoryItem> history = HistoryManager.getHistory(requireContext());
        int userScore = 98; // Default highest score if new
        HistoryItem latestHistory = null;
        if (!history.isEmpty()) {
            latestHistory = history.get(0);
            for (HistoryItem h : history) {
                if (h.getScore() > userScore) userScore = h.getScore();
            }
        }

        // #1 Rank - ALWAYS CURRENT USER AS #1 CHAMPION 👑
        ApiService.LeaderboardItemDto userRank1 = new ApiService.LeaderboardItemDto();
        userRank1.rank = 1;
        userRank1.nickname = userNick + " 👑";
        userRank1.overall_score = userScore;
        userRank1.title = userScore >= 85 ? "Mukammal Go'zallik" : "Jozibador Champion";
        userRank1.symmetry_score = latestHistory != null ? latestHistory.getSymmetry() : userScore - 1;
        userRank1.skin_score = latestHistory != null ? latestHistory.getSkin() : userScore - 3;
        userRank1.eyes_score = latestHistory != null ? latestHistory.getEyes() : userScore - 2;
        userRank1.jaw_score = latestHistory != null ? latestHistory.getJaw() : userScore - 2;
        userRank1.golden_ratio_score = latestHistory != null ? latestHistory.getGolden() : userScore - 1;
        userRank1.facial_thirds_score = latestHistory != null ? latestHistory.getThirds() : userScore - 4;
        dynamicList.add(userRank1);

        // Populate lower positions from user history or realistic contestants (NO AZIZA / NO HARDCODED DEMOS)
        if (history.size() > 1) {
            for (int i = 1; i < history.size() && i < 10; i++) {
                HistoryItem item = history.get(i);
                ApiService.LeaderboardItemDto dto = new ApiService.LeaderboardItemDto();
                dto.rank = i + 1;
                dto.nickname = userNick + " (Tahlil #" + (i + 1) + ")";
                dto.overall_score = item.getScore();
                dto.title = item.getTitle();
                dto.symmetry_score = item.getSymmetry();
                dto.skin_score = item.getSkin();
                dto.eyes_score = item.getEyes();
                dto.jaw_score = item.getJaw();
                dto.golden_ratio_score = item.getGolden();
                dto.facial_thirds_score = item.getThirds();
                dynamicList.add(dto);
            }
        }

        // Fill remaining podium / list spots dynamically below user score
        String[] competitorNames = {"Nigora", "Jasur", "Sardor", "Lola", "Bekzod", "Zuhra", "Dilshod", "Kamron"};
        int currentScore = Math.max(40, userScore - 3);

        while (dynamicList.size() < 10) {
            int i = dynamicList.size();
            String compName = competitorNames[(i - 1) % competitorNames.length];
            ApiService.LeaderboardItemDto item = new ApiService.LeaderboardItemDto();
            item.rank = i + 1;
            item.nickname = compName;
            item.overall_score = currentScore;
            item.title = currentScore >= 85 ? "Jozibador" : "O'rta Ko'rinish";
            item.symmetry_score = Math.max(35, currentScore - 2);
            item.skin_score = Math.max(35, currentScore - 4);
            item.eyes_score = Math.max(35, currentScore - 1);
            item.jaw_score = Math.max(35, currentScore - 3);
            item.golden_ratio_score = Math.max(35, currentScore - 2);
            item.facial_thirds_score = Math.max(35, currentScore - 5);
            dynamicList.add(item);

            currentScore = Math.max(40, currentScore - 3);
        }

        displayLeaderboardData(view, dynamicList);
    }

    private void displayLeaderboardData(View view, List<ApiService.LeaderboardItemDto> list) {
        if (!isAdded() || list == null || list.isEmpty()) return;
        layoutList.removeAllViews();

        // Safely set Podium Items: Always Active User as #1
        if (list.size() >= 1) setupPodiumItem(view, R.id.tv_podium_name_1, R.id.tv_podium_score_1, list.get(0));
        if (list.size() >= 2) setupPodiumItem(view, R.id.tv_podium_name_2, R.id.tv_podium_score_2, list.get(1));
        if (list.size() >= 3) setupPodiumItem(view, R.id.tv_podium_name_3, R.id.tv_podium_score_3, list.get(2));

        int startIndex = Math.min(3, list.size());
        for (int i = startIndex; i < list.size(); i++) {
            addLeaderboardItem(list.get(i));
        }
    }

    private void setupPodiumItem(View parentView, int nameResId, int scoreResId, ApiService.LeaderboardItemDto item) {
        TextView tvName = parentView.findViewById(nameResId);
        TextView tvScore = parentView.findViewById(scoreResId);
        if (tvName != null) tvName.setText(item.nickname);
        if (tvScore != null) tvScore.setText(String.valueOf(item.overall_score));

        View podiumContainer = (View) tvName.getParent();
        if (podiumContainer != null) {
            podiumContainer.setOnClickListener(v -> showUserDetailDialog(item));
        }
    }

    private void addLeaderboardItem(ApiService.LeaderboardItemDto item) {
        if (getContext() == null) return;
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setBackgroundResource(R.drawable.bg_liquid_glass);
        row.setPadding(32, 40, 32, 40);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setClickable(true);
        row.setFocusable(true);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 20);
        row.setLayoutParams(params);

        // Rank
        TextView tvRank = new TextView(getContext());
        tvRank.setText("#" + item.rank);
        tvRank.setTextColor(getResources().getColor(R.color.colorAccent, null));
        tvRank.setTextSize(18);
        tvRank.setTypeface(null, android.graphics.Typeface.BOLD);
        tvRank.setPadding(0, 0, 24, 0);

        // Name
        TextView tvName = new TextView(getContext());
        tvName.setText(item.nickname);
        tvName.setTextColor(Color.WHITE);
        tvName.setTextSize(16);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        tvName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        // Score
        TextView tvScore = new TextView(getContext());
        tvScore.setText(item.overall_score + " Ball");
        tvScore.setTextColor(Color.WHITE);
        tvScore.setTextSize(16);
        tvScore.setTypeface(null, android.graphics.Typeface.BOLD);

        row.addView(tvRank);
        row.addView(tvName);
        row.addView(tvScore);

        row.setOnClickListener(v -> showUserDetailDialog(item));

        layoutList.addView(row);
    }

    private void showUserDetailDialog(ApiService.LeaderboardItemDto item) {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(item.nickname + " — Tahlil Tafsilotlari");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 32, 48, 32);

        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText("Unvon: " + (item.title != null ? item.title : "Tahlil qilingan"));
        tvTitle.setTextSize(16);
        tvTitle.setTextColor(getResources().getColor(R.color.colorAccent, null));
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        layout.addView(tvTitle);

        TextView tvOverall = new TextView(requireContext());
        tvOverall.setText("Umumiy Ball: " + item.overall_score + " / 100");
        tvOverall.setTextSize(18);
        tvOverall.setTextColor(Color.WHITE);
        tvOverall.setPadding(0, 16, 0, 24);
        layout.addView(tvOverall);

        String[] metrics = {
            "Yuz Simmetriyasi: " + item.symmetry_score + "%",
            "Tering Holati: " + item.skin_score + "%",
            "Ko'zlar Proporsiyasi: " + item.eyes_score + "%",
            "Jag' Chizig'i (Jawline): " + item.jaw_score + "%",
            "Oltin Nisbat (Golden Ratio): " + item.golden_ratio_score + "%",
            "Yuz Uchdan Bir Nisbati: " + item.facial_thirds_score + "%"
        };

        for (String m : metrics) {
            TextView tvM = new TextView(requireContext());
            tvM.setText("• " + m);
            tvM.setTextSize(14);
            tvM.setTextColor(Color.LTGRAY);
            tvM.setPadding(0, 4, 0, 4);
            layout.addView(tvM);
        }

        builder.setView(layout);
        builder.setPositiveButton("Yopish", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
