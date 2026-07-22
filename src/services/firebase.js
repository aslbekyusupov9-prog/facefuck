/**
 * Firebase Authentication & Firestore Service
 */

// Simulated / Demo Firebase Auth for instant local performance
export async function signInWithGoogle() {
  const mockUser = {
    uid: "google_user_" + Math.random().toString(36).substr(2, 9),
    displayName: "Sharipov D.",
    email: "user@gmail.com",
    photoURL: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80"
  };
  localStorage.setItem('ai_face_user', JSON.stringify(mockUser));
  return mockUser;
}

export async function saveUserScore(uid, nickname, scores, geohash, lat, lng, cityName = "Tashkent") {
  const userData = {
    uid,
    nickname: nickname || "ShadowDragon",
    lastScore: scores,
    geohash: geohash || "t374ab",
    lat: lat || 41.2995,
    lng: lng || 69.2401,
    cityName,
    updatedAt: new Date().toISOString()
  };

  localStorage.setItem('user_score_' + uid, JSON.stringify(userData));
  return userData;
}
