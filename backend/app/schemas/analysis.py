from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class FaceAnalysisCreate(BaseModel):
    device_id: str
    overall_score: int
    symmetry_score: int
    skin_score: int
    eyes_score: int
    jaw_score: int
    golden_ratio_score: int
    facial_thirds_score: int
    title: str
    description: Optional[str] = None

class FaceAnalysisOut(BaseModel):
    id: int
    overall_score: int
    symmetry_score: int
    skin_score: int
    eyes_score: int
    jaw_score: int
    golden_ratio_score: int
    facial_thirds_score: int
    title: str
    description: Optional[str] = None
    created_at: datetime

    class Config:
        from_attributes = True

class LeaderboardItem(BaseModel):
    rank: int
    nickname: str
    gender: str
    overall_score: int
    title: str
    avatar_url: Optional[str] = None

class LeaderboardResponse(BaseModel):
    total_users: int
    leaderboard: List[LeaderboardItem]
