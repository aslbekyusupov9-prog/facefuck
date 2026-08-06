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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aifacerating.app.R;
import com.aifacerating.app.network.ApiClient;
import com.aifacerating.app.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardFragment extends Fragment {

    private LinearLayout layoutList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        layoutList = view.findViewById(R.id.layout_leaderboard_list);

        requestLocationPermission();
        fetchLeaderboardFromBackend(view);

        return view;
    }

    private void requestLocationPermission() {
        if (androidx.core.content.ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    private void fetchLeaderboardFromBackend(View view) {
        ApiClient.getService().getLeaderboard().enqueue(new Callback<ApiService.LeaderboardResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<ApiService.LeaderboardResponseDto> call, @NonNull Response<ApiService.LeaderboardResponseDto> response) {
                if (response.isSuccessful() && response.body() != null && response.body().leaderboard != null && !response.body().leaderboard.isEmpty()) {
                    displayLeaderboardData(view, response.body().leaderboard);
                } else {
                    loadFallbackLeaderboard(view);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiService.LeaderboardResponseDto> call, @NonNull Throwable t) {
                loadFallbackLeaderboard(view);
            }
        });
    }

    private void displayLeaderboardData(View view, List<ApiService.LeaderboardItemDto> list) {
        if (!isAdded() || list == null) return;
        layoutList.removeAllViews();

        if (list.size() >= 3) {
            setupPodiumItem(view, R.id.tv_podium_name_1, R.id.tv_podium_score_1, list.get(0));
            setupPodiumItem(view, R.id.tv_podium_name_2, R.id.tv_podium_score_2, list.get(1));
            setupPodiumItem(view, R.id.tv_podium_name_3, R.id.tv_podium_score_3, list.get(2));
        }

        for (int i = 3; i < list.size(); i++) {
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

    private void loadFallbackLeaderboard(View view) {
        if (!isAdded()) return;
        List<ApiService.LeaderboardItemDto> fallbackList = new ArrayList<>();
        String[] names = {"Aziza", "Kamron", "Shaxzoda", "Dilmurod", "Zuhra", "Bekzod", "Sardor", "Lola", "Jasur", "Nigora"};
        int[] scores = {98, 95, 92, 89, 87, 85, 82, 79, 76, 73};

        for (int i = 0; i < names.length; i++) {
            ApiService.LeaderboardItemDto item = new ApiService.LeaderboardItemDto();
            item.rank = i + 1;
            item.nickname = names[i];
            item.overall_score = scores[i];
            item.title = scores[i] >= 85 ? "Mukammal Go'zallik" : "Jozibador";
            item.symmetry_score = scores[i] - 2;
            item.skin_score = scores[i] - 4;
            item.eyes_score = scores[i] - 1;
            item.jaw_score = scores[i] - 3;
            item.golden_ratio_score = scores[i] - 2;
            item.facial_thirds_score = scores[i] - 5;
            fallbackList.add(item);
        }

        displayLeaderboardData(view, fallbackList);
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
