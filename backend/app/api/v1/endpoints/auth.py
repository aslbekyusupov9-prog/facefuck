from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.db.session import get_db
from app.schemas.user import UserCreate, UserInDB
from app.schemas.token import Token
from app.services.auth_service import AuthService

router = APIRouter()

@router.post("/register", response_model=UserInDB)
def register_user(user_in: UserCreate, db: Session = Depends(get_db)):
    """Register or update user device profile."""
    user = AuthService.register_or_get_user(db, user_in)
    return user

@router.post("/token", response_model=Token)
def login_access_token(user_in: UserCreate, db: Session = Depends(get_db)):
    """Authenticate user and return JWT Access Token."""
    user = AuthService.register_or_get_user(db, user_in)
    token = AuthService.generate_token_for_user(user)
    return Token(access_token=token, token_type="bearer")
