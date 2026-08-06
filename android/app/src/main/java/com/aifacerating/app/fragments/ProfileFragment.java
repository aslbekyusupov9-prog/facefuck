package com.aifacerating.app.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.aifacerating.app.utils.HistoryItem;
import com.aifacerating.app.utils.HistoryManager;
import com.aifacerating.app.utils.UserProfileManager;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ProfileFragment extends Fragment {

    private LinearLayout layoutHistoryList;
    private TextView tvHistoryCount;
    private TextView tvAvgScore;
    private TextView tvEmptyHistory;

    private ShapeableImageView ivUserAvatar;
    private TextView tvAvatarPlaceholder;
    private TextView tvUserNickname;
    private TextView tvUserBio;
    private FrameLayout layoutAvatarClick;
    private LinearLayout layoutEditNickname;
    private ImageView btnOpenSettings;

    private final ActivityResultLauncher<Intent> avatarGalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedUri = result.getData().getData();
                    if (selectedUri != null) {
                        UserProfileManager.setAvatarUri(requireContext(), selectedUri.toString());
                        updateProfileUI();
                        Toast.makeText(getContext(), "Profil rasmi yangilandi!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        layoutHistoryList = view.findViewById(R.id.layout_history_list);
        tvHistoryCount = view.findViewById(R.id.tv_history_count);
        tvAvgScore = view.findViewById(R.id.tv_avg_score);
        tvEmptyHistory = view.findViewById(R.id.tv_empty_history);

        ivUserAvatar = view.findViewById(R.id.iv_user_avatar);
        tvAvatarPlaceholder = view.findViewById(R.id.tv_avatar_placeholder);
        tvUserNickname = view.findViewById(R.id.tv_user_nickname);
        tvUserBio = view.findViewById(R.id.tv_user_bio);
        layoutAvatarClick = view.findViewById(R.id.layout_avatar_click);
        layoutEditNickname = view.findViewById(R.id.layout_edit_nickname);
        btnOpenSettings = view.findViewById(R.id.btn_open_settings);

        updateProfileUI();
        loadHistoryData();

        // Change Avatar on Click
        layoutAvatarClick.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            avatarGalleryLauncher.launch(intent);
        });

        // Edit Profile (Nickname & Bio) on Click
        layoutEditNickname.setOnClickListener(v -> showEditProfileDialog());

        // Open Settings Fragment
        btnOpenSettings.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();
        });

        return view;
    }

    private void updateProfileUI() {
        if (getContext() == null) return;

        String nickname = UserProfileManager.getNickname(getContext());
        String bio = UserProfileManager.getBio(getContext());
        tvUserNickname.setText(nickname);
        tvUserBio.setText(bio);

        String avatarUriStr = UserProfileManager.getAvatarUri(getContext());
        if (avatarUriStr != null && !avatarUriStr.isEmpty()) {
            try {
                ivUserAvatar.setImageURI(Uri.parse(avatarUriStr));
                ivUserAvatar.setVisibility(View.VISIBLE);
                tvAvatarPlaceholder.setVisibility(View.GONE);
            } catch (Exception e) {
                ivUserAvatar.setVisibility(View.GONE);
                tvAvatarPlaceholder.setVisibility(View.VISIBLE);
                tvAvatarPlaceholder.setText(nickname.isEmpty() ? "F" : nickname.substring(0, 1).toUpperCase());
            }
        } else {
            ivUserAvatar.setVisibility(View.GONE);
            tvAvatarPlaceholder.setVisibility(View.VISIBLE);
            tvAvatarPlaceholder.setText(nickname.isEmpty() ? "F" : nickname.substring(0, 1).toUpperCase());
        }
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Profilni Tahrirlash");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 24);

        TextView lblNick = new TextView(requireContext());
        lblNick.setText("Ismingiz / Nickname:");
        lblNick.setTextSize(14);
        lblNick.setTextColor(Color.GRAY);
        layout.addView(lblNick);

        final EditText etNickname = new EditText(requireContext());
        etNickname.setText(UserProfileManager.getNickname(requireContext()));
        layout.addView(etNickname);

        TextView lblBio = new TextView(requireContext());
        lblBio.setText("Bio (Siz haqingizda qisqa matn):");
        lblBio.setTextSize(14);
        lblBio.setTextColor(Color.GRAY);
        lblBio.setPadding(0, 24, 0, 0);
        layout.addView(lblBio);

        final EditText etBio = new EditText(requireContext());
        etBio.setText(UserProfileManager.getBio(requireContext()));
        layout.addView(etBio);

        builder.setView(layout);

        builder.setPositiveButton("Saqlash", (dialog, which) -> {
            String newName = etNickname.getText().toString().trim();
            String newBio = etBio.getText().toString().trim();
            if (!newName.isEmpty()) {
                UserProfileManager.setNickname(requireContext(), newName);
                UserProfileManager.setBio(requireContext(), newBio);
                updateProfileUI();
                Toast.makeText(getContext(), "Profil saqlandi!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Bekor qilish", (dialog, which) -> dialog.cancel());
        builder.show();
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
        ShapeableImageView ivThumb = new ShapeableImageView(getContext());
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
