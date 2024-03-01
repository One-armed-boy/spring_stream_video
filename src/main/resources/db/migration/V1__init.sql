-- Member
CREATE TABLE IF NOT EXISTS members (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    last_login_at DATETIME(6),
    role_id INT
);
CREATE INDEX idx_members_email ON members(email);

-- Role
CREATE TABLE IF NOT EXISTS roles (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name ENUM('USER', 'UPLOADER', 'ADMIN')
);

-- Videos
CREATE TABLE IF NOT EXISTS videos (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    file_tag VARCHAR(50) NOT NULL,
    extension VARCHAR(10) NOT NULL,
    path VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    description TEXT,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL
)

