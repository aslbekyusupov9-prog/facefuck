import React, { useState, useRef } from 'react';
import { User, Shield, LogOut, Check, Settings, Edit3, Globe, Camera, X, ChevronLeft, ChevronRight, Sparkles } from 'lucide-react';
import { LANGUAGES, getTranslation } from '../services/translations';

const PRESET_AVATARS = [
  'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80',
  'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80',
  'https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80',
  'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&w=300&q=80'
];

export default function ProfileScreen({ user, onUpdateUser, onLogout, currentLang = 'uz', onChangeLang }) {
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  const [activeSettingsView, setActiveSettingsView] = useState('menu'); // 'menu', 'edit_profile', 'language'
  
  // Profile edit fields
  const [displayName, setDisplayName] = useState(user?.displayName || 'Sharipov D.');
  const [nickname, setNickname] = useState(user?.nickname || localStorage.getItem('user_nickname') || 'DragonRider');
  const [avatarUrl, setAvatarUrl] = useState(user?.photoURL || PRESET_AVATARS[0]);
  const [savedSuccess, setSavedSuccess] = useState(false);
  
  const fileInputRef = useRef(null);
  const t = (key) => getTranslation(currentLang, key);

  // Handle Custom Avatar File Upload
  const handleAvatarFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (evt) => {
        setAvatarUrl(evt.target.result);
      };
      reader.readAsDataURL(file);
    }
  };

  // Save Profile Changes
  const handleSaveProfile = (e) => {
    e.preventDefault();
    const updatedUser = {
      ...user,
      displayName: displayName,
      nickname: nickname,
      photoURL: avatarUrl
    };

    localStorage.setItem('ai_face_user', JSON.stringify(updatedUser));
    localStorage.setItem('user_nickname', nickname);
    
    if (onUpdateUser) {
      onUpdateUser(updatedUser);
    }

    setActiveSettingsView('menu');
    setIsSettingsOpen(false);
    setSavedSuccess(true);
    setTimeout(() => setSavedSuccess(false), 2500);
  };

  return (
    <div className="space-y-4 animate-fadeIn pb-4 relative">
      {/* Top Header with Title and Top-Right Settings Gear Icon Only */}
      <div className="flex items-center justify-between pt-1 px-1">
        <div>
          <h2 className="text-xl font-bold font-heading text-white">{t('profileTitle')}</h2>
          <p className="text-xs text-white/60">{t('profileSubtitle')}</p>
        </div>

        {/* Top-Right Settings Gear Icon Button */}
        <button
          onClick={() => {
            setActiveSettingsView('menu');
            setIsSettingsOpen(true);
          }}
          title={t('settingsTitle')}
          className="p-3 rounded-2xl liquid-glass border border-white/20 text-white hover:text-[#F4C430] hover:border-[#F4C430]/50 transition-all shadow-md active:scale-95 flex items-center justify-center"
        >
          <Settings className="w-5 h-5 animate-spin-slow text-[#F4C430]" />
        </button>
      </div>

      {savedSuccess && (
        <div className="p-2.5 rounded-xl bg-[#22F0B6]/20 border border-[#22F0B6]/40 text-xs font-semibold text-[#22F0B6] flex items-center justify-center space-x-1.5 animate-bounce">
          <Check className="w-4 h-4" />
          <span>{t('savedMsg')}</span>
        </div>
      )}

      {/* Main Profile Card Display */}
      <div className="liquid-glass rounded-card p-5 space-y-4 border border-white/20 relative">
        <div className="flex items-center space-x-4">
          <img
            src={avatarUrl}
            alt="Avatar"
            className="w-16 h-16 rounded-full object-cover border-2 border-[#E63946] shadow-md shrink-0"
          />
          <div className="space-y-1 text-left overflow-hidden">
            <h3 className="text-base font-extrabold font-heading text-white truncate">
              {displayName}
            </h3>
            <p className="text-xs font-mono text-[#F4C430] font-semibold truncate">@{nickname}</p>
            <div className="inline-flex items-center space-x-1 text-[10px] text-[#22F0B6] font-mono bg-[#22F0B6]/10 px-2 py-0.5 rounded-full border border-[#22F0B6]/30">
              <Shield className="w-3 h-3" />
              <span>{t('verifiedUser')}</span>
            </div>
          </div>
        </div>

        {/* Biometric Account Stats Highlights */}
        <div className="grid grid-cols-3 gap-2 pt-2 border-t border-white/10 text-center">
          <div className="liquid-glass rounded-xl p-2">
            <span className="text-[10px] text-white/50 block font-mono uppercase">{t('statusLabel')}</span>
            <span className="text-xs font-bold text-[#22F0B6]">{t('activeStatus')}</span>
          </div>
          <div className="liquid-glass rounded-xl p-2">
            <span className="text-[10px] text-white/50 block font-mono uppercase">{t('rankLabel')}</span>
            <span className="text-xs font-bold text-[#F4C430]">#4 {t('rankText')}</span>
          </div>
          <div className="liquid-glass rounded-xl p-2">
            <span className="text-[10px] text-white/50 block font-mono uppercase">{t('appLangLabel')}</span>
            <span className="text-xs font-bold text-[#38BDF8] uppercase">{currentLang}</span>
          </div>
        </div>
      </div>

      {/* Logout Button */}
      <div className="pt-4">
        <button
          onClick={onLogout}
          className="w-full py-3.5 rounded-btn bg-red-950/40 hover:bg-red-900/60 border border-red-500/30 text-red-300 font-semibold font-sans text-xs flex items-center justify-center space-x-2 transition-all active:scale-95"
        >
          <LogOut className="w-4 h-4" />
          <span>{t('logoutBtn')}</span>
        </button>
      </div>

      {/* Settings Modal Window with Sub-Sections */}
      {isSettingsOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-md animate-fadeIn">
          <div className="relative w-full max-w-sm liquid-glass rounded-card p-5 space-y-4 border border-white/20 shadow-2xl text-left">
            {/* Modal Navigation Header */}
            <div className="flex items-center justify-between border-b border-white/15 pb-3">
              <div className="flex items-center space-x-2">
                {activeSettingsView !== 'menu' && (
                  <button
                    onClick={() => setActiveSettingsView('menu')}
                    className="p-1 rounded-full bg-white/10 text-white hover:bg-white/20"
                  >
                    <ChevronLeft className="w-4 h-4" />
                  </button>
                )}
                <h3 className="text-base font-bold font-heading text-white flex items-center space-x-2">
                  <Settings className="w-4 h-4 text-[#F4C430]" />
                  <span>
                    {activeSettingsView === 'menu' && t('settingsTitle')}
                    {activeSettingsView === 'edit_profile' && t('editModalTitle')}
                    {activeSettingsView === 'language' && t('languageOption')}
                  </span>
                </h3>
              </div>
              <button
                onClick={() => setIsSettingsOpen(false)}
                className="p-1.5 rounded-full bg-black/60 text-white/70 hover:text-white"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            {/* Sub-View 1: Main Settings Menu List */}
            {activeSettingsView === 'menu' && (
              <div className="space-y-3 py-1">
                {/* Section 1: Edit Profile Option */}
                <button
                  onClick={() => setActiveSettingsView('edit_profile')}
                  className="w-full p-3.5 rounded-2xl liquid-glass border border-white/15 hover:border-[#E63946] flex items-center justify-between text-white transition-all group active:scale-98"
                >
                  <div className="flex items-center space-x-3">
                    <div className="p-2 rounded-xl bg-[#E63946]/20 text-[#E63946] border border-[#E63946]/30">
                      <Edit3 className="w-4 h-4" />
                    </div>
                    <div>
                      <p className="text-xs font-bold text-white group-hover:text-[#E63946] transition-colors">
                        {t('editProfileOption')}
                      </p>
                      <p className="text-[10px] text-white/50">{t('editProfileDesc')}</p>
                    </div>
                  </div>
                  <ChevronRight className="w-4 h-4 text-white/40 group-hover:text-white" />
                </button>

                {/* Section 2: App Language Option */}
                <button
                  onClick={() => setActiveSettingsView('language')}
                  className="w-full p-3.5 rounded-2xl liquid-glass border border-white/15 hover:border-[#38BDF8] flex items-center justify-between text-white transition-all group active:scale-98"
                >
                  <div className="flex items-center space-x-3">
                    <div className="p-2 rounded-xl bg-[#38BDF8]/20 text-[#38BDF8] border border-[#38BDF8]/30">
                      <Globe className="w-4 h-4" />
                    </div>
                    <div>
                      <p className="text-xs font-bold text-white group-hover:text-[#38BDF8] transition-colors">
                        {t('languageOption')}
                      </p>
                      <p className="text-[10px] text-white/50">{t('languageOptionDesc')}</p>
                    </div>
                  </div>
                  <ChevronRight className="w-4 h-4 text-white/40 group-hover:text-white" />
                </button>
              </div>
            )}

            {/* Sub-View 2: Edit Profile Form */}
            {activeSettingsView === 'edit_profile' && (
              <form onSubmit={handleSaveProfile} className="space-y-4">
                {/* Change Avatar Image */}
                <div className="space-y-2">
                  <label className="text-xs font-semibold text-white/80 block">{t('editAvatarLabel')}</label>
                  
                  <div className="flex items-center space-x-3">
                    <img
                      src={avatarUrl}
                      alt="Preview Avatar"
                      className="w-14 h-14 rounded-full object-cover border-2 border-[#E63946] shrink-0"
                    />
                    <button
                      type="button"
                      onClick={() => fileInputRef.current?.click()}
                      className="px-3 py-2 rounded-xl bg-white/10 hover:bg-white/20 border border-white/20 text-xs font-semibold text-white flex items-center space-x-1.5"
                    >
                      <Camera className="w-3.5 h-3.5 text-[#22F0B6]" />
                      <span>{t('uploadNewPhoto')}</span>
                    </button>
                    <input
                      type="file"
                      ref={fileInputRef}
                      onChange={handleAvatarFileChange}
                      accept="image/*"
                      className="hidden"
                    />
                  </div>

                  {/* Preset Avatars Selection */}
                  <div className="flex space-x-2 pt-1">
                    {PRESET_AVATARS.map((url, idx) => (
                      <img
                        key={idx}
                        src={url}
                        alt={`Preset ${idx}`}
                        onClick={() => setAvatarUrl(url)}
                        className={`w-9 h-9 rounded-full object-cover cursor-pointer border-2 transition-all ${
                          avatarUrl === url ? 'border-[#F4C430] scale-110' : 'border-white/20 opacity-60 hover:opacity-100'
                        }`}
                      />
                    ))}
                  </div>
                </div>

                {/* Edit Display Name */}
                <div className="space-y-1">
                  <label className="text-xs font-semibold text-white/80 block">{t('editNameLabel')}</label>
                  <input
                    type="text"
                    required
                    value={displayName}
                    onChange={(e) => setDisplayName(e.target.value)}
                    className="w-full bg-black/50 border border-white/20 rounded-xl px-3 py-2 text-xs font-sans text-white focus:outline-none focus:border-[#E63946]"
                  />
                </div>

                {/* Edit Secret Nickname */}
                <div className="space-y-1">
                  <label className="text-xs font-semibold text-white/80 block">{t('editNickLabel')}</label>
                  <input
                    type="text"
                    required
                    value={nickname}
                    onChange={(e) => setNickname(e.target.value)}
                    className="w-full bg-black/50 border border-white/20 rounded-xl px-3 py-2 text-xs font-mono text-white focus:outline-none focus:border-[#F4C430]"
                  />
                </div>

                {/* Submit / Cancel Buttons */}
                <div className="flex items-center space-x-2 pt-2">
                  <button
                    type="button"
                    onClick={() => setActiveSettingsView('menu')}
                    className="flex-1 py-2.5 rounded-xl bg-white/10 hover:bg-white/20 border border-white/20 text-xs font-semibold text-white"
                  >
                    {t('cancelBtn')}
                  </button>
                  <button
                    type="submit"
                    className="flex-1 py-2.5 rounded-xl liquid-glass-accent text-xs font-bold text-white shadow-red-pulse"
                  >
                    {t('saveBtn')}
                  </button>
                </div>
              </form>
            )}

            {/* Sub-View 3: 8-Language Switcher */}
            {activeSettingsView === 'language' && (
              <div className="space-y-3">
                <div className="grid grid-cols-2 gap-2 max-h-60 overflow-y-auto pr-1">
                  {LANGUAGES.map((lang) => (
                    <button
                      key={lang.code}
                      onClick={() => {
                        onChangeLang(lang.code);
                        setIsSettingsOpen(false);
                      }}
                      className={`p-2.5 rounded-2xl text-xs font-medium flex items-center space-x-2 transition-all border ${
                        currentLang === lang.code
                          ? 'liquid-glass-accent border-[#E63946] font-bold text-white shadow-red-pulse'
                          : 'bg-black/30 border-white/10 text-white/70 hover:text-white hover:border-white/20'
                      }`}
                    >
                      <span className="text-base">{lang.flag}</span>
                      <span className="truncate">{lang.name}</span>
                    </button>
                  ))}
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
