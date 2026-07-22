import React, { useEffect, useState } from 'react';
import { analyzeFaceImage } from '../services/faceLandmarks';

const STATUS_STEPS = [
  'Nuqtalar aniqlanmoqda...',
  'Geometriya o\'lchanmoqda...',
  'Teri tahlil qilinmoqda...',
  'Ballar hisoblanmoqda...'
];

export default function ScanningScreen({ imageSrc, gender, onAnalysisComplete }) {
  const [currentStepIdx, setCurrentStepIdx] = useState(0);

  useEffect(() => {
    // Step message interval animation
    const interval = setInterval(() => {
      setCurrentStepIdx((prev) => (prev < STATUS_STEPS.length - 1 ? prev + 1 : prev));
    }, 650);

    // Perform analysis
    const imgObj = new Image();
    imgObj.crossOrigin = "Anonymous";
    imgObj.src = imageSrc;
    imgObj.onload = async () => {
      const results = await analyzeFaceImage(imgObj, gender);
      clearInterval(interval);
      onAnalysisComplete(results);
    };

    return () => clearInterval(interval);
  }, [imageSrc, gender]);

  return (
    <div className="h-full flex flex-col items-center justify-center space-y-6 py-8 animate-fadeIn text-center">
      {/* Title */}
      <div className="space-y-1">
        <h2 className="text-xl font-bold font-heading text-white">Biometrik Tahlil Ketmoqda</h2>
        <p className="text-xs text-[#E63946] font-mono tracking-wide uppercase">On-Device MediaPipe Engine</p>
      </div>

      {/* High-Tech Camera Scanning Window */}
      <div className="relative w-64 h-80 rounded-card overflow-hidden liquid-glass border border-[#E63946]/40 shadow-red-pulse p-1">
        {/* User Image */}
        <img
          src={imageSrc}
          alt="Biometric Scan"
          className="w-full h-full object-cover rounded-[22px] filter brightness-90 contrast-105"
        />

        {/* High-Tech Glowing Laser Scan Line */}
        <div className="absolute inset-x-0 h-1 bg-gradient-to-r from-transparent via-[#E63946] to-transparent shadow-[0_0_20px_#E63946] animate-radar z-20" />

        {/* Glowing Central Symmetry Axis Line */}
        <div className="absolute inset-y-0 left-1/2 w-[1px] bg-gradient-to-b from-transparent via-[#22D3EE]/60 to-transparent border-r border-dashed border-[#22D3EE]/50 z-10" />

        {/* Cyberpunk Biometric HUD Alignment Corners */}
        <div className="absolute inset-0 pointer-events-none z-10 p-3 flex flex-col justify-between">
          <div className="flex justify-between">
            <div className="w-6 h-6 border-t-2 border-l-2 border-[#F4C430] rounded-tl-lg" />
            <div className="w-6 h-6 border-t-2 border-r-2 border-[#F4C430] rounded-tr-lg" />
          </div>

          {/* Dynamic Grid Matrix Nodes */}
          <div className="w-full flex items-center justify-around opacity-40">
            <div className="w-1.5 h-1.5 rounded-full bg-[#22F0B6] animate-ping" />
            <div className="w-1.5 h-1.5 rounded-full bg-[#E63946] animate-ping" />
            <div className="w-1.5 h-1.5 rounded-full bg-[#F4C430] animate-ping" />
          </div>

          <div className="flex justify-between">
            <div className="w-6 h-6 border-b-2 border-l-2 border-[#F4C430] rounded-bl-lg" />
            <div className="w-6 h-6 border-b-2 border-r-2 border-[#F4C430] rounded-br-lg" />
          </div>
        </div>
      </div>

      {/* Dynamic Status Text & Loader */}
      <div className="liquid-glass rounded-2xl px-6 py-3 border border-white/15 flex items-center space-x-3">
        <div className="w-4 h-4 border-2 border-[#E63946] border-t-transparent rounded-full animate-spin shrink-0" />
        <span className="text-sm font-mono text-white font-medium">
          {STATUS_STEPS[currentStepIdx]}
        </span>
      </div>
    </div>
  );
}
