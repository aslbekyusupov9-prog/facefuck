from sqlalchemy.orm import Session
from sqlalchemy import func
from app.models.user import User
from app.models.analysis import FaceAnalysis
from app.schemas.analysis import LeaderboardResponse, LeaderboardItem

class LeaderboardService:
    @staticmethod
    def get_top_leaderboard(db: Session, limit: int = 100) -> LeaderboardResponse:
        """Fetch top 100 ranked unique users sorted by their highest face analysis score with automatic fallback populate."""
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
                avatar_url=user.avatar_url,
                symmetry_score=analysis.symmetry_score,
                skin_score=analysis.skin_score,
                eyes_score=analysis.eyes_score,
                jaw_score=analysis.jaw_score,
                golden_ratio_score=analysis.golden_ratio_score,
                facial_thirds_score=analysis.facial_thirds_score
            ))

        # If database records are less than 10, pad with dynamic leaderboard candidates
        if len(items) < 10:
            demo_names = ["Aziza", "Kamron", "Shaxzoda", "Dilmurod", "Zuhra", "Bekzod", "Sardor", "Lola", "Jasur", "Nigora"]
            start_score = 98 if not items else min(item.overall_score for item in items) - 2

            for i, name in enumerate(demo_names):
                if len(items) >= limit:
                    break
                score = MathMax(45, start_score - (i * 2))
                items.append(LeaderboardItem(
                    rank=len(items) + 1,
                    nickname=name,
                    gender="FEMALE" if i % 2 == 0 else "MALE",
                    overall_score=score,
                    title="Mukammal Go'zallik" if score >= 85 else "Jozibador",
                    avatar_url=None,
                    symmetry_score=score - 1,
                    skin_score=score - 3,
                    eyes_score=score - 2,
                    jaw_score=score - 2,
                    golden_ratio_score=score - 1,
                    facial_thirds_score=score - 4
                ))

        return LeaderboardResponse(total_users=len(items), leaderboard=items)

def MathMax(a, b):
    return a if a > b else b
