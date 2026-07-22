import React from 'react';

export default function DragonBackground() {
  return (
    <div className="absolute inset-0 pointer-events-none z-0 overflow-hidden select-none flex items-center justify-center">
      {/* Ambient Dark Red & Gold Glows */}
      <div className="absolute top-1/4 left-1/2 -translate-x-1/2 w-80 h-80 bg-[#E63946]/35 rounded-full blur-3xl" />
      <div className="absolute bottom-20 right-0 w-72 h-72 bg-[#7F1D1D]/45 rounded-full blur-3xl" />

      {/* Actual Cropped Transparent User Dragon Image File */}
      <div className="relative w-full h-full flex items-center justify-center p-2">
        <img
          src="/dragon_image.png"
          alt="Dragon Background"
          className="w-full max-w-[360px] h-auto object-contain opacity-[0.75] filter drop-shadow-[0_0_30px_rgba(230,57,70,0.6)] animate-dragon-float"
        />
      </div>
    </div>
  );
}
