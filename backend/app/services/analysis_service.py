from sqlalchemy.orm import Session
from app.models.user import User
from app.models.analysis import FaceAnalysis
from app.schemas.analysis import FaceAnalysisCreate

class AnalysisService:
    @staticmethod
    def save_face_analysis(db: Session, analysis_in: FaceAnalysisCreate) -> FaceAnalysis:
        """Save a new face rating analysis result linked to a user."""
        db_user = db.query(User).filter(User.device_id == analysis_in.device_id).first()
        if not db_user:
            db_user = User(device_id=analysis_in.device_id)
            db.add(db_user)
            db.commit()
            db.refresh(db_user)

        new_analysis = FaceAnalysis(
            user_id=db_user.id,
            overall_score=analysis_in.overall_score,
            symmetry_score=analysis_in.symmetry_score,
            skin_score=analysis_in.skin_score,
            eyes_score=analysis_in.eyes_score,
            jaw_score=analysis_in.jaw_score,
            golden_ratio_score=analysis_in.golden_ratio_score,
            facial_thirds_score=analysis_in.facial_thirds_score,
            title=analysis_in.title,
            description=analysis_in.description
        )
        db.add(new_analysis)
        db.commit()
        db.refresh(new_analysis)
        return new_analysis
