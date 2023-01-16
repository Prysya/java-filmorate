<p align="center">
    <img src="./assets/images/logo.png">
</p>

# Диаграмма базы данных

![Schema image](./assets/images/schema.png)

# Примеры запросов

### Получение списка всех пользователей

```sql
SELECT *
FROM users;
```

| user\_id | email            | login   | name      | birthday   |
|:--------:|:-----------------|:--------|:----------|:-----------|
|    1     | jumba@script.py  | Ayano   | Дмитрий   | 1990-12-05 |
|    2     | jumba1@script.py | Avrisia | Игорь     | 2005-04-21 |
|    3     | jumba2@script.py | Rochan  | Александр | 1850-11-10 |

### Получение списка всех фильмов

```sql
SELECT *
FROM films;
```

| film\_id | name           | description                                                                                                                     | release\_date | duration | mpa\_rating\_id |
|:--------:|:---------------|:--------------------------------------------------------------------------------------------------------------------------------|:--------------|:---------|:----------------|
|    1     | Шестой элемент | Незадачливый коп летит на Луну, чтобы спасти президента. Фантастическая комедия, пародирующая классику сай-фая                  | 2000-10-31    | 99       | 4               |
|    2     | Аэроплан       | Бывший военный лётчик оказывается на борту самолёта, экипаж которого из-за пищевого отравления не в состоянии управлять судном. | 1980-07-02    | 88       | 2               |
|    3     | Один дома      | Мальчик-озорник задает жару грабителям. Лучшая комедия для создания праздничного настроения у всей семьи                        | 2005-12-13    | 103      | 2               |

### Получение списка MPA рейтинга

```sql
SELECT *
FROM mpa_rating;
```

| mpa\_rating\_id | name  |
|:---------------:|:------|
|        1        | G     |
|        2        | PG    |
|        3        | PG-13 |
|        4        | R     |
|        5        | NC-17 |

### Получение списка жанров

```sql
SELECT *
FROM FILM_GENRE;
```

| filmGenre\_id | name           |
|:---------:|:---------------|
|     1     | Комедия        |
|     2     | Драма          |
|     3     | Мультфильм     |
|     4     | Триллер        |
|     5     | Документальный |
|     6     | Боевик         |

### Получение списка жанров фильма

```sql
SELECT f.name as film_name,
       g.FILM_GENRE_NAME as film_genre
FROM films f
         JOIN FILM_GENRES as gs ON f.film_id = gs.film_id
         JOIN FILM_GENRE as g ON gs.FILM_GENRE_ID = g.FILM_GENRE_ID;
```

| film_name      | filmGenre |
|:---------------|:----------|
| Шестой элемент | Комедия   |

### Получение списка фильмов и их жанров

```sql
SELECT f.name as film_name,
       g.FILM_GENRE_NAME as film_genre
FROM films as f
         LEFT JOIN FILM_GENRES as gs ON f.film_id = gs.film_id
         LEFT JOIN FILM_GENRE as g ON gs.FILM_GENRE_ID = g.FILM_GENRE_ID;
```

| film_name      | film\_genre |
|:---------------|:------------|
| Аэроплан       | Комедия     |
| Шестой элемент | Комедия     |
| Один дома      | Боевик      |

### Получение всего списка друзей

```sql
SELECT *
FROM users
         LEFT JOIN friendship_status as fs ON users.user_id = fs.friend_id;
```

| user\_id | email           | login   | name      | birthday   | friendship\_id | user\_id | friend\_id | is\_confirm |
|:---------|:----------------|:--------|:----------|:-----------|:---------------|:---------|:-----------|:------------|
| 1        | jumba@script.py | Ayano   | Дмитрий   | 1990-12-05 | 1              | 1        | 2          | true        |
| 1        | jumba@script.py | Ayano   | Дмитрий   | 1990-12-05 | 2              | 1        | 3          | true        |
| 2        | jumba@script.py | Avrisia | Игорь     | 2005-04-21 | 3              | 2        | 1          | true        |
| 2        | jumba@script.py | Avrisia | Игорь     | 2005-04-21 | 4              | 2        | 3          | true        |
| 3        | jumba@script.py | Rochan  | Александр | 1850-11-10 | 5              | 3        | 1          | true        |
| 3        | jumba@script.py | Rochan  | Александр | 1850-11-10 | 6              | 3        | 2          | false       |

### Получение списка друзей Дмитрия

```sql
SELECT *
FROM users
WHERE user_id IN (SELECT fs.friend_id
                  FROM users as u
                           JOIN friendship_status as fs ON u.user_id = fs.user_id
                  WHERE u.user_id = 3);
```

| user\_id | email           | login | name    | birthday   |
|:---------|:----------------|:------|:--------|:-----------|
| 1        | jumba@script.py | Ayano | Дмитрий | 1990-12-05 |
