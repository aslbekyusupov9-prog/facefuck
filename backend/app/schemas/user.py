from pydantic import BaseModel
from typing import Optional
from datetime import datetime

class UserBase(BaseModel):
    device_id: str
    nickname: Optional[str] = "Foydalanuvchi"
    gender: Optional[str] = "MALE"
    avatar_url: Optional[str] = None

class UserCreate(UserBase):
    password: Optional[str] = None

class UserUpdate(BaseModel):
    nickname: Optional[str] = None
    gender: Optional[str] = None
    avatar_url: Optional[str] = None

class UserInDB(UserBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True
