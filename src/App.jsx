import React, { useState, useEffect } from 'react';
import MobileShell from './components/MobileShell';
import OnboardingScreen from './components/OnboardingScreen';
import AuthScreen from './components/AuthScreen';
import PhotoUploadScreen from './components/PhotoUploadScreen';
import ScanningScreen from './components/ScanningScreen';
import ResultScreen from './components/ResultScreen';
import TipsScreen from './components/TipsScreen';
import LeaderboardScreen from './components/LeaderboardScreen';
import ProfileScreen from './components/ProfileScreen';

export default function App() {
  const [currentStep, setCurrentStep] = useState('onboarding'); // onboarding, auth, upload, scanning, result, leaderboard, tips, profile
  const [activeTab, setActiveTab] = useState('upload');
  const [user, setUser] = useState(null);
  const [selectedImage, setSelectedImage] = useState(null);
  const [selectedGender, setSelectedGender] = useState('male');
  const [faceScores, setFaceScores] = useState(null);

  // App language state (8 languages: uz, ru, en, kk, ky, tg, hi, tr)
  const [currentLang, setCurrentLang] = useState(() => {
    return localStorage.getItem('app_language') || 'uz';
  });

  const handleChangeLanguage = (langCode) => {
    setCurrentLang(langCode);
    localStorage.setItem('app_language', langCode);
  };

  // Check saved session on launch
  useEffect(() => {
    const savedUser = localStorage.getItem('ai_face_user');
    if (savedUser) {
      try {
        const parsed = JSON.parse(savedUser);
        setUser(parsed);
      } catch (e) {}
    }
  }, []);

  const handleOnboardingComplete = () => {
    if (user) {
      setCurrentStep('upload');
    } else {
      setCurrentStep('auth');
    }
  };

  const handleLoginSuccess = (loggedInUser) => {
    setUser(loggedInUser);
    setCurrentStep('upload');
  };

  const handleUpdateUser = (updatedUser) => {
    setUser(updatedUser);
  };

  const handlePhotoSelected = (imgSrc, gender) => {
    setSelectedImage(imgSrc);
    setSelectedGender(gender);
    setCurrentStep('scanning');
  };

  const handleAnalysisComplete = (results) => {
    setFaceScores(results);
    setCurrentStep('result');
  };

  const handleLogout = () => {
    localStorage.removeItem('ai_face_user');
    setUser(null);
    setCurrentStep('auth');
  };

  const handleReAnalyze = () => {
    setSelectedImage(null);
    setFaceScores(null);
    setCurrentStep('upload');
    setActiveTab('upload');
  };

  const handleTabChange = (tabName) => {
    setActiveTab(tabName);
    if (tabName === 'upload') {
      setCurrentStep(selectedImage && faceScores ? 'result' : 'upload');
    } else if (tabName === 'leaderboard') {
      setCurrentStep('leaderboard');
    } else if (tabName === 'tips') {
      setCurrentStep('tips');
    } else if (tabName === 'profile') {
      setCurrentStep('profile');
    }
  };

  return (
    <div className="relative min-h-screen bg-[#0d0403] text-white overflow-hidden">
      {/* Main Mobile App Container */}
      <MobileShell activeTab={activeTab} setActiveTab={handleTabChange} user={user} currentLang={currentLang}>
        {currentStep === 'onboarding' && (
          <OnboardingScreen onComplete={handleOnboardingComplete} />
        )}

        {currentStep === 'auth' && (
          <AuthScreen onLoginSuccess={handleLoginSuccess} />
        )}

        {currentStep === 'upload' && (
          <PhotoUploadScreen onPhotoSelected={handlePhotoSelected} />
        )}

        {currentStep === 'scanning' && (
          <ScanningScreen
            imageSrc={selectedImage}
            gender={selectedGender}
            onAnalysisComplete={handleAnalysisComplete}
          />
        )}

        {currentStep === 'result' && (
          <ResultScreen
            imageSrc={selectedImage}
            scores={faceScores}
            gender={selectedGender}
            onReAnalyze={handleReAnalyze}
            onGoLeaderboard={() => handleTabChange('leaderboard')}
            onGoTips={() => handleTabChange('tips')}
          />
        )}

        {currentStep === 'tips' && (
          <TipsScreen scores={faceScores} />
        )}

        {currentStep === 'leaderboard' && (
          <LeaderboardScreen currentUser={user} userScore={faceScores} />
        )}

        {currentStep === 'profile' && (
          <ProfileScreen
            user={user}
            onUpdateUser={handleUpdateUser}
            onLogout={handleLogout}
            currentLang={currentLang}
            onChangeLang={handleChangeLanguage}
          />
        )}
      </MobileShell>
    </div>
  );
}
