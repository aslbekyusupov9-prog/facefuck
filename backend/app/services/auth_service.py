from sqlalchemy.orm import Session
from app.models.user import User
from app.schemas.user import UserCreate
from app.core.security import get_password_hash, verify_password, create_access_token

class AuthService:
    @staticmethod
    def register_or_get_user(db: Session, user_in: UserCreate) -> User:
        """Register a new user by device_id or return existing user."""
        db_user = db.query(User).filter(User.device_id == user_in.device_id).first()
        if db_user:
            if user_in.nickname:
                db_user.nickname = user_in.nickname
            if user_in.gender:
                db_user.gender = user_in.gender
            if user_in.avatar_url:
                db_user.avatar_url = user_in.avatar_url
            db.commit()
            db.refresh(db_user)
            return db_user

        hashed_pwd = get_password_hash(user_in.password) if user_in.password else None
        new_user = User(
            device_id=user_in.device_id,
            nickname=user_in.nickname,
            gender=user_in.gender,
            hashed_password=hashed_pwd,
            avatar_url=user_in.avatar_url
        )
        db.add(new_user)
        db.commit()
        db.refresh(new_user)
        return new_user

    @staticmethod
    def generate_token_for_user(user: User) -> str:
        """Generate JWT access token for a user."""
        return create_access_token(subject=user.device_id)
