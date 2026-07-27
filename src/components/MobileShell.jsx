import React from 'react';
import { Sparkles, Trophy, HeartHandshake, User, Camera } from 'lucide-react';
import DragonBackground from './DragonBackground';
import { getTranslation } from '../services/translations';

export default function MobileShell({ children, activeTab, setActiveTab, user, currentLang = 'uz' }) {
  const t = (key) => getTranslation(currentLang, key);

  return (
    <div className="relative min-h-screen w-full flex items-center justify-center z-10 bg-[#0d0403]">
      <div className="relative w-full h-full min-h-screen bg-[#0d0403] overflow-hidden flex flex-col">
        
        {/* Dragon Emblem Background inside Full Screen */}
        <DragonBackground />
        
        {/* Scrollable Main Screen Body */}
        <div className="flex-1 overflow-y-auto px-4 pb-24 pt-4 relative z-20">
          {children}
        </div>

        {/* Liquid Glass Bottom Navigation Bar */}
        <nav className="fixed bottom-0 inset-x-0 h-20 bg-black/80 backdrop-blur-3xl border-t border-white/10 px-4 flex items-center justify-around z-40 pb-2">
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
