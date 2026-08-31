\set ON_ERROR_STOP on

CREATE TYPE direction_style AS ENUM (
    'DRAMA',
    'COMEDY',
    'MUSICAL',
    'OPERA',
    'BALLET'
    );

CREATE TYPE user_role AS ENUM (
    'ADMIN',
    'USER'
    );

CREATE TABLE app_user (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          username VARCHAR(100) NOT NULL,
                          password_hash VARCHAR(255) NOT NULL,
                          role user_role NOT NULL DEFAULT 'USER',

                          CONSTRAINT uq_app_user_username UNIQUE (username),
                          CONSTRAINT chk_app_user_username_not_blank
                              CHECK (btrim(username) <> '')
);

CREATE TABLE country (
                         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         country_code VARCHAR(3) NOT NULL,
                         state_name VARCHAR(100) NOT NULL,

                         CONSTRAINT uq_country_code UNIQUE (country_code),
                         CONSTRAINT uq_country_state_name UNIQUE (state_name),
                         CONSTRAINT chk_country_code_not_blank
                             CHECK (btrim(country_code) <> ''),
                         CONSTRAINT chk_country_state_name_not_blank
                             CHECK (btrim(state_name) <> '')
);

CREATE TABLE city (
                      id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      postal_code VARCHAR(20) NOT NULL,

                      CONSTRAINT uq_city_name_postal_code UNIQUE (name, postal_code),
                      CONSTRAINT chk_city_name_not_blank
                          CHECK (btrim(name) <> ''),
                      CONSTRAINT chk_city_postal_code_not_blank
                          CHECK (btrim(postal_code) <> '')
);

CREATE TABLE actor (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       oib VARCHAR(11) NOT NULL,
                       actor_id VARCHAR(30) NOT NULL,

                       CONSTRAINT uq_actor_oib UNIQUE (oib),
                       CONSTRAINT uq_actor_actor_id UNIQUE (actor_id),
                       CONSTRAINT chk_actor_first_name_not_blank
                           CHECK (btrim(first_name) <> ''),
                       CONSTRAINT chk_actor_last_name_not_blank
                           CHECK (btrim(last_name) <> ''),
                       CONSTRAINT chk_actor_oib_format
                           CHECK (oib ~ '^[0-9]{11}$'),
                       CONSTRAINT chk_actor_actor_id_not_blank
                           CHECK (btrim(actor_id) <> '')
);

CREATE TABLE director (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          oib VARCHAR(11) NOT NULL,
                          director_id VARCHAR(30) NOT NULL,
                          direction_style direction_style,

                          CONSTRAINT uq_director_oib UNIQUE (oib),
                          CONSTRAINT uq_director_director_id UNIQUE (director_id),
                          CONSTRAINT chk_director_first_name_not_blank
                              CHECK (btrim(first_name) <> ''),
                          CONSTRAINT chk_director_last_name_not_blank
                              CHECK (btrim(last_name) <> ''),
                          CONSTRAINT chk_director_oib_format
                              CHECK (oib ~ '^[0-9]{11}$'),
                          CONSTRAINT chk_director_director_id_not_blank
                              CHECK (btrim(director_id) <> '')
);

CREATE TABLE theater (
                         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         name VARCHAR(200) NOT NULL,
                         address VARCHAR(250),
                         founded_year INTEGER NOT NULL,
                         auditorium_capacity INTEGER NOT NULL,
                         history TEXT,
                         image_path VARCHAR(500),
                         country_id BIGINT NOT NULL,
                         city_id BIGINT NOT NULL,

                         CONSTRAINT uq_theater_name_address UNIQUE (name, address),
                         CONSTRAINT chk_theater_name_not_blank
                             CHECK (btrim(name) <> ''),
                         CONSTRAINT chk_theater_founded_year_positive
                             CHECK (founded_year > 0),
                         CONSTRAINT chk_theater_auditorium_capacity_positive
                             CHECK (auditorium_capacity > 0),

                         CONSTRAINT fk_theater_country
                             FOREIGN KEY (country_id)
                                 REFERENCES country(id)
                                 ON UPDATE CASCADE
                                 ON DELETE RESTRICT,

                         CONSTRAINT fk_theater_city
                             FOREIGN KEY (city_id)
                                 REFERENCES city(id)
                                 ON UPDATE CASCADE
                                 ON DELETE RESTRICT
);

CREATE TABLE play (
                      id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      name VARCHAR(200) NOT NULL,
                      director_id BIGINT NOT NULL,
                      theater_id BIGINT NOT NULL,
                      premier_date DATE,
                      performance_counter INTEGER NOT NULL DEFAULT 0,
                      play_type direction_style,

                      CONSTRAINT uq_play_theater_name_premier_date
                          UNIQUE (theater_id, name, premier_date),
                      CONSTRAINT chk_play_name_not_blank
                          CHECK (btrim(name) <> ''),
                      CONSTRAINT chk_play_performance_counter_non_negative
                          CHECK (performance_counter >= 0),

                      CONSTRAINT fk_play_director
                          FOREIGN KEY (director_id)
                              REFERENCES director(id)
                              ON UPDATE CASCADE
                              ON DELETE RESTRICT,

                      CONSTRAINT fk_play_theater
                          FOREIGN KEY (theater_id)
                              REFERENCES theater(id)
                              ON UPDATE CASCADE
                              ON DELETE CASCADE
);

CREATE TABLE play_actor (
                            play_id BIGINT NOT NULL,
                            actor_id BIGINT NOT NULL,

                            CONSTRAINT pk_play_actor PRIMARY KEY (play_id, actor_id),

                            CONSTRAINT fk_play_actor_play
                                FOREIGN KEY (play_id)
                                    REFERENCES play(id)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_play_actor_actor
                                FOREIGN KEY (actor_id)
                                    REFERENCES actor(id)
                                    ON UPDATE CASCADE
                                    ON DELETE RESTRICT
);

CREATE TABLE theater_actor (
                               theater_id BIGINT NOT NULL,
                               actor_id BIGINT NOT NULL,

                               CONSTRAINT pk_theater_actor PRIMARY KEY (theater_id, actor_id),

                               CONSTRAINT fk_theater_actor_theater
                                   FOREIGN KEY (theater_id)
                                       REFERENCES theater(id)
                                       ON UPDATE CASCADE
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_theater_actor_actor
                                   FOREIGN KEY (actor_id)
                                       REFERENCES actor(id)
                                       ON UPDATE CASCADE
                                       ON DELETE RESTRICT
);

CREATE TABLE theater_director (
                                  theater_id BIGINT NOT NULL,
                                  director_id BIGINT NOT NULL,

                                  CONSTRAINT pk_theater_director PRIMARY KEY (theater_id, director_id),

                                  CONSTRAINT fk_theater_director_theater
                                      FOREIGN KEY (theater_id)
                                          REFERENCES theater(id)
                                          ON UPDATE CASCADE
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_theater_director_director
                                      FOREIGN KEY (director_id)
                                          REFERENCES director(id)
                                          ON UPDATE CASCADE
                                          ON DELETE RESTRICT
);

CREATE INDEX idx_theater_country_id
    ON theater(country_id);

CREATE INDEX idx_theater_city_id
    ON theater(city_id);

CREATE INDEX idx_play_director_id
    ON play(director_id);

CREATE INDEX idx_play_theater_id
    ON play(theater_id);

CREATE INDEX idx_play_actor_actor_id
    ON play_actor(actor_id);

CREATE INDEX idx_theater_actor_actor_id
    ON theater_actor(actor_id);

CREATE INDEX idx_theater_director_director_id
    ON theater_director(director_id);