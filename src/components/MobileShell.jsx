import React from 'react';
import { Sparkles, Trophy, HeartHandshake, User, Camera } from 'lucide-react';
import DragonBackground from './DragonBackground';
import { getTranslation } from '../services/translations';

export default function MobileShell({ children, activeTab, setActiveTab, user, currentLang = 'uz' }) {
  const t = (key) => getTranslation(currentLang, key);

  return (
    <div className="relative min-h-screen w-full flex items-center justify-center p-0 sm:p-4 md:p-6 z-10">
      {/* Outer Phone Mockup Frame for Desktop / Container for Mobile */}
      <div className="relative w-full max-w-md h-[100vh] sm:h-[880px] bg-[#0d0403] sm:rounded-[54px] sm:border-[8px] sm:border-white/15 shadow-2xl overflow-hidden flex flex-col backdrop-blur-2xl">
        
        {/* Dragon Emblem Background inside Phone Frame */}
        <DragonBackground />
        
        {/* Top Status Bar Mock */}
        <div className="w-full px-6 pt-3 pb-2 flex items-center justify-between text-xs font-mono text-white/60 z-30 select-none bg-gradient-to-b from-[#0d0403] to-transparent">
          <span>9:41</span>
          {/* Dynamic Island / Camera Notch */}
          <div className="w-24 h-4 bg-black/80 rounded-full border border-white/10 flex items-center justify-center">
            <div className="w-2 h-2 rounded-full bg-[#E63946] animate-pulse" />
          </div>
          <div className="flex items-center space-x-1.5">
            <span className="text-[10px] bg-[#E63946]/30 text-[#E63946] px-1.5 py-0.5 rounded border border-[#E63946]/40 font-semibold">AI ON</span>
            <div className="w-4 h-2.5 border border-white/60 rounded-sm flex items-center p-0.5">
              <div className="w-full h-full bg-white/80 rounded-2xs" />
            </div>
          </div>
        </div>

        {/* Scrollable Main Screen Body */}
        <div className="flex-1 overflow-y-auto px-4 pb-24 pt-2 relative z-20">
          {children}
        </div>

        {/* Liquid Glass Bottom Navigation Bar */}
        <nav className="absolute bottom-0 inset-x-0 h-20 bg-black/40 backdrop-blur-2xl border-t border-white/10 px-4 flex items-center justify-around z-40 sm:rounded-b-[46px]">
          <button
            onClick={() => setActiveTab('upload')}
            className={`flex flex-col items-center justify-center space-y-1 transition-all duration-300 ${
              activeTab === 'upload' || activeTab === 'scanning' || activeTab === 'result'
                ? 'text-[#E63946] scale-110'
                : 'text-white/50 hover:text-white'
            }`}
          >
            <div className={`p-2 rounded-2xl ${activeTab === 'upload' ? 'bg-[#E63946]/20 border border-[#E63946]/40' : ''}`}>
              <Camera className="w-5 h-5" />
            </div>
            <span className="text-[10px] font-medium tracking-wide">{t('navAnalysis')}</span>
          </button>

          <button
            onClick={() => setActiveTab('leaderboard')}
            className={`flex flex-col items-center justify-center space-y-1 transition-all duration-300 ${
              activeTab === 'leaderboard'
                ? 'text-[#F4C430] scale-110'
                : 'text-white/50 hover:text-white'
            }`}
          >
            <div className={`p-2 rounded-2xl ${activeTab === 'leaderboard' ? 'bg-[#D4AF37]/20 border border-[#D4AF37]/40' : ''}`}>
              <Trophy className="w-5 h-5" />
            </div>
            <span className="text-[10px] font-medium tracking-wide">{t('navLeaderboard')}</span>
          </button>

          <button
            onClick={() => setActiveTab('tips')}
            className={`flex flex-col items-center justify-center space-y-1 transition-all duration-300 ${
              activeTab === 'tips'
                ? 'text-[#22F0B6] scale-110'
                : 'text-white/50 hover:text-white'
            }`}
          >
            <div className={`p-2 rounded-2xl ${activeTab === 'tips' ? 'bg-[#22F0B6]/20 border border-[#22F0B6]/40' : ''}`}>
              <HeartHandshake className="w-5 h-5" />
            </div>
            <span className="text-[10px] font-medium tracking-wide">{t('navTips')}</span>
          </button>

          <button
            onClick={() => setActiveTab('profile')}
            className={`flex flex-col items-center justify-center space-y-1 transition-all duration-300 ${
              activeTab === 'profile'
                ? 'text-white scale-110'
                : 'text-white/50 hover:text-white'
            }`}
          >
            <div className={`p-2 rounded-2xl ${activeTab === 'profile' ? 'bg-white/20 border border-white/30' : ''}`}>
              <User className="w-5 h-5" />
            </div>
            <span className="text-[10px] font-medium tracking-wide">{t('navProfile')}</span>
          </button>
        </nav>
      </div>
    </div>
  );
}
