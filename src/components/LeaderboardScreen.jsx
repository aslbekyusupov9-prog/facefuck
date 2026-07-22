import React, { useState, useEffect } from 'react';
import { MapPin, Trophy, Shield, Crown, Navigation, X, Sparkles, UserCheck } from 'lucide-react';
import { encodeGeohash } from '../services/geohash';

// Mock surrounding leaderboard users within 20km with detailed biometric metrics
const INITIAL_MOCK_LEADERBOARD = [
  {
    rank: 1,
    nickname: 'ViperX',
    distanceKm: 2.4,
    score: 96,
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80',
    details: {
      potential: 98,
      symmetry: 97,
      goldenRatio: 96,
      skinQuality: 94,
      jawline: 95,
      cheekbones: 96
    }
  },
  {
    rank: 2,
    nickname: 'ShadowDragon',
    distanceKm: 4.8,
    score: 94,
    avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80',
    details: {
      potential: 97,
      symmetry: 95,
      goldenRatio: 94,
      skinQuality: 92,
      jawline: 94,
      cheekbones: 93
    }
  },
  {
    rank: 3,
    nickname: 'AestheticKing',
    distanceKm: 8.1,
    score: 92,
    avatar: 'https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80',
    details: {
      potential: 95,
      symmetry: 93,
      goldenRatio: 92,
      skinQuality: 90,
      jawline: 92,
      cheekbones: 91
    }
  },
  {
    rank: 4,
    nickname: 'SilkRoadGuy',
    distanceKm: 11.3,
    score: 89,
    avatar: 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&w=300&q=80',
    details: {
      potential: 93,
      symmetry: 90,
      goldenRatio: 88,
      skinQuality: 87,
      jawline: 89,
      cheekbones: 88
    }
  },
  {
    rank: 5,
    nickname: 'GoldenRatio99',
    distanceKm: 14.5,
    score: 88,
    avatar: 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=300&q=80',
    details: {
      potential: 92,
      symmetry: 89,
      goldenRatio: 90,
      skinQuality: 86,
      jawline: 87,
      cheekbones: 88
    }
  },
  {
    rank: 6,
    nickname: 'PhoenixUz',
    distanceKm: 17.2,
    score: 86,
    avatar: 'https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?auto=format&fit=crop&w=300&q=80',
    details: {
      potential: 90,
      symmetry: 87,
      goldenRatio: 86,
      skinQuality: 85,
      jawline: 85,
      cheekbones: 86
    }
  }
];

import { getTranslation } from '../services/translations';

