import React from 'react';
import { HeartHandshake, Sparkles, Droplets, Moon, Scissors, Smile } from 'lucide-react';
import { getTranslation } from '../services/translations';

const RECOMMENDATIONS = [
  {
    icon: Droplets,
    color: 'text-[#22D3EE]',
    title: 'Namlantirish va Teri Parvarishi',
    description: 'Har kuni 2 litr toza suv ichish va kechki namlantiruvchi krem terining tabiiy yorqinligini 20-30% ga oshiradi.'
  },
  {
    icon: Moon,
    color: 'text-[#818CF8]',
    title: 'Sifatli Uyqu va Simmetriya',
    description: 'Sakkiz soatlik uyqu yuzdagi shishlarni kamaytiradi. Chalqancha yotib uyqu rejasi yuz simmetriyasini tabiiy saqlaydi.'
  },
  {
    icon: Scissors,
    color: 'text-[#F4C430]',
    title: 'Yuz Shakliga Mos Soch Uslubi',
    description: 'Jag\' va yonoq suyaklarini chiroyli ramkaga solish uchun sochni yon tomondan qisqartirib, tepasiga hajm berish tavsiya etiladi.'
  },
  {
    icon: Smile,
    color: 'text-[#22F0B6]',
    title: 'Tabiiy Doyimiy Tabassum va Qomat',
    description: 'To\'g\'ri qomat (posture) va jag\' mushaklarini bo\'sh tutish yuz konturlarini keskin va jozibador ko\'rsatadi.'
  }
];

export default function TipsScreen({ scores, currentLang = 'uz' }) {
  const t = (key) => getTranslation(currentLang, key);

  return (
    <div className="space-y-4 animate-fadeIn pb-4">
      {/* Header */}
      <div className="text-center space-y-1">
        <div className="inline-flex items-center space-x-1.5 px-3 py-1 rounded-full bg-[#22F0B6]/20 border border-[#22F0B6]/30 text-xs font-semibold text-[#22F0B6]">
          <HeartHandshake className="w-3.5 h-3.5" />
          <span>{t('navTips')}</span>
        </div>
        <h2 className="text-xl font-bold font-heading text-white">{t('tipsHeaderTitle')}</h2>
        <p className="text-xs text-white/60">{t('tipsHeaderSubtitle')}</p>
      </div>

      {/* Encouraging Affirmation Banner */}
      <div className="liquid-glass-accent rounded-card p-4 space-y-2 border border-[#E63946]/40 shadow-red-pulse">
        <div className="flex items-center space-x-2 text-[#F4C430]">
          <Sparkles className="w-4 h-4 animate-spin-slow" />
          <h3 className="text-sm font-bold font-heading">{t('tipsBannerTitle')}</h3>
        </div>
        <p className="text-xs text-white/80 leading-relaxed">
          {t('tipsBannerDesc')}
        </p>
      </div>

      {/* Recommendation Cards */}
      <div className="space-y-3">
        {RECOMMENDATIONS.map((rec, idx) => {
          const IconComp = rec.icon;
          return (
            <div key={idx} className="liquid-glass rounded-2xl p-4 flex items-start space-x-3.5 border border-white/10 hover:border-white/20 transition-all">
              <div className="w-10 h-10 rounded-2xl bg-white/10 flex items-center justify-center shrink-0 border border-white/15">
                <IconComp className={`w-5 h-5 ${rec.color}`} />
              </div>
              <div className="space-y-1">
                <h4 className="text-sm font-semibold font-heading text-white">{rec.title}</h4>
                <p className="text-xs text-white/70 leading-relaxed font-sans">{rec.description}</p>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
