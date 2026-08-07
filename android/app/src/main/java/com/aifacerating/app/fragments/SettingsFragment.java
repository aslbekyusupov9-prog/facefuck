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
import com.aifacerating.app.utils.UpdateManager;
import com.aifacerating.app.utils.UserProfileManager;

public class SettingsFragment extends Fragment {

    private SwitchCompat switchAutoCrop;
    private SwitchCompat switchMirror;
    private SwitchCompat switchAutoSave;
    private SwitchCompat switchReminder;
    private TextView tvLanguage;
    private Button btnClearHistory;
    private Button btnCheckUpdate;
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
        btnCheckUpdate = view.findViewById(R.id.btn_check_update);

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
                .setTitle(R.string.clear_history_title)
                .setMessage(R.string.clear_history_message)
                .setPositiveButton(R.string.confirm_delete, (dialog, which) -> {
                    UserProfileManager.clearHistory(requireContext());
                    Toast.makeText(requireContext(), R.string.history_cleared, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
        });

        if (btnCheckUpdate != null) {
            btnCheckUpdate.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Yangilanishlar tekshirilmoqda...", Toast.LENGTH_SHORT).show();
                UpdateManager.checkForUpdates(requireActivity(), true);
            });
        }

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
        String[] languages = {
            getString(R.string.language_uz),
            getString(R.string.language_en),
            getString(R.string.language_ru)
        };
        String[] codes = {"uz", "en", "ru"};

        new AlertDialog.Builder(requireContext())
            .setTitle(R.string.language_dialog_title)
            .setItems(languages, (dialog, which) -> {
                String chosenCode = codes[which];
                UserProfileManager.setLanguage(requireContext(), chosenCode);
                updateLanguageDisplay(chosenCode);

                // Apply App Language Locale dynamically via AppCompatDelegate
                LocaleListCompat appLocales = LocaleListCompat.forLanguageTags(chosenCode);
                AppCompatDelegate.setApplicationLocales(appLocales);

                Toast.makeText(requireContext(), getString(R.string.language_changed, languages[which]), Toast.LENGTH_SHORT).show();

                // Recreate Activity so all UI components re-inflate immediately with newly selected language
                if (getActivity() != null) {
                    getActivity().recreate();
                }
            })
            .show();
    }

    private void updateLanguageDisplay(String code) {
        if ("en".equals(code)) {
            tvLanguage.setText(R.string.language_en);
        } else if ("ru".equals(code)) {
            tvLanguage.setText(R.string.language_ru);
        } else {
            tvLanguage.setText(R.string.language_uz);
        }
    }
}
