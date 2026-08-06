from fastapi import FastAPI, Depends, HTTPException, status
from sqlalchemy.orm import Session
from typing import List

from . import models, schemas, database

models.Base.metadata.create_all(bind=database.engine)

app = FastAPI(
    title="Face Rating AI Backend API",
    description="Python FastAPI & PostgreSQL backend for face attractiveness rating app",
    version="1.0.0"
)

@app.get("/")
def read_root():
    return {"status": "ok", "message": "Face Rating API is running"}

@app.post("/api/v1/users/register", response_model=schemas.UserResponse)
def register_user(user: schemas.UserCreate, db: Session = Depends(database.get_db)):
    db_user = db.query(models.User).filter(models.User.device_id == user.device_id).first()
    if db_user:
        db_user.nickname = user.nickname
        db_user.gender = user.gender
        if user.avatar_url:
            db_user.avatar_url = user.avatar_url
        db.commit()
        db.refresh(db_user)
        return db_user
    
    new_user = models.User(
        device_id=user.device_id,
        nickname=user.nickname,
        gender=user.gender,
        avatar_url=user.avatar_url
    )
    db.add(new_user)
    db.commit()
    db.refresh(new_user)
    return new_user

@app.post("/api/v1/analysis/save")
def save_analysis(analysis: schemas.FaceAnalysisCreate, db: Session = Depends(database.get_db)):
    db_user = db.query(models.User).filter(models.User.device_id == analysis.device_id).first()
    if not db_user:
        db_user = models.User(device_id=analysis.device_id)
        db.add(db_user)
        db.commit()
        db.refresh(db_user)

    new_analysis = models.FaceAnalysis(
        user_id=db_user.id,
        overall_score=analysis.overall_score,
        symmetry_score=analysis.symmetry_score,
        skin_score=analysis.skin_score,
        eyes_score=analysis.eyes_score,
        jaw_score=analysis.jaw_score,
        golden_ratio_score=analysis.golden_ratio_score,
        facial_thirds_score=analysis.facial_thirds_score,
        title=analysis.title,
        description=analysis.description
    )
    db.add(new_analysis)
    db.commit()
    return {"status": "success", "message": "Analysis saved to PostgreSQL database"}

@app.get("/api/v1/leaderboard", response_model=schemas.LeaderboardResponse)
def get_leaderboard(db: Session = Depends(database.get_db)):
    # Query top face ratings joined with user details
    results = (
        db.query(models.User, models.FaceAnalysis)
        .join(models.FaceAnalysis, models.User.id == models.FaceAnalysis.user_id)
        .order_by(models.FaceAnalysis.overall_score.desc())
        .limit(50)
        .all()
    )

    items = []
    for rank, (user, analysis) in enumerate(results, start=1):
        items.append(schemas.LeaderboardItem(
            rank=rank,
            nickname=user.nickname,
            gender=user.gender,
            overall_score=analysis.overall_score,
            title=analysis.title,
            avatar_url=user.avatar_url
        ))

    return schemas.LeaderboardResponse(total_users=len(items), leaderboard=items)
