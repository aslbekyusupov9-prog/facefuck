from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.db.session import get_db
from app.schemas.analysis import LeaderboardResponse
from app.services.leaderboard_service import LeaderboardService

router = APIRouter()

@router.get("", response_model=LeaderboardResponse)
def get_leaderboard(db: Session = Depends(get_db)):
    """Fetch live top face rating leaderboard."""
    return LeaderboardService.get_top_leaderboard(db)
