import React, { useState } from 'react';
import { Sparkles, ShieldCheck, MapPin, ChevronRight, Info } from 'lucide-react';

const slides = [
  {
    icon: Sparkles,
    color: 'from-[#E63946] to-[#7F1D1D]',
    title: 'Sun\'iy Intellekt Yuz Tahlili',
    subtitle: 'On-device MediaPipe texnologiyasi orqali yuz simmetriyasi, jag\' chizig\'i va teri sifatini lahzada aniqlang.'
  },
  {
    icon: MapPin,
    color: 'from-[#D4AF37] to-[#996515]',
    title: '20 km Radiusdagi Reyting',
    subtitle: 'Atrofingizdagi foydalanuvchilar orasida o\'z o\'rningizni ko\'ring. Maxfiylik uchun faqat taxallusingiz ko\'rsatiladi.'
  },
  {
    icon: ShieldCheck,
    color: 'from-[#22F0B6] to-[#22D3EE]',
    title: 'Ijobiy va Do\'stona Yondashuv',
    subtitle: 'Ilova hech qachon tanqid qilmaydi. Har bir ball qo\'llab-quvvatlovchi va foydali parvarish maslahatlari bilan beriladi.'
  }
];

export default function OnboardingScreen({ onComplete }) {
  const [currentSlide, setCurrentSlide] = useState(0);

  const nextSlide = () => {
    if (currentSlide < slides.length - 1) {
      setCurrentSlide(currentSlide + 1);
    } else {
      onComplete();
    }
  };

  const IconComp = slides[currentSlide].icon;

  return (
    <div className="h-full flex flex-col justify-between py-6 px-2 text-center animate-fadeIn">
      {/* Top Header Branding */}
      <div className="flex flex-col items-center space-y-2">
        <div className="px-3 py-1 rounded-full bg-[#E63946]/20 border border-[#E63946]/40 flex items-center space-x-1.5 text-xs text-[#E63946] font-mono">
          <Sparkles className="w-3.5 h-3.5" />
          <span>AI FACE RATING</span>
        </div>
        <h1 className="text-2xl font-bold font-heading tracking-tight text-white">
          Qizil-Qora Ajdar Edition
        </h1>
      </div>

      {/* Main Slide Card */}
      <div className="liquid-glass rounded-card p-6 my-auto flex flex-col items-center space-y-6 transform transition-all duration-500">
        <div className={`w-20 h-20 rounded-3xl bg-gradient-to-tr ${slides[currentSlide].color} p-0.5 shadow-liquid-glow flex items-center justify-center`}>
          <div className="w-full h-full bg-[#0d0403]/80 rounded-[22px] flex items-center justify-center backdrop-blur-md">
            <IconComp className="w-10 h-10 text-white" />
          </div>
        </div>

        <div className="space-y-3">
          <h2 className="text-xl font-bold font-heading text-white">
            {slides[currentSlide].title}
          </h2>
          <p className="text-sm text-white/70 leading-relaxed font-sans max-w-xs">
            {slides[currentSlide].subtitle}
          </p>
        </div>

        {/* Slide Indicators */}
        <div className="flex items-center space-x-2 pt-2">
          {slides.map((_, idx) => (
            <div
              key={idx}
              className={`h-1.5 rounded-full transition-all duration-300 ${
                idx === currentSlide ? 'w-8 bg-[#E63946]' : 'w-2 bg-white/20'
              }`}
            />
          ))}
        </div>
      </div>

      {/* Gentle Disclaimer Notice */}
      <div className="space-y-4">
        <div className="liquid-glass rounded-2xl p-3 text-left flex items-start space-x-2.5 border-white/10">
          <Info className="w-4 h-4 text-[#D4AF37] shrink-0 mt-0.5" />
          <p className="text-[11px] text-white/60 leading-tight">
            <span className="font-semibold text-white/80">Yumshoq eslatma:</span> Bu ilova faqat qiziqarli va ma'lumot uchun mo'ljallangan — ballarni haddan tashqari jiddiy qabul qilmang. Har bir yuz takrorlanmas va go'zaldir!
          </p>
        </div>

        {/* Primary Action Button */}
        <button
          onClick={nextSlide}
          className="w-full py-4 rounded-btn liquid-glass-accent text-white font-bold font-heading text-base tracking-wide flex items-center justify-center space-x-2 group hover:opacity-95 transition-all shadow-red-pulse"
        >
          <span>{currentSlide === slides.length - 1 ? 'Boshlash' : 'Keyingisi'}</span>
          <ChevronRight className="w-5 h-5 group-hover:translate-x-1 transition-transform" />
        </button>
      </div>
    </div>
  );
}
