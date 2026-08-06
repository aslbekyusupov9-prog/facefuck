package com.aifacerating.app.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;

import com.aifacerating.app.R;
import com.aifacerating.app.utils.UserProfileManager;

public class SettingsFragment extends Fragment {

    private SwitchCompat switchAutoCrop;
    private SwitchCompat switchMirror;
    private SwitchCompat switchAutoSave;
    private SwitchCompat switchReminder;
    private TextView tvLanguage;
    private Button btnClearHistory;
    private ImageView btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnBack = view.findViewById(R.id.btn_back_settings);
        switchAutoCrop = view.findViewById(R.id.switch_auto_crop);
        switchMirror = view.findViewById(R.id.switch_mirror);
        switchAutoSave = view.findViewById(R.id.switch_auto_save);
        switchReminder = view.findViewById(R.id.switch_reminder);
        tvLanguage = view.findViewById(R.id.tv_current_language);
        btnClearHistory = view.findViewById(R.id.btn_clear_history);

        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .commit();
        });

        loadSettings();

        switchAutoCrop.setOnCheckedChangeListener((b, isChecked) -> UserProfileManager.setAutoCrop(requireContext(), isChecked));
        switchMirror.setOnCheckedChangeListener((b, isChecked) -> UserProfileManager.setMirrorMode(requireContext(), isChecked));
        switchAutoSave.setOnCheckedChangeListener((b, isChecked) -> UserProfileManager.setAutoSaveHistory(requireContext(), isChecked));
        switchReminder.setOnCheckedChangeListener((b, isChecked) -> UserProfileManager.setWeeklyReminder(requireContext(), isChecked));

        tvLanguage.setOnClickListener(v -> showLanguageDialog());

        btnClearHistory.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                .setTitle("Tarixni tozalash")
                .setMessage("Rostdan ham barcha tahlillar tarixini o'chirmoqchimisiz?")
                .setPositiveButton("Ha, O'chirish", (dialog, which) -> {
                    UserProfileManager.clearHistory(requireContext());
                    Toast.makeText(requireContext(), "Tarix tozalandi!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Bekor qilish", null)
                .show();
        });

        return view;
    }

    private void loadSettings() {
        if (getContext() == null) return;
        switchAutoCrop.setChecked(UserProfileManager.isAutoCrop(requireContext()));
        switchMirror.setChecked(UserProfileManager.isMirrorMode(requireContext()));
        switchAutoSave.setChecked(UserProfileManager.isAutoSaveHistory(requireContext()));
        switchReminder.setChecked(UserProfileManager.isWeeklyReminder(requireContext()));
        
        updateLanguageDisplay(UserProfileManager.getLanguage(requireContext()));
    }

    private void showLanguageDialog() {
        String[] languages = {"🇺🇿 O'zbekcha", "🇬🇧 English", "🇷🇺 Русский"};
        String[] codes = {"uz", "en", "ru"};

        new AlertDialog.Builder(requireContext())
            .setTitle("Tilni tanlang")
            .setItems(languages, (dialog, which) -> {
                String chosenCode = codes[which];
                UserProfileManager.setLanguage(requireContext(), chosenCode);
                updateLanguageDisplay(chosenCode);

                // Apply App Language Locale dynamically
                LocaleListCompat appLocales = LocaleListCompat.forLanguageTags(chosenCode);
                AppCompatDelegate.setApplicationLocales(appLocales);

                Toast.makeText(requireContext(), "Til o'zgartirildi: " + languages[which], Toast.LENGTH_SHORT).show();
            })
            .show();
    }

    private void updateLanguageDisplay(String code) {
        if ("en".equals(code)) {
            tvLanguage.setText("🇬🇧 English");
        } else if ("ru".equals(code)) {
            tvLanguage.setText("🇷🇺 Русский");
        } else {
            tvLanguage.setText("🇺🇿 O'zbekcha");
        }
    }
}
