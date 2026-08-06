from fastapi import APIRouter, Depends, status
from sqlalchemy.orm import Session
from app.db.session import get_db
from app.schemas.analysis import FaceAnalysisCreate, FaceAnalysisOut
from app.services.analysis_service import AnalysisService

router = APIRouter()

@router.post("/save", status_code=status.HTTP_201_CREATED)
def save_face_analysis(analysis_in: FaceAnalysisCreate, db: Session = Depends(get_db)):
    """Save face analysis result to PostgreSQL database."""
    saved_analysis = AnalysisService.save_face_analysis(db, analysis_in)
    return {"status": "success", "id": saved_analysis.id, "message": "Analysis saved to database."}
