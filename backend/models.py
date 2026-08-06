from sqlalchemy import Column, Integer, String, Text, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from datetime import datetime
from .database import Base

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    device_id = Column(String(100), unique=True, index=True, nullable=False)
    nickname = Column(String(100), default="Foydalanuvchi")
    gender = Column(String(10), default="MALE")
    avatar_url = Column(Text, nullable=True)
    created_at = Column(DateTime, default=datetime.utcnow)

    analyses = relationship("FaceAnalysis", back_populates="owner")

class FaceAnalysis(Base):
    __tablename__ = "face_analyses"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    overall_score = Column(Integer, nullable=False)
    symmetry_score = Column(Integer, nullable=False)
    skin_score = Column(Integer, nullable=False)
    eyes_score = Column(Integer, nullable=False)
    jaw_score = Column(Integer, nullable=False)
    golden_ratio_score = Column(Integer, nullable=False)
    facial_thirds_score = Column(Integer, nullable=False)
    title = Column(String(100), nullable=False)
    description = Column(Text, nullable=True)
    created_at = Column(DateTime, default=datetime.utcnow)

    owner = relationship("User", back_populates="analyses")
