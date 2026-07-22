import React, { useEffect, useState } from 'react';
import { Trophy, HeartHandshake, Sparkles, Share2, RotateCcw } from 'lucide-react';
import confetti from 'canvas-confetti';

import { getTranslation } from '../services/translations';

export default function ResultScreen({ imageSrc, scores, gender, onReAnalyze, onGoLeaderboard, onGoTips, currentLang = 'uz' }) {
  const [animatedOverall, setAnimatedOverall] = useState(0);
  const t = (key) => getTranslation(currentLang, key);

  useEffect(() => {
    // Fire celebratory confetti for overall score > 85
    if (scores.overall >= 85) {
      confetti({
        particleCount: 50,
        spread: 60,
        origin: { y: 0.6 }
      });
    }

    // Score count-up animation
    let start = 0;
    const duration = 1500;
    const increment = scores.overall / (duration / 20);

    const timer = setInterval(() => {
      start += increment;
      if (start >= scores.overall) {
        setAnimatedOverall(scores.overall);
        clearInterval(timer);
      } else {
        setAnimatedOverall(Math.floor(start));
      }
    }, 20);

    return () => clearInterval(timer);
  }, [scores]);

  // Color helper based on strict guidelines:
  // 90+: Mint-green -> Cyan
  // 70-89: Amber
  // <70: Gentle soft cyan/purple (never red!)
  const getMetricStyle = (val) => {
    if (val >= 90) {
      return {
        text: 'text-[#22F0B6]',
        bg: 'from-[#22F0B6] to-[#22D3EE]',
        badge: 'bg-[#22F0B6]/20 text-[#22F0B6] border-[#22F0B6]/40',
        label: 'A\'lo'
      };
    } else if (val >= 70) {
      return {
        text: 'text-[#FFC145]',
        bg: 'from-[#FFC145] to-[#FF8A3D]',
        badge: 'bg-[#FFC145]/20 text-[#FFC145] border-[#FFC145]/40',
        label: 'Yaxshi'
      };
    } else {
      return {
        text: 'text-[#38BDF8]',
        bg: 'from-[#818CF8] to-[#38BDF8]',
        badge: 'bg-[#38BDF8]/20 text-[#38BDF8] border-[#38BDF8]/40',
        label: 'Normal'
      };
    }
  };

  const metricsList = [
    { key: 'potential', label: t('scorePotential'), val: scores.potential },
    { key: 'skinQuality', label: t('scoreSkin'), val: scores.skinQuality },
    { key: 'jawline', label: t('scoreJawline'), val: scores.jawline },
    { key: 'cheekbones', label: t('scoreCheekbones'), val: scores.cheekbones },
    { key: 'symmetry', label: t('scoreSymmetry'), val: scores.symmetry },
    { key: 'genderScore', label: gender === 'female' ? 'Femininity' : 'Masculinity', val: scores.genderScore },
  ];

  const overallStyle = getMetricStyle(scores.overall);

  return (
    <div className="space-y-5 animate-fadeIn pb-4">
      {/* Top Header */}
      <div className="text-center space-y-1">
        <h2 className="text-xl font-bold font-heading text-white">{t('resultTitle')}</h2>
        <p className="text-xs text-white/60">{t('resultSubtitle')}</p>
      </div>

      {/* Central Pulsating Avatar & Big Score */}
      <div className="liquid-glass rounded-card p-5 flex flex-col items-center justify-center space-y-4 border border-white/20 relative">
        <div className="relative w-28 h-28 rounded-full p-1 border-2 border-[#E63946] pulse-avatar shadow-red-pulse">
          <img
            src={imageSrc}
            alt="Profil"
            className="w-full h-full object-cover rounded-full"
          />
          <div className="absolute -bottom-2 -right-2 w-8 h-8 rounded-full liquid-glass-gold flex items-center justify-center border border-[#F4C430]">
            <Sparkles className="w-4 h-4 text-[#F4C430]" />
          </div>
        </div>

        {/* Big Overall Animated Score */}
        <div className="text-center space-y-1">
          <div className="text-5xl font-extrabold font-mono tracking-tight text-white flex items-baseline justify-center space-x-1">
            <span className={overallStyle.text}>{animatedOverall}</span>
            <span className="text-lg text-white/50 font-normal">/100</span>
          </div>
          <div className={`inline-block px-3 py-1 rounded-full text-xs font-semibold font-heading uppercase tracking-wider border shadow-md ${overallStyle.badge}`}>
            {scores.overall >= 90 ? 'Modelesque Harmony' : scores.overall >= 80 ? 'Stunning Symmetry' : 'Classic Proportion'}
          </div>
        </div>
      </div>

      {/* 2-Column Metrics Grid */}
      <div className="grid grid-cols-2 gap-3">
        {metricsList.map((m, idx) => {
          const style = getMetricStyle(m.val);
          return (
            <div key={idx} className="liquid-glass rounded-2xl p-3 space-y-2 border border-white/10 hover:border-white/20 transition-all">
              <div className="flex items-center justify-between">
                <span className="text-[11px] font-medium text-white/70 truncate">{m.label}</span>
                <span className={`text-xs font-mono font-bold ${style.text}`}>{m.val}</span>
              </div>

              {/* Animated Custom Progress Bar */}
              <div className="w-full h-2 bg-black/40 rounded-full overflow-hidden p-0.5 border border-white/5">
                <div
                  className={`h-full rounded-full bg-gradient-to-r ${style.bg} transition-all duration-1000`}
                  style={{ width: `${m.val}%` }}
                />
              </div>
            </div>
          );
        })}
      </div>

      {/* Action Buttons */}
      <div className="space-y-2.5 pt-2">
        <button
          onClick={onReAnalyze}
          className="w-full py-3.5 rounded-btn liquid-glass-gold text-white font-bold font-heading text-sm flex items-center justify-center space-x-2 shadow-gold-glow hover:opacity-95 transition-all"
        >
          <RotateCcw className="w-4 h-4 text-[#F4C430]" />
          <span>{t('reanalyzeBtn')}</span>
        </button>

        <button
          onClick={onGoTips}
          className="w-full py-3.5 rounded-btn liquid-glass-accent text-white font-bold font-heading text-sm flex items-center justify-center space-x-2 shadow-red-pulse hover:opacity-95 transition-all"
        >
          <HeartHandshake className="w-4 h-4 text-[#22F0B6]" />
          <span>{t('viewTipsBtn')}</span>
        </button>
      </div>
    </div>
  );
}
