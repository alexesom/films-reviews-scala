CREATE TABLE IF NOT EXISTS age_ratings(
    id serial PRIMARY KEY,
	age_rating VARCHAR (10) NOT NULL
);

CREATE TABLE IF NOT EXISTS directors(
    id serial PRIMARY KEY,
	firstName VARCHAR (30) NOT NULL,
	secondName VARCHAR (50) NOT NULL,
	dob DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS languages(
    id serial PRIMARY KEY,
    lang VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS countries(
    id serial PRIMARY KEY,
    country VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres(
    id serial PRIMARY KEY,
	genre VARCHAR (20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS movies(
    id serial PRIMARY KEY,
   	title VARCHAR (50) NOT NULL UNIQUE,
	release DATE NOT NULL,
	age_rating_id INT NOT NULL,
	actors VARCHAR (255) NOT NULL,
	plot VARCHAR (500) NOT NULL,
	runtime INT NOT NULL,
	poster VARCHAR(200) NOT NULL,
	language_id INT NOT NULL,
	country_id INT NOT NULL,
	awards VARCHAR(100) NOT NULL,
	imdb_id VARCHAR(15) NOT NULL,
	is_public bool NOT NULL,

    FOREIGN KEY (age_rating_id)
         REFERENCES age_ratings(id),
    FOREIGN KEY (language_id)
         REFERENCES languages(id),
    FOREIGN KEY (country_id)
         REFERENCES countries(id)
);

CREATE TABLE IF NOT EXISTS comments(
    id serial PRIMARY KEY,
	header VARCHAR (50),
	description VARCHAR(2000),
	rate INT NOT NULL,
	movie_id INT NOT NULL,
	recommendation BOOL,

	FOREIGN KEY (movie_id)
	    REFERENCES movies(id)
);

CREATE TABLE IF NOT EXISTS genres_movies(
    id serial PRIMARY KEY,
	genre_id INT NOT NULL,
	movie_id INT NOT NULL,

	FOREIGN KEY (genre_id)
         REFERENCES genres(id),
    FOREIGN KEY (movie_id)
         REFERENCES movies(id)
);

CREATE TABLE IF NOT EXISTS directors_movies(
    id serial PRIMARY KEY,
	director_id INT NOT NULL,
	movie_id INT NOT NULL,

	FOREIGN KEY (director_id)
         REFERENCES directors(id),
    FOREIGN KEY (movie_id)
         REFERENCES movies(id)
);

CREATE TABLE IF NOT EXISTS users(
    id serial PRIMARY KEY,
	first_name VARCHAR(40),
	second_name VARCHAR(40),
	login VARCHAR(20) UNIQUE NOT NULL,
	password VARCHAR(70) NOT NULL,
	is_admin BOOL NOT NULL
);