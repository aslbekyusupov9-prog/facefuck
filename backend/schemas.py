from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class UserBase(BaseModel):
    device_id: str
    nickname: Optional[str] = "Foydalanuvchi"
    gender: Optional[str] = "MALE"
    avatar_url: Optional[str] = None

class UserCreate(UserBase):
    pass

class UserResponse(UserBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True

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
