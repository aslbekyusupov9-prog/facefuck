from sqlalchemy.orm import Session
from app.models.user import User
from app.models.analysis import FaceAnalysis
from app.schemas.analysis import LeaderboardResponse, LeaderboardItem

class LeaderboardService:
    @staticmethod
    def get_top_leaderboard(db: Session, limit: int = 50) -> LeaderboardResponse:
        """Fetch top ranked users sorted by overall face analysis score."""
        results = (
            db.query(User, FaceAnalysis)
            .join(FaceAnalysis, User.id == FaceAnalysis.user_id)
            .order_by(FaceAnalysis.overall_score.desc())
            .limit(limit)
            .all()
        )

        items = []
        for rank, (user, analysis) in enumerate(results, start=1):
            items.append(LeaderboardItem(
                rank=rank,
                nickname=user.nickname,
                gender=user.gender,
                overall_score=analysis.overall_score,
                title=analysis.title,
                avatar_url=user.avatar_url
            ))

        return LeaderboardResponse(total_users=len(items), leaderboard=items)
