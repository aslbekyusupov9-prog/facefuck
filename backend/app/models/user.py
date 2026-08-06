from sqlalchemy import Column, Integer, String, Text, DateTime
from sqlalchemy.orm import relationship
from datetime import datetime, timezone
from app.db.base import Base

class User(Base):
    """User Model representing application registered users."""
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    device_id = Column(String(100), unique=True, index=True, nullable=False)
    nickname = Column(String(100), default="Foydalanuvchi")
    gender = Column(String(10), default="MALE")
    hashed_password = Column(String(255), nullable=True)
    avatar_url = Column(Text, nullable=True)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc))

    analyses = relationship("FaceAnalysis", back_populates="owner", cascade="all, delete-orphan")
