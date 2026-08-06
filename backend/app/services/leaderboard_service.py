from sqlalchemy.orm import Session
from sqlalchemy import func
from app.models.user import User
from app.models.analysis import FaceAnalysis
from app.schemas.analysis import LeaderboardResponse, LeaderboardItem

class LeaderboardService:
    @staticmethod
    def get_top_leaderboard(db: Session, limit: int = 50) -> LeaderboardResponse:
        """Fetch top ranked unique users sorted by their highest face analysis score."""
        # Query highest overall_score per user to prevent duplicate entries
        subquery = (
            db.query(
                FaceAnalysis.user_id,
                func.max(FaceAnalysis.overall_score).label("max_score")
            )
            .group_by(FaceAnalysis.user_id)
            .subquery()
        )

        results = (
            db.query(User, FaceAnalysis)
            .join(subquery, User.id == subquery.c.user_id)
            .join(FaceAnalysis, (FaceAnalysis.user_id == subquery.c.user_id) & (FaceAnalysis.overall_score == subquery.c.max_score))
            .order_by(subquery.c.max_score.desc())
            .limit(limit)
            .all()
        )

        items = []
        seen_users = set()
        for user, analysis in results:
            if user.id in seen_users:
                continue
            seen_users.add(user.id)
            items.append(LeaderboardItem(
                rank=len(items) + 1,
                nickname=user.nickname,
                gender=user.gender,
                overall_score=analysis.overall_score,
                title=analysis.title,
                avatar_url=user.avatar_url
            ))

        return LeaderboardResponse(total_users=len(items), leaderboard=items)
