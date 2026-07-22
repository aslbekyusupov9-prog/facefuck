import React, { useState, useRef, useEffect } from 'react';
import { Camera, Image as ImageIcon, Sparkles, Copy, Check, AlertCircle, RefreshCw, SwitchCamera, Video, X } from 'lucide-react';

const SAMPLE_DEMO_FACES = [
  {
    name: 'Model 1',
    url: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80'
  },
  {
    name: 'Model 2',
    url: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80'
  },
  {
    name: 'Model 3',
    url: 'https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80'
  }
];

import { getTranslation } from '../services/translations';

export default function PhotoUploadScreen({ onPhotoSelected, currentLang = 'uz' }) {
  const [selectedImage, setSelectedImage] = useState(null);
  const [gender, setGender] = useState('male');
  const [isCopied, setIsCopied] = useState(false);
  const [isCameraActive, setIsCameraActive] = useState(false);
  const [facingMode, setFacingMode] = useState('user'); // 'user' (front) or 'environment' (back)
  const [cameraError, setCameraError] = useState(null);

  const t = (key) => getTranslation(currentLang, key);

  const fileInputRef = useRef(null);
  const videoRef = useRef(null);
  const canvasRef = useRef(null);
  const streamRef = useRef(null);

  const AI_ENHANCE_PROMPT = `Bu rasmni professional darajada tiniqlashtir va rezolyutsiyasini oshir (upscale + deblur + denoise), quyidagi aniq bosqichlarni bajar:
1. RESOLYUTSIYA: rasmni kamida 2x sifat/piksel zichligida qayta tikla (super-resolution), piksellashgan va bulg'ur joylarni to'liq tekislab, chekka va detallarni aniq chiz.
2. XIRALIK: harakat xiraligi va fokus xiraligini to'liq olib tashla — yuzdagi barcha chiziqlar keskin va aniq chegaralarga ega bo'lsin.
3. SHOVQIN: ISO shovqini va kompressiya artefaktlarini to'liq tozala.
4. MIKRO-KONTRAST: teri tekstura detallarini tabiiy holda kuchaytir — lekin tekislab yubormasdan.
5. YORUG'LIK: agar rasm juda qorong'i yoki ortiqcha ekspozitsiyada bo'lsa, tabiiy dinamik diapazonni tiklab, yuz aniq ko'rinadigan darajaga keltir.
QAT'IY CHEKLOV: yuz geometriyasi, proporsiyalari, yosh belgilari, teri rangi, soqol, soch shakli va HECH QANDAY xususiyatni o'zgartirma yoki "go'zallashtirma". Bu faqat texnik tiklash, badiiy qayta chizish emas — natija xuddi o'sha odam, o'sha kadr, faqat texnik jihatdan ancha yuqori sifatda bo'lishi kerak. Agar imkoning bo'lsa, natijani original o'lchamdan kamida 2x kattaroq va aniqroq rezolyutsiyada qaytar.`;

  // Clean up camera stream on unmount
  useEffect(() => {
    return () => {
      stopCamera();
    };
  }, []);

  // Start Browser Live Camera Stream using MediaDevices API
  const startCamera = async (mode = facingMode) => {
    setCameraError(null);
    stopCamera();

    if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
      setCameraError("Brauzeringiz real-vaqtli kameraga ruxsat bermaydi. Iltimos HTTPS yoki fayl yuklashdan foydalaning.");
      return;
    }

    try {
      const constraints = {
        video: {
          facingMode: mode,
          width: { ideal: 1280 },
          height: { ideal: 720 }
        },
        audio: false
      };

      const stream = await navigator.mediaDevices.getUserMedia(constraints);
      streamRef.current = stream;

      if (videoRef.current) {
        videoRef.current.srcObject = stream;
      }
      setIsCameraActive(true);
    } catch (err) {
      console.error("Camera access error:", err);
      setCameraError("Kameraga ulanishda xatolik yuz berdi. Ruxsat berilganini tekshiring.");
      setIsCameraActive(false);
    }
  };

  // Stop Browser Live Camera Stream
  const stopCamera = () => {
    if (streamRef.current) {
      streamRef.current.getTracks().forEach((track) => track.stop());
      streamRef.current = null;
    }
    setIsCameraActive(false);
  };

  // Switch Front / Back Camera
  const toggleCameraFacing = () => {
    const newMode = facingMode === 'user' ? 'environment' : 'user';
    setFacingMode(newMode);
    startCamera(newMode);
  };

  // Capture Snapshot Frame from Video Stream
  const capturePhoto = () => {
    if (!videoRef.current) return;
    const video = videoRef.current;
    const canvas = canvasRef.current || document.createElement('canvas');

    canvas.width = video.videoWidth || 640;
    canvas.height = video.videoHeight || 480;

    const ctx = canvas.getContext('2d');
    
    // Flip horizontally if front camera for natural mirror effect
    if (facingMode === 'user') {
      ctx.translate(canvas.width, 0);
      ctx.scale(-1, 1);
    }

    ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
    const capturedDataUrl = canvas.toDataURL('image/jpeg', 0.95);

    setSelectedImage(capturedDataUrl);
    stopCamera();
  };

  // File Upload Handler
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        const imgUrl = event.target.result;
        setSelectedImage(imgUrl);
        stopCamera();
      };
      reader.readAsDataURL(file);
    }
  };

  const handleCopyPrompt = () => {
    navigator.clipboard.writeText(AI_ENHANCE_PROMPT);
    setIsCopied(true);
    setTimeout(() => setIsCopied(false), 3000);
  };

  const handleStartAnalysis = () => {
    if (!selectedImage) return;
    onPhotoSelected(selectedImage, gender);
  };

  return (
    <div className="space-y-5 animate-fadeIn pb-4">
      {/* Header Title */}
      <div className="text-center space-y-1">
        <h2 className="text-xl font-bold font-heading text-white">{t('uploadTitle')}</h2>
        <p className="text-xs text-white/60">{t('uploadSubtitle')}</p>
      </div>

      {/* Hidden Snapshot Canvas */}
      <canvas ref={canvasRef} className="hidden" />

      {/* Main Preview / Live Camera Frame */}
      <div className="relative w-full h-80 rounded-card liquid-glass overflow-hidden flex flex-col items-center justify-center p-2 border border-white/20 shadow-liquid-glow group">
        
        {/* State 1: Live MediaDevices Camera Stream View */}
        {isCameraActive ? (
          <div className="relative w-full h-full rounded-2xl overflow-hidden bg-black flex items-center justify-center">
            <video
              ref={videoRef}
              autoPlay
              playsInline
              muted
              className={`w-full h-full object-cover ${facingMode === 'user' ? 'scale-x-[-1]' : ''}`}
            />

            {/* Glowing Face Oval Target Overlay */}
            <div className="absolute inset-0 pointer-events-none flex items-center justify-center">
              <div className="w-48 h-64 border-2 border-dashed border-[#22F0B6] rounded-[50%] shadow-[0_0_20px_rgba(34,240,184,0.4)] animate-pulse flex items-center justify-center">
                <span className="text-[10px] font-mono text-[#22F0B6] bg-black/60 px-2 py-0.5 rounded-full border border-[#22F0B6]/40">
                  {t('centerFaceMsg')}
                </span>
              </div>
            </div>

            {/* Live Camera Controls Floating Overlay */}
            <div className="absolute top-3 right-3 flex items-center space-x-2 z-20">
              <button
                onClick={toggleCameraFacing}
                title="Camera Flip"
                className="p-2.5 rounded-full bg-black/60 backdrop-blur-md text-white border border-white/20 hover:bg-black/80 transition-all active:scale-95"
              >
                <SwitchCamera className="w-4 h-4 text-[#F4C430]" />
              </button>
              <button
                onClick={stopCamera}
                title="Close Camera"
                className="p-2.5 rounded-full bg-black/60 backdrop-blur-md text-white border border-white/20 hover:bg-black/80 transition-all active:scale-95"
              >
                <X className="w-4 h-4 text-red-400" />
              </button>
            </div>

            {/* Bottom Capture Trigger Button */}
            <div className="absolute bottom-4 inset-x-0 flex items-center justify-center z-20">
              <button
                onClick={capturePhoto}
                className="px-6 py-3 rounded-full liquid-glass-accent text-white font-bold font-heading text-xs flex items-center space-x-2 border border-[#E63946] shadow-red-pulse active:scale-95 transition-all"
              >
                <div className="w-3 h-3 rounded-full bg-[#E63946] animate-ping" />
                <span>{t('capturePhotoBtn')}</span>
              </button>
            </div>
          </div>

        /* State 2: Selected Photo Image Preview */
        ) : selectedImage ? (
          <>
            <img
              src={selectedImage}
              alt="Yuz rasmi"
              className="w-full h-full object-cover rounded-2xl"
            />
            {/* Reset / Re-take Photo Button */}
            <button
              onClick={() => {
                setSelectedImage(null);
                startCamera();
              }}
              className="absolute top-3 right-3 p-2 rounded-full bg-black/60 backdrop-blur-md text-white border border-white/20 hover:bg-black/80 transition-all"
            >
              <RefreshCw className="w-4 h-4" />
            </button>
          </>

        /* State 3: Idle Choice View (Activate Camera or Choose File) */
        ) : (
          <div className="flex flex-col items-center justify-center space-y-4 text-center px-4">
            <div className="w-20 h-20 rounded-3xl bg-[#E63946]/10 border border-[#E63946]/30 flex items-center justify-center text-[#E63946]">
              <Camera className="w-10 h-10 animate-pulse" />
            </div>

            <div className="space-y-1">
              <p className="text-sm font-semibold text-white/90">{t('liveCameraBtn')} / {t('galleryBtn')}</p>
              <p className="text-[11px] text-white/50">{t('uploadSubtitle')}</p>
            </div>

            {cameraError && (
              <div className="px-3 py-1.5 rounded-xl bg-red-950/60 border border-red-500/40 text-[11px] text-red-300 font-medium">
                {cameraError}
              </div>
            )}

            {/* Main Action Buttons */}
            <div className="flex items-center space-x-3 pt-2">
              <button
                onClick={() => startCamera()}
                className="px-4 py-2.5 rounded-btn liquid-glass-accent text-xs font-semibold text-white flex items-center space-x-2 shadow-red-pulse transition-all active:scale-95"
              >
                <Video className="w-4 h-4 text-[#22F0B6]" />
                <span>{t('liveCameraBtn')}</span>
              </button>

              <button
                onClick={() => fileInputRef.current?.click()}
                className="px-4 py-2.5 rounded-btn bg-white/10 hover:bg-white/20 border border-white/20 text-xs font-semibold text-white flex items-center space-x-2 transition-all active:scale-95"
              >
                <ImageIcon className="w-4 h-4 text-[#F4C430]" />
                <span>{t('galleryBtn')}</span>
              </button>
            </div>

            <input
              type="file"
              ref={fileInputRef}
              onChange={handleFileChange}
              accept="image/*"
              className="hidden"
            />
          </div>
        )}
      </div>

      {/* Gender Selection Pill */}
      <div className="liquid-glass rounded-2xl p-2 flex items-center justify-between">
        <span className="text-xs text-white/70 font-medium pl-2">{t('genderLabel')}</span>
        <div className="flex space-x-1">
          <button
            onClick={() => setGender('male')}
            className={`px-4 py-1.5 rounded-xl text-xs font-semibold transition-all ${
              gender === 'male' ? 'bg-[#E63946] text-white shadow-md' : 'text-white/60 hover:text-white'
            }`}
          >
            {t('maleGender')}
          </button>
          <button
            onClick={() => setGender('female')}
            className={`px-4 py-1.5 rounded-xl text-xs font-semibold transition-all ${
              gender === 'female' ? 'bg-[#E63946] text-white shadow-md' : 'text-white/60 hover:text-white'
            }`}
          >
            {t('femaleGender')}
          </button>
        </div>
      </div>

      {/* AI Image Upscale / Deblur Copy Block */}
      <div className="liquid-glass rounded-2xl p-4 space-y-3 border-amber-500/20">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-2 text-amber-400">
            <AlertCircle className="w-4 h-4 shrink-0" />
            <span className="text-xs font-bold font-heading">{t('aiPromptTitle')}</span>
          </div>
          <button
            onClick={handleCopyPrompt}
            className={`px-3 py-1.5 rounded-xl text-xs font-bold font-mono transition-all flex items-center space-x-1.5 ${
              isCopied
                ? 'bg-[#22F0B6] text-gray-900 shadow-md'
                : 'bg-white/10 text-white hover:bg-white/20 border border-white/20'
            }`}
          >
            {isCopied ? (
              <>
                <Check className="w-3.5 h-3.5" />
                <span>{t('copiedText')}</span>
              </>
            ) : (
              <>
                <Copy className="w-3.5 h-3.5" />
                <span>{t('copyPromptBtn')}</span>
              </>
            )}
          </button>
        </div>
      </div>

      {/* Primary Start Analysis Button */}
      <button
        disabled={!selectedImage}
        onClick={handleStartAnalysis}
        className="w-full py-4 rounded-btn liquid-glass-accent text-white font-bold font-heading text-base tracking-wide flex items-center justify-center space-x-2 disabled:opacity-50 disabled:cursor-not-allowed shadow-red-pulse transition-all"
      >
        <Sparkles className="w-5 h-5 text-[#F4C430]" />
        <span>{t('startAnalysisBtn')}</span>
      </button>
    </div>
  );
}
