package com.aifacerating.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aifacerating.app.R;
import com.aifacerating.app.utils.HistoryItem;
import com.aifacerating.app.utils.HistoryManager;

import java.util.List;

public class TipsFragment extends Fragment {

    private View cardPersonalTip;
    private TextView tvPersonalTipText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips, container, false);

        cardPersonalTip = view.findViewById(R.id.card_personal_ai_tip);
        tvPersonalTipText = view.findViewById(R.id.tv_personal_tip_text);

        loadPersonalizedAITip();

        return view;
    }

    private void loadPersonalizedAITip() {
        if (getContext() == null) return;
        List<HistoryItem> history = HistoryManager.getHistory(requireContext());

        if (history != null && !history.isEmpty()) {
            HistoryItem latest = history.get(0);
            cardPersonalTip.setVisibility(View.VISIBLE);

            int minScore = latest.getSymmetry();
            String lowestMetric = "Simmetriya";

            if (latest.getSkin() < minScore) {
                minScore = latest.getSkin();
                lowestMetric = "Teri Holati";
            }
            if (latest.getEyes() < minScore) {
                minScore = latest.getEyes();
                lowestMetric = "Ko'zlar Proporsiyasi";
            }
            if (latest.getJaw() < minScore) {
                minScore = latest.getJaw();
                lowestMetric = "Jag' Chizig'i (Jawline)";
            }
            if (latest.getGolden() < minScore) {
                minScore = latest.getGolden();
                lowestMetric = "Oltin Nisbat";
            }
            if (latest.getThirds() < minScore) {
                minScore = latest.getThirds();
                lowestMetric = "Yuz Proporsiyalari";
            }

            String advice;
            if ("Teri Holati".equals(lowestMetric)) {
                advice = "So'nggi tahlilingizda 'Teri Holati' (" + minScore + "%) nisbatan past baholandi. Tabiiy quyosh yorug'ligida rasmga tushish va yuzni namlantiruvchi parvarish tavsiya etiladi.";
            } else if ("Simmetriya".equals(lowestMetric)) {
                advice = "So'nggi tahlilingizda 'Yuz Simmetriyasi' (" + minScore + "%) nisbatan pastroq bo'ldi. Kameraga o'ng/chapga burilmasdan to'g'ri va tikka qarash tavsiya etiladi.";
            } else if ("Jag' Chizig'i (Jawline)".equals(lowestMetric)) {
                advice = "So'nggi tahlilingizda 'Jag' Chizig'i' (" + minScore + "%) pastroq baholandi. Jag'ingiz yaqqol ko'rinishi uchun kamerani biroz yuqoriroq burchakda tuting.";
            } else {
                advice = "So'nggi tahlilingizda '" + lowestMetric + "' ko'rsatkichi (" + minScore + "%) pastroq. Har gal rasmga tushishda kameraga neytral (mimikasiz) hamda 720p HD ruxsatda qarashingiz ballni oshiradi.";
            }

            tvPersonalTipText.setText(advice);
        } else {
            cardPersonalTip.setVisibility(View.GONE);
        }
    }
}
