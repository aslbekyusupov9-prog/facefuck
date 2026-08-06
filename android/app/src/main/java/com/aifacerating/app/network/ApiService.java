package com.aifacerating.app.network;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    class LeaderboardItemDto {
        public int rank;
        public String nickname;
        public String gender;
        public int overall_score;
        public String title;
        public String avatar_url;
    }

    class LeaderboardResponseDto {
        public int total_users;
        public List<LeaderboardItemDto> leaderboard;
    }

    class FaceAnalysisSaveDto {
        public String device_id;
        public int overall_score;
        public int symmetry_score;
        public int skin_score;
        public int eyes_score;
        public int jaw_score;
        public int golden_ratio_score;
        public int facial_thirds_score;
        public String title;
        public String description;

        public FaceAnalysisSaveDto(String device_id, int overall_score, int symmetry_score, 
                                   int skin_score, int eyes_score, int jaw_score, 
                                   int golden_ratio_score, int facial_thirds_score, 
                                   String title, String description) {
            this.device_id = device_id;
            this.overall_score = overall_score;
            this.symmetry_score = symmetry_score;
            this.skin_score = skin_score;
            this.eyes_score = eyes_score;
            this.jaw_score = jaw_score;
            this.golden_ratio_score = golden_ratio_score;
            this.facial_thirds_score = facial_thirds_score;
            this.title = title;
            this.description = description;
        }
    }

    class ApiResponseDto {
        public String status;
        public String message;
    }

    @GET("api/v1/leaderboard")
    Call<LeaderboardResponseDto> getLeaderboard();

    @POST("api/v1/analysis/save")
    Call<ApiResponseDto> saveAnalysis(@Body FaceAnalysisSaveDto dto);
}
