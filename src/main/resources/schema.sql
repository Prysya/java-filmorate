drop table if exists FRIENDSHIP_STATUS;
drop table if exists FILM_GENRES;
drop table if exists FILM_GENRE;
drop table if exists LIKES;
drop table if exists FILMS;
drop table if exists MPA_RATING;
drop table if exists USERS;

create user if not exists sa password 'password' admin; -- создать пользователя для проекта

create table if not exists users
(
    email    varchar not null,
    login    varchar not null,
    birthday DATE              not null,
    name     varchar,
    user_id  INTEGER auto_increment,
    constraint users_pk
        primary key (user_id),
    constraint uq_users_email UNIQUE (email),
    constraint uq_users_login UNIQUE (login)
);

create table if not exists friendship_status
(
    user_id             INTEGER not null,
    friend_id           INTEGER not null,
    constraint friendship_status_pk
        primary key (user_id, friend_id),
    constraint "friendship_status_USERS_USER_ID_fk"
        foreign key (user_id) references USERS ON DELETE CASCADE,
    constraint "friendship_status_USERS_USER_ID_fk2"
        foreign key (friend_id) references USERS ON DELETE CASCADE,
    constraint FriendsAreDistinct_CK
        check (user_id <> friend_id)
);

create table if not exists mpa_rating
(
    mpa_rating_name   varchar not null,
    mpa_rating_id INTEGER auto_increment,
    constraint mpa_rating_pk
        primary key (mpa_rating_id)
);

create table if not exists films
(
    film_id       INTEGER auto_increment,
    name          CHARACTER VARYING not null,
    description   varchar(200),
    release_date  DATE              not null,
    duration      INTEGER           not null,
    mpa_rating_id INTEGER,
    constraint films_pk
        primary key (film_id),
    constraint "films_MPA_RATING_mpa_rating_id_fk"
        foreign key (mpa_rating_id) references MPA_RATING (mpa_rating_id)
);

create table if not exists likes
(
    film_id INTEGER not null,
    user_id INTEGER not null,
    constraint likes_pk
        primary key (film_id, user_id),
    constraint "likes_USERS_user_id_fk"
        foreign key (user_id) references USERS ON DELETE CASCADE,
    constraint "likes_FILMS_film_id_fk"
        foreign key (film_id) references films
);

create table if not exists film_genre
(
    film_genre_name varchar not null,
    film_genre_id   INTEGER auto_increment,
    constraint film_genre_pk
        primary key (film_genre_id)
);


create table if not exists film_genres
(
    film_id   INTEGER not null,
    film_genre_id  INTEGER not null,
    constraint film_genres_id
        primary key (film_id, film_genre_id),
    constraint "film_genres_genre_genre_id_fk"
        foreign key (film_genre_id) references film_genre,
    constraint "film_genres_films_film_id_fk"
        foreign key (film_id) references films
);
