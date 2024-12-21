CREATE DATABASE IF NOT EXISTS mdd;

USE mdd;

-- Création de la table 'users'
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Création de la table 'themes'
CREATE TABLE themes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL
);

-- Création de la table 'articles'
CREATE TABLE articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    theme_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (theme_id) REFERENCES themes(id)
);

-- Création de la table 'comments'
CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    article_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (article_id) REFERENCES articles(id)
);

-- Insertion des données dans la table 'users'
INSERT INTO users (username, email, password, role, created_at) VALUES
('john_doe', 'john@example.com', 'password123', 'user', CURRENT_TIMESTAMP),
('jane_smith', 'jane@example.com', 'password456', 'user', CURRENT_TIMESTAMP);

-- Insertion des données dans la table 'themes'
INSERT INTO themes (title, description) VALUES
('Java', 'object-oriented programming language'),
('Python','object-oriented programming language'),
('JavaScript','object-oriented programming language'),
('C++','object-oriented programming language'),
('Ruby','object-oriented programming language'),
('Go','object-oriented programming language');

-- Insertion des articles dans la table 'articles'
INSERT INTO articles (title, content, created_at, user_id, theme_id) VALUES
('Introduction to Java Streams', 'Content about Java Streams...', CURRENT_TIMESTAMP, 1, 1),
('Advanced Python Tips', 'Content about Python tips...', CURRENT_TIMESTAMP, 2, 2),
('JavaScript Async Patterns', 'Content about async in JavaScript...', CURRENT_TIMESTAMP, 1, 3),
('Memory Management in C++', 'Content about C++ memory...', CURRENT_TIMESTAMP, 2, 4);

-- Insertion des commentaires dans la table 'comments'
INSERT INTO comments (content, created_at, user_id, article_id) VALUES
('Very helpful on Java streams!', CURRENT_TIMESTAMP, 2, 1),
('I learned a lot about async patterns!', CURRENT_TIMESTAMP, 1, 3),
('Helpful on Java streams!', CURRENT_TIMESTAMP, 2, 1),
('I learned a lot about async patterns!', CURRENT_TIMESTAMP, 1, 3);
