from fastapi import APIRouter
from app.api.v1.endpoints import auth, analysis, leaderboard

api_router = APIRouter()

api_router.include_router(auth.router, prefix="/auth", tags=["Authentication"])
api_router.include_router(analysis.router, prefix="/analysis", tags=["Face Analysis"])
api_router.include_router(leaderboard.router, prefix="/leaderboard", tags=["Leaderboard"])
