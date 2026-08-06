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
                    loadMockData(view);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiService.LeaderboardResponseDto> call, @NonNull Throwable t) {
                // Fallback to local data if backend server is not running
                loadMockData(view);
            }
        });
    }

    private void displayLeaderboardData(View view, List<ApiService.LeaderboardItemDto> list) {
        if (!isAdded() || list == null) return;
        layoutList.removeAllViews();

        if (list.size() >= 3) {
            ((TextView) view.findViewById(R.id.tv_podium_name_1)).setText(list.get(0).nickname);
            ((TextView) view.findViewById(R.id.tv_podium_score_1)).setText(String.valueOf(list.get(0).overall_score));
            
            ((TextView) view.findViewById(R.id.tv_podium_name_2)).setText(list.get(1).nickname);
            ((TextView) view.findViewById(R.id.tv_podium_score_2)).setText(String.valueOf(list.get(1).overall_score));
            
            ((TextView) view.findViewById(R.id.tv_podium_name_3)).setText(list.get(2).nickname);
            ((TextView) view.findViewById(R.id.tv_podium_score_3)).setText(String.valueOf(list.get(2).overall_score));
        }

        for (int i = 3; i < list.size(); i++) {
            addLeaderboardItem(list.get(i).rank, list.get(i).nickname, list.get(i).overall_score);
        }
    }

    private void loadMockData(View view) {
        if (!isAdded()) return;
        String[] names = {"Aziza", "Kamron", "Shaxzoda", "Dilmurod", "Zuhra", "Bekzod", "Sardor", "Lola"};
        int[] scores = {98, 95, 92, 89, 87, 81, 78, 75};

        if (names.length >= 3) {
            ((TextView) view.findViewById(R.id.tv_podium_name_1)).setText(names[0]);
            ((TextView) view.findViewById(R.id.tv_podium_score_1)).setText(String.valueOf(scores[0]));
            
            ((TextView) view.findViewById(R.id.tv_podium_name_2)).setText(names[1]);
            ((TextView) view.findViewById(R.id.tv_podium_score_2)).setText(String.valueOf(scores[1]));
            
            ((TextView) view.findViewById(R.id.tv_podium_name_3)).setText(names[2]);
            ((TextView) view.findViewById(R.id.tv_podium_score_3)).setText(String.valueOf(scores[2]));
        }

        layoutList.removeAllViews();
        for (int i = 3; i < names.length; i++) {
            addLeaderboardItem(i + 1, names[i], scores[i]);
        }
    }

    private void addLeaderboardItem(int rank, String name, int score) {
        if (getContext() == null) return;
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
