from sqlalchemy import Column, Integer, String, Text, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from datetime import datetime
from app.db.base import Base

class FaceAnalysis(Base):
    """FaceAnalysis Model representing stored face rating metrics."""
    __tablename__ = "face_analyses"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"), nullable=False)
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
