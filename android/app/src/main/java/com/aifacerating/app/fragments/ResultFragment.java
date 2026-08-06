package com.aifacerating.app.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.aifacerating.app.R;
import java.util.Random;

public class ResultFragment extends Fragment {

    private View layoutScanning;
    private View layoutResult;
    private TextView tvScore, tvTitle, tvDescription;
    private Button btnRetry;
    
    private String gender = "MALE"; // Passed from UploadFragment

    public static ResultFragment newInstance(String gender) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString("GENDER", gender);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gender = getArguments().getString("GENDER", "MALE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        layoutScanning = view.findViewById(R.id.layout_scanning);
        layoutResult = view.findViewById(R.id.layout_result_scroll);
        tvScore = view.findViewById(R.id.tv_score);
        tvTitle = view.findViewById(R.id.tv_title);
        tvDescription = view.findViewById(R.id.tv_description);
        btnRetry = view.findViewById(R.id.btn_retry);

        btnRetry.setOnClickListener(v -> {
            // Go back to upload
            requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new UploadFragment())
                .commit();
        });

        // Set uploaded image for the scanner overlay
        android.widget.ImageView ivScannedImage = view.findViewById(R.id.iv_scanned_image);
        com.aifacerating.app.utils.ImageHolder holder = com.aifacerating.app.utils.ImageHolder.getInstance();
        if (holder.getUri() != null) {
            ivScannedImage.setImageURI(holder.getUri());
        } else if (holder.getBitmap() != null) {
            ivScannedImage.setImageBitmap(holder.getBitmap());
        }

        // Simulate AI processing for 3 seconds
        simulateAIProcessing();

        return view;
    }

    private void simulateAIProcessing() {
        layoutScanning.setVisibility(View.VISIBLE);
        layoutResult.setVisibility(View.GONE);

        new Handler(Looper.getMainLooper()).postDelayed(this::showResult, 3000);
    }

    private void showResult() {
        if (!isAdded()) return; // Check if fragment is still attached to avoid crash
        
        layoutScanning.setVisibility(View.GONE);
        layoutResult.setVisibility(View.VISIBLE);

        // Deterministic Random based on uploaded image pixels
        long seed = com.aifacerating.app.utils.ImageHolder.getInstance().getImageHash(requireContext());
        Random r = new Random(seed);

        // Generate deterministic score between 65 and 99
        int score = r.nextInt(35) + 65;
        tvScore.setText(String.valueOf(score));

        if (score >= 90) {
            tvTitle.setText("Mukammal Go'zallik");
            tvTitle.setTextColor(getResources().getColor(R.color.colorAccent, null));
            tvDescription.setText(gender.equals("MALE") ? 
                "Sizning yuz tuzilishingiz juda maskulin va jozibali. Xuddi Gollivud aktyorlaridek!" :
                "Yuz chiziqlaringiz aqlbovar qilmas darajada go'zal va mutanosib.");
        } else if (score >= 80) {
            tvTitle.setText("Jozibador");
            tvTitle.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            tvDescription.setText(gender.equals("MALE") ? 
                "Yaxshi proporsiya va o'ziga xos xarizma bor." :
                "Juda chiroyli va tabiiy go'zallikka egasiz.");
        } else {
            tvTitle.setText("O'ziga Xos");
            tvTitle.setTextColor(getResources().getColor(R.color.white, null));
            tvDescription.setText("Standart qoliplarga tushmaydigan, lekin juda e'tiborni tortuvchi yuz tuzilishi.");
        }

        // Setup detailed metrics dynamically
        TextView tvSymmetry = requireView().findViewById(R.id.tv_metric_symmetry);
        TextView tvSkin = requireView().findViewById(R.id.tv_metric_skin);
        TextView tvEyes = requireView().findViewById(R.id.tv_metric_eyes);
        TextView tvJaw = requireView().findViewById(R.id.tv_metric_jaw);
        TextView tvGolden = requireView().findViewById(R.id.tv_metric_golden);
        TextView tvThirds = requireView().findViewById(R.id.tv_metric_thirds);

        int symScore = Math.min(100, Math.max(70, score + (r.nextInt(10) - 5)));
        int skinScore = Math.min(100, Math.max(65, score + (r.nextInt(15) - 5)));
        int eyeScore = Math.min(100, Math.max(75, score + (r.nextInt(8) - 3)));
        int jawScore = Math.min(100, Math.max(60, score + (r.nextInt(12) - 6)));
        int goldenScore = Math.min(100, Math.max(72, score + (r.nextInt(10) - 4)));
        int thirdsScore = Math.min(100, Math.max(68, score + (r.nextInt(14) - 7)));

        tvSymmetry.setText(symScore + "%");
        tvSkin.setText(skinScore + "%");
        tvEyes.setText(eyeScore + "%");
        tvJaw.setText(jawScore + "%");
        tvGolden.setText(goldenScore + "%");
        tvThirds.setText(thirdsScore + "%");
    }
}
