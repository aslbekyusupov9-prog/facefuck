package com.aifacerating.app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.aifacerating.app.R;

public class UploadFragment extends Fragment {

    private ImageView ivPreview;
    private LinearLayout layoutPlaceholder;
    private TextView tvGenderMale, tvGenderFemale;
    private Button btnCamera, btnGallery, btnAnalyze;
    private String selectedGender = "MALE";
    private Uri selectedImageUri = null;
    private Bitmap selectedBitmap = null;

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    ivPreview.setImageURI(selectedImageUri);
                    layoutPlaceholder.setVisibility(View.GONE);
                }
            }
    );

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    selectedBitmap = (Bitmap) extras.get("data");
                    ivPreview.setImageBitmap(selectedBitmap);
                    layoutPlaceholder.setVisibility(View.GONE);
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        ivPreview = view.findViewById(R.id.iv_preview);
        layoutPlaceholder = view.findViewById(R.id.layout_placeholder);
        tvGenderMale = view.findViewById(R.id.tv_gender_male);
        tvGenderFemale = view.findViewById(R.id.tv_gender_female);
        btnCamera = view.findViewById(R.id.btn_camera);
        btnGallery = view.findViewById(R.id.btn_gallery);
        btnAnalyze = view.findViewById(R.id.btn_analyze);

        updateGenderUI();

        tvGenderMale.setOnClickListener(v -> {
            selectedGender = "MALE";
            updateGenderUI();
        });

        tvGenderFemale.setOnClickListener(v -> {
            selectedGender = "FEMALE";
            updateGenderUI();
        });

        btnGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        });

        btnCamera.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(intent);
        });

        btnAnalyze.setOnClickListener(v -> {
            if (selectedImageUri == null && selectedBitmap == null) {
                Toast.makeText(getContext(), "Iltimos, avval rasm tanlang yoki rasmga oling!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "AI analiz boshlanmoqda (" + selectedGender + ")...", Toast.LENGTH_LONG).show();
            // TODO: Navigate to Result/Scanning Fragment
        });

        return view;
    }

    private void updateGenderUI() {
        if ("MALE".equals(selectedGender)) {
            tvGenderMale.setBackgroundResource(R.drawable.bg_liquid_glass_accent);
            tvGenderFemale.setBackgroundResource(R.drawable.bg_liquid_glass);
        } else {
            tvGenderFemale.setBackgroundResource(R.drawable.bg_liquid_glass_accent);
            tvGenderMale.setBackgroundResource(R.drawable.bg_liquid_glass);
        }
    }
}
