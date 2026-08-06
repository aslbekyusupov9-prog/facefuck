-- PostgreSQL Database Schema for Face Rating System
-- Database name: face_rating_db

-- Drop tables if exists
DROP TABLE IF EXISTS face_analyses CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    device_id VARCHAR(100) UNIQUE NOT NULL,
    nickname VARCHAR(100) NOT NULL DEFAULT 'Foydalanuvchi',
    gender VARCHAR(10) NOT NULL DEFAULT 'MALE',
    avatar_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create Face Analyses table
CREATE TABLE face_analyses (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    overall_score INT NOT NULL,
    symmetry_score INT NOT NULL,
    skin_score INT NOT NULL,
    eyes_score INT NOT NULL,
    jaw_score INT NOT NULL,
    golden_ratio_score INT NOT NULL,
    facial_thirds_score INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Insert initial mock data for Top Leaderboard
INSERT INTO users (device_id, nickname, gender) VALUES 
('dev_001', 'Aziza', 'FEMALE'),
('dev_002', 'Kamron', 'MALE'),
('dev_003', 'Shaxzoda', 'FEMALE'),
('dev_004', 'Dilmurod', 'MALE'),
('dev_005', 'Zuhra', 'FEMALE'),
('dev_006', 'Bekzod', 'MALE'),
('dev_007', 'Sardor', 'MALE'),
('dev_008', 'Lola', 'FEMALE');

INSERT INTO face_analyses (user_id, overall_score, symmetry_score, skin_score, eyes_score, jaw_score, golden_ratio_score, facial_thirds_score, title) VALUES
(1, 98, 98, 97, 99, 96, 98, 97, 'Mukammal Go''zallik'),
(2, 95, 95, 94, 96, 95, 94, 96, 'Mukammal Go''zallik'),
(3, 92, 92, 91, 93, 90, 92, 93, 'Mukammal Go''zallik'),
(4, 89, 89, 88, 90, 88, 89, 88, 'Jozibador'),
(5, 87, 87, 86, 88, 85, 87, 86, 'Jozibador'),
(6, 81, 81, 80, 82, 80, 81, 80, 'Jozibador'),
(7, 78, 78, 77, 79, 76, 78, 77, 'O''ziga Xos'),
(8, 75, 75, 74, 76, 73, 75, 74, 'O''ziga Xos');
