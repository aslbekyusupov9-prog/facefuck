import React, { useState } from 'react';
import { Sparkles, Shield, Lock, User, Mail, UserCheck, ArrowRight } from 'lucide-react';
import { signInWithGoogle } from '../services/firebase';

export default function AuthScreen({ onLoginSuccess }) {
  const [authMode, setAuthMode] = useState('register'); // 'register' or 'login'
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Form Fields
  const [fullName, setFullName] = useState('');
  const [nickname, setNickname] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  // Handle Manual Form Submission (Register or Login)
  const handleSubmitForm = (e) => {
    e.preventDefault();
    setError(null);

    if (!email || !password) {
      setError("Iltimos, email va parolni kiriting.");
      return;
    }

    if (authMode === 'register' && (!fullName || !nickname)) {
      setError("Iltimos, ismingiz va maxfiy taxallusingizni kiriting.");
      return;
    }

    setLoading(true);

    setTimeout(() => {
      const newUser = {
        uid: "user_" + Math.random().toString(36).substr(2, 9),
        displayName: fullName || nickname || "Foydalanuvchi",
        nickname: nickname || "ShadowRider",
        email: email,
        photoURL: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
      };

      localStorage.setItem('ai_face_user', JSON.stringify(newUser));
      localStorage.setItem('user_nickname', newUser.nickname);
      setLoading(false);
      onLoginSuccess(newUser);
    }, 800);
  };

  // Handle Google Sign-In
  const handleGoogleLogin = async () => {
    setLoading(true);
    setError(null);
    try {
      const user = await signInWithGoogle();
      onLoginSuccess(user);
    } catch (e) {
      console.error(e);
      setError("Google orqali kirishda xatolik yuz berdi.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="h-full flex flex-col justify-between py-4 px-2 text-center animate-fadeIn">
      {/* Top Branding Header */}
      <div className="space-y-2 pt-2">
        <div className="w-14 h-14 rounded-2xl liquid-glass-gold mx-auto flex items-center justify-center border border-[#D4AF37]/50 shadow-gold-glow">
          <Sparkles className="w-7 h-7 text-[#F4C430]" />
        </div>
        <h1 className="text-xl font-extrabold font-heading text-white tracking-tight">
          AI Face Rating
        </h1>
        <p className="text-xs text-white/60 font-sans max-w-xs mx-auto">
          {authMode === 'register' ? 'Shaxsiy hisob yaratish va tahlil qilish' : 'Akkauntingizga kirish'}
        </p>
      </div>

      {/* Mode Switcher Tabs (Ro'yxatdan O'tish vs Kirish) */}
      <div className="liquid-glass rounded-2xl p-1 flex items-center my-3 border border-white/15">
        <button
          onClick={() => {
            setAuthMode('register');
            setError(null);
          }}
          className={`flex-1 py-2 rounded-xl text-xs font-bold font-heading transition-all ${
            authMode === 'register'
              ? 'bg-[#E63946] text-white shadow-md'
              : 'text-white/60 hover:text-white'
          }`}
        >
          Ro'yxatdan O'tish
        </button>
        <button
          onClick={() => {
            setAuthMode('login');
            setError(null);
          }}
          className={`flex-1 py-2 rounded-xl text-xs font-bold font-heading transition-all ${
            authMode === 'login'
              ? 'bg-[#E63946] text-white shadow-md'
              : 'text-white/60 hover:text-white'
          }`}
        >
          Kirish
        </button>
      </div>

      {/* Auth Form Card */}
      <div className="liquid-glass rounded-card p-5 my-auto space-y-4 text-left border border-white/15">
        {error && (
          <div className="p-2.5 rounded-xl bg-red-950/60 border border-red-500/40 text-[11px] text-red-300 font-medium text-center">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmitForm} className="space-y-3">
          {authMode === 'register' && (
            <>
              {/* Full Name Field */}
              <div className="space-y-1">
                <label className="text-[11px] font-semibold text-white/80 block">Ism va Familiyangiz:</label>
                <div className="relative">
                  <User className="w-4 h-4 text-white/40 absolute left-3 top-3" />
                  <input
                    type="text"
                    required
                    value={fullName}
                    onChange={(e) => setFullName(e.target.value)}
                    placeholder="Masalan: Jamshid Sharipov"
                    className="w-full bg-black/40 border border-white/20 rounded-xl pl-9 pr-3 py-2 text-xs font-sans text-white placeholder-white/30 focus:outline-none focus:border-[#E63946]"
                  />
                </div>
              </div>

              {/* Nickname Field for 20km Leaderboard Privacy */}
              <div className="space-y-1">
                <label className="text-[11px] font-semibold text-white/80 block">Maxfiy Taxallus (Reyting uchun):</label>
                <div className="relative">
                  <UserCheck className="w-4 h-4 text-[#F4C430] absolute left-3 top-3" />
                  <input
                    type="text"
                    required
                    value={nickname}
                    onChange={(e) => setNickname(e.target.value)}
                    placeholder="Masalan: ShadowDragon99"
                    className="w-full bg-black/40 border border-white/20 rounded-xl pl-9 pr-3 py-2 text-xs font-mono text-white placeholder-white/30 focus:outline-none focus:border-[#F4C430]"
                  />
                </div>
              </div>
            </>
          )}

          {/* Email Field */}
          <div className="space-y-1">
            <label className="text-[11px] font-semibold text-white/80 block">Email Manzil:</label>
            <div className="relative">
              <Mail className="w-4 h-4 text-white/40 absolute left-3 top-3" />
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="name@gmail.com"
                className="w-full bg-black/40 border border-white/20 rounded-xl pl-9 pr-3 py-2 text-xs font-sans text-white placeholder-white/30 focus:outline-none focus:border-[#E63946]"
              />
            </div>
          </div>

          {/* Password Field */}
          <div className="space-y-1">
            <label className="text-[11px] font-semibold text-white/80 block">Parol:</label>
            <div className="relative">
              <Lock className="w-4 h-4 text-white/40 absolute left-3 top-3" />
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full bg-black/40 border border-white/20 rounded-xl pl-9 pr-3 py-2 text-xs font-sans text-white placeholder-white/30 focus:outline-none focus:border-[#E63946]"
              />
            </div>
          </div>

          {/* Primary Submit Button */}
          <button
            type="submit"
            disabled={loading}
            className="w-full py-3.5 rounded-btn liquid-glass-accent text-white font-bold font-heading text-xs tracking-wide flex items-center justify-center space-x-2 shadow-red-pulse hover:opacity-95 transition-all mt-2 active:scale-95 disabled:opacity-50"
          >
            {loading ? (
              <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
            ) : (
              <>
                <span>{authMode === 'register' ? 'Ro\'yxatdan O\'tish' : 'Kirish'}</span>
                <ArrowRight className="w-4 h-4" />
              </>
            )}
          </button>
        </form>

        {/* Divider */}
        <div className="relative flex py-1 items-center">
          <div className="flex-grow border-t border-white/10"></div>
          <span className="flex-shrink mx-2 text-[10px] font-mono text-white/40 uppercase">yoki</span>
          <div className="flex-grow border-t border-white/10"></div>
        </div>

        {/* Google One Tap Style Button */}
        <button
          type="button"
          onClick={handleGoogleLogin}
          disabled={loading}
          className="w-full py-3 px-4 rounded-btn bg-white hover:bg-gray-100 text-gray-900 font-semibold font-sans text-xs flex items-center justify-center space-x-2.5 shadow-md transition-all active:scale-95 disabled:opacity-75"
        >
          <svg className="w-4 h-4" viewBox="0 0 24 24">
            <path
              fill="#4285F4"
              d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
            />
            <path
              fill="#34A853"
              d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
            />
            <path
              fill="#FBBC05"
              d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"
            />
            <path
              fill="#EA4335"
              d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"
            />
          </svg>
          <span>Google bilan tezkor kirish</span>
        </button>
      </div>

      {/* Security Privacy Badge */}
      <div className="liquid-glass rounded-2xl p-2.5 flex items-center justify-center space-x-2 text-[11px] text-white/60">
        <Shield className="w-3.5 h-3.5 text-[#22F0B6]" />
        <span>Maxfiyligingiz va biometrik ma'lumotlaringiz 100% himoyalangan</span>
      </div>
    </div>
  );
}