export default function LeaderboardScreen({ currentUser, userScore, currentLang = 'uz' }) {
  const [cityName] = useState('Tashkent');
  const [userNickname] = useState('DragonRider');
  const [leaderboard, setLeaderboard] = useState(INITIAL_MOCK_LEADERBOARD);
  const [selectedUser, setSelectedUser] = useState(null); // Selected user modal details

  const t = (key) => getTranslation(currentLang, key);

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (pos) => {
          const lat = pos.coords.latitude;
          const lng = pos.coords.longitude;
          const hash = encodeGeohash(lat, lng, 6);
          console.log("Geohash initialized:", hash);
        },
        (err) => console.warn("Location permission fallback:", err),
        { timeout: 5000 }
      );
    }

    if (userScore) {
      const myScoreObj = {
        rank: 0,
        nickname: userNickname,
        distanceKm: 0.8,
        score: userScore.overall,
        isCurrent: true,
        avatar: currentUser?.photoURL || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=300&q=80',
        details: {
          potential: userScore.potential,
          symmetry: userScore.symmetry,
          goldenRatio: Math.round((userScore.symmetry + userScore.jawline) / 2),
          skinQuality: userScore.skinQuality,
          jawline: userScore.jawline,
          cheekbones: userScore.cheekbones
        }
      };

      const updated = [...INITIAL_MOCK_LEADERBOARD, myScoreObj]
        .sort((a, b) => b.score - a.score)
        .map((item, idx) => ({ ...item, rank: idx + 1 }));

      setLeaderboard(updated);
    }
  }, [userScore, userNickname, currentUser]);

  const top1 = leaderboard[0];
  const top2 = leaderboard[1];
  const top3 = leaderboard[2];
  const rest = leaderboard.slice(3);

  const getScoreColor = (val) => {
    if (val >= 90) return { text: 'text-[#22F0B6]', bg: 'from-[#22F0B6] to-[#22D3EE]' };
    if (val >= 70) return { text: 'text-[#FFC145]', bg: 'from-[#FFC145] to-[#FF8A3D]' };
    return { text: 'text-[#38BDF8]', bg: 'from-[#818CF8] to-[#38BDF8]' };
  };

  return (
    <div className="space-y-4 animate-fadeIn pb-4 relative">
      {/* Location Badge Header */}
      <div className="flex items-center justify-between">
        <div className="inline-flex items-center space-x-2 px-3.5 py-1.5 rounded-full liquid-glass-gold border border-[#D4AF37]/40 text-xs font-mono text-[#F4C430]">
          <MapPin className="w-3.5 h-3.5 animate-bounce text-[#E63946]" />
          <span>{t('radiusText')} · {cityName}</span>
        </div>
        <div className="flex items-center space-x-1 text-[11px] text-white/50">
          <Shield className="w-3.5 h-3.5 text-[#22F0B6]" />
          <span>{t('privacyNickText')}</span>
        </div>
      </div>

      {/* Top-3 Podium */}
      <div className="grid grid-cols-3 gap-2 items-end pt-4 pb-2">
        {/* 2nd Place */}
        {top2 && (
          <div
            onClick={() => setSelectedUser(top2)}
            className="liquid-glass rounded-card p-3 flex flex-col items-center justify-center space-y-2 border border-slate-300/30 text-center relative cursor-pointer hover:border-slate-300/70 transition-all active:scale-95"
          >
            <div className="w-6 h-6 rounded-full bg-slate-300/30 text-slate-200 text-xs font-extrabold flex items-center justify-center border border-slate-200/50">
              2
            </div>
            <img src={top2.avatar} alt="Rank 2" className="w-12 h-12 rounded-full object-cover border-2 border-slate-300" />
            <div className="w-full">
              <p className="text-[11px] font-bold text-white truncate">{top2.nickname}</p>
              <p className="text-xs font-mono text-[#FFC145] font-extrabold">{top2.score} pts</p>
            </div>
          </div>
        )}

        {/* 1st Place (Gold Crown) */}
        {top1 && (
          <div
            onClick={() => setSelectedUser(top1)}
            className="liquid-glass-gold rounded-card p-3.5 flex flex-col items-center justify-center space-y-2 border border-[#F4C430] text-center relative transform -translate-y-2 shadow-gold-glow cursor-pointer hover:scale-105 transition-all active:scale-95"
          >
            <div className="absolute -top-3 w-7 h-7 rounded-full bg-[#F4C430] text-gray-900 text-xs font-black flex items-center justify-center shadow-md">
              <Crown className="w-4 h-4 text-gray-900" />
            </div>
            <img src={top1.avatar} alt="Rank 1" className="w-14 h-14 rounded-full object-cover border-2 border-[#F4C430] mt-1" />
            <div className="w-full">
              <p className="text-xs font-extrabold text-white truncate">{top1.nickname}</p>
              <p className="text-sm font-mono text-[#F4C430] font-black">{top1.score} pts</p>
            </div>
          </div>
        )}

        {/* 3rd Place */}
        {top3 && (
          <div
            onClick={() => setSelectedUser(top3)}
            className="liquid-glass rounded-card p-3 flex flex-col items-center justify-center space-y-2 border border-amber-700/40 text-center relative cursor-pointer hover:border-amber-600/70 transition-all active:scale-95"
          >
            <div className="w-6 h-6 rounded-full bg-amber-700/40 text-amber-300 text-xs font-extrabold flex items-center justify-center border border-amber-600/50">
              3
            </div>
            <img src={top3.avatar} alt="Rank 3" className="w-12 h-12 rounded-full object-cover border-2 border-amber-600" />
            <div className="w-full">
              <p className="text-[11px] font-bold text-white truncate">{top3.nickname}</p>
              <p className="text-xs font-mono text-[#FFC145] font-extrabold">{top3.score} pts</p>
            </div>
          </div>
        )}
      </div>

      {/* Remaining Leaderboard Rank List */}
      <div className="space-y-2 pt-2">
        <span className="text-[11px] font-mono text-white/50 uppercase tracking-wider block">
          {t('allLeaderboardText')}
        </span>

        {rest.map((item, idx) => (
          <div
            key={idx}
            onClick={() => setSelectedUser(item)}
            className={`liquid-glass rounded-2xl p-3 flex items-center justify-between border transition-all cursor-pointer hover:border-[#E63946]/50 active:scale-98 ${
              item.isCurrent
                ? 'liquid-glass-gold border-[#F4C430] shadow-gold-glow'
                : 'border-white/10 hover:border-white/20'
            }`}
          >
            <div className="flex items-center space-x-3">
              <span className="w-6 text-center font-mono font-bold text-xs text-white/60">
                #{item.rank}
              </span>
              <img src={item.avatar} alt={item.nickname} className="w-9 h-9 rounded-full object-cover border border-white/20" />
              <div>
                <p className="text-xs font-semibold text-white flex items-center space-x-1.5">
                  <span>{item.nickname}</span>
                  {item.isCurrent && (
                    <span className="bg-[#F4C430] text-gray-900 text-[9px] font-extrabold px-1.5 py-0.2 rounded-full">
                      {t('youBadge')}
                    </span>
                  )}
                </p>
                <p className="text-[10px] text-white/50 flex items-center space-x-1">
                  <Navigation className="w-2.5 h-2.5 text-[#22F0B6]" />
                  <span>{item.distanceKm} {t('distanceText')}</span>
                </p>
              </div>
            </div>

            <div className="text-right">
              <span className="text-sm font-mono font-extrabold text-[#22F0B6]">
                {item.score}
              </span>
              <span className="text-[10px] text-white/40 block">ball</span>
            </div>
          </div>
        ))}
      </div>

      {/* Detailed Score Breakdown Modal Window */}
      {selectedUser && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md animate-fadeIn">
          <div className="relative w-full max-w-sm liquid-glass-gold rounded-card p-5 space-y-4 border border-[#F4C430]/60 shadow-2xl">
            {/* Modal Close Button */}
            <button
              onClick={() => setSelectedUser(null)}
              className="absolute top-3 right-3 p-2 rounded-full bg-black/60 text-white hover:bg-black/80 transition-all"
            >
              <X className="w-4 h-4 text-white" />
            </button>

            {/* User Header Info */}
            <div className="flex items-center space-x-3 border-b border-white/15 pb-3">
              <img
                src={selectedUser.avatar}
                alt={selectedUser.nickname}
                className="w-14 h-14 rounded-full object-cover border-2 border-[#F4C430] shadow-md"
              />
              <div>
                <div className="flex items-center space-x-2">
                  <h3 className="text-base font-bold font-heading text-white">{selectedUser.nickname}</h3>
                  <span className="px-2 py-0.5 rounded-full bg-[#F4C430] text-gray-900 text-[10px] font-mono font-extrabold">
                    #{selectedUser.rank}{t('rankText')}
                  </span>
                </div>
                <p className="text-xs text-white/60 font-mono">
                  {selectedUser.distanceKm} {t('distanceText')} · Tashkent
                </p>
                <div className="inline-flex items-center space-x-1 text-[10px] text-[#22F0B6] font-mono mt-0.5">
                  <UserCheck className="w-3 h-3" />
                  <span>{t('overallScoreLabel')}: {selectedUser.score} / 100</span>
                </div>
              </div>
            </div>

            {/* Detailed Metric Scores breakdown per requirement (2-Column Grid) */}
            <div className="space-y-1.5">
              <span className="text-[11px] font-mono text-white/60 uppercase tracking-wider block">
                {t('scoreBreakdownTitle')}
              </span>

              <div className="grid grid-cols-2 gap-2">
                {[
                  { label: t('scoreSymmetry'), val: selectedUser.details?.symmetry || 92 },
                  { label: t('scoreGoldenRatio'), val: selectedUser.details?.goldenRatio || 91 },
                  { label: t('scoreSkin'), val: selectedUser.details?.skinQuality || 88 },
                  { label: t('scoreJawline'), val: selectedUser.details?.jawline || 90 },
                  { label: t('scoreCheekbones'), val: selectedUser.details?.cheekbones || 89 },
                  { label: t('scorePotential'), val: selectedUser.details?.potential || 95 }
                ].map((m, idx) => {
                  const color = getScoreColor(m.val);
                  return (
                    <div key={idx} className="liquid-glass rounded-xl p-2 space-y-1 border border-white/10">
                      <div className="flex items-center justify-between text-[11px]">
                        <span className="text-white/80 font-medium truncate">{m.label}</span>
                        <span className={`font-mono font-extrabold shrink-0 ${color.text}`}>{m.val}</span>
                      </div>
                      <div className="w-full h-1.5 bg-black/40 rounded-full overflow-hidden p-0.5">
                        <div
                          className={`h-full rounded-full bg-gradient-to-r ${color.bg}`}
                          style={{ width: `${m.val}%` }}
                        />
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>

            {/* Modal Footer Action Button */}
            <button
              onClick={() => setSelectedUser(null)}
              className="w-full py-2.5 rounded-btn bg-white/10 hover:bg-white/20 border border-white/20 text-xs font-semibold text-white transition-all"
            >
              {t('closeBtn')}
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
