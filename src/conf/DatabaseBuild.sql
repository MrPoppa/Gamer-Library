CREATE TABLE Owner(
id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
firstName VARCHAR(64),
lastName VARCHAR(64),
email VARCHAR(64),
ssn VARCHAR(12),
userName VARCHAR(24),
password VARCHAR(16)
);

CREATE TABLE Genre (
id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
genre VARCHAR(64)
);

CREATE TABLE Game_Rating (
id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
rating VARCHAR(64)
);

CREATE TABLE Platform_Brand (
id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
brandName VARCHAR(64)
);

CREATE TABLE Game_Brand (
id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
brandName VARCHAR(64)
);

CREATE TABLE Platform(
id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
platformName VARCHAR(64),
price INTEGER,
buyDate DATE,
brand_id INTEGER,
CONSTRAINT fk_platform_brand FOREIGN KEY(brand_id) REFERENCES Platform_Brand(id)
);

CREATE TABLE Game (
id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
gameName VARCHAR(64),
price INTEGER,
buyDate DATE,
platform_id INTEGER,
rating_id INTEGER,
brand_id INTEGER,
CONSTRAINT fk_game_platform FOREIGN KEY(platform_id) REFERENCES Platform(id),
CONSTRAINT fk_game_rating FOREIGN KEY(rating_id) REFERENCES Game_Rating(id),
CONSTRAINT fk_game_brand FOREIGN KEY(brand_id) REFERENCES Game_Brand(id)
);

CREATE TABLE Platform_Owner(
owner_id INTEGER,
platform_id INTEGER,
CONSTRAINT fk_platform_owner FOREIGN KEY(owner_id) REFERENCES Owner(id),
CONSTRAINT fk_owner_platform FOREIGN KEY(platform_id) REFERENCES Platform(id)
);

CREATE TABLE Game_Owner(
owner_id INTEGER,
game_id INTEGER,
CONSTRAINT fk_game_owner FOREIGN KEY(owner_id) REFERENCES Owner(id),
CONSTRAINT fk_owner_game FOREIGN KEY(game_id) REFERENCES Game(id)
);

CREATE TABLE Game_Genre(
game_id INTEGER,
genre_id INTEGER,
CONSTRAINT fk_genre_game FOREIGN KEY(game_id) REFERENCES Game(id),
CONSTRAINT fk_game_genre FOREIGN KEY(genre_id) REFERENCES Genre(id)
)
