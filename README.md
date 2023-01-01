<p align="center">
    <img src="./assets/images/logo.png">
</p>

# Диаграмма базы данных

![Schema image](./assets/images/schema.png)

# Примеры запросов

### Получение списка всех пользователей

```sql
SELECT *
FROM user;
```

| user\_id | email            | login   | name      | birthday   |
|:--------:|:-----------------|:--------|:----------|:-----------|
|    1     | jumba@script.py  | Ayano   | Дмитрий   | 1990-12-05 |
|    2     | jumba1@script.py | Avrisia | Игорь     | 2005-04-21 |
|    3     | jumba2@script.py | Rochan  | Александр | 1850-11-10 |

### Получение списка всех фильмов

```sql
SELECT *
FROM film;
```

| film\_id | name           | description                                                                                                                      | release\_date | duration | mpa\_rating\_id |
|:--------:|:---------------|:---------------------------------------------------------------------------------------------------------------------------------|:--------------|:---------|:----------------|
|    1     | Шестой элемент | Незадачливый коп летит на Луну, чтобы спасти президента. Фантастическая комедия, пародирующая классику сай-фая                   | 2000-10-31    | 99       | 4               |
|    2     | Аэроплан       | Бывший военный лётчик оказывается на борту самолёта, экипаж которого из-за пищевого отравления не в состоянии управлять судном.  | 1980-07-02    | 88       | 2               |
|    3     | Один дома      | Мальчик-озорник задает жару грабителям. Лучшая комедия для создания праздничного настроения у всей семьи                         | 2005-12-13    | 103      | 2               |



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
FROM genres;
```

| genre\_id | name           |
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
       g.name as genre
FROM film f
         JOIN genres as gs ON f.film_id = gs.film_id
         JOIN genre as g ON gs.genre_id = g.genre_id;
```

| film_name      | genre   |
|:---------------|:--------|
| Шестой элемент | Комедия |

### Получение списка фильмов и их жанров

```sql
SELECT f.name as film_name,
       g.name as genre
FROM film f
         LEFT JOIN genres as gs ON f.film_id = gs.film_id
         LEFT JOIN genre as g ON gs.genre_id = g.genre_id;
```

| film_name      | genre   |
|:---------------|:--------|
| Аэроплан       | Комедия |
| Шестой элемент | Комедия |
| Один дома      | Боевик  |


### Получение всего списка друзей

```sql
SELECT *
FROM user
         LEFT JOIN friendship_status as fs ON user.user_id = fs.friend_id;
```

| user\_id | email            | login     | name      | birthday   | friendship\_id | user\_id | friend\_id | is\_confirm |
|:---------|:-----------------|:----------|:----------|:-----------|:---------------|:---------|:-----------|:------------|
| 1        | jumba@script.py  | Ayano     | Дмитрий   | 1990-12-05 | 1              | 1        | 2          | true        |
| 1        | jumba@script.py  | Ayano     | Дмитрий   | 1990-12-05 | 2              | 1        | 3          | true        |
| 2        | jumba@script.py  | Avrisia   | Игорь     | 2005-04-21 | 3              | 2        | 1          | true        |
| 2        | jumba@script.py  | Avrisia   | Игорь     | 2005-04-21 | 4              | 2        | 3          | true        |
| 3        | jumba@script.py  | Rochan    | Александр | 1850-11-10 | 5              | 3        | 1          | true        |
| 3        | jumba@script.py  | Rochan    | Александр | 1850-11-10 | 6              | 3        | 2          | false       |

### Получение списка друзей Александра

```sql
SELECT *
FROM user
WHERE user_id IN (SELECT f.friend_id
                  FROM user as u
                           JOIN friendship_status as fs ON u.user_id = fs.user_id
                  WHERE u.user_id = 3);
```

| user\_id | email           | login  | name      | birthday   |
|:---------|:----------------|:-------|:----------|:-----------|
| 1        | jumba@script.py | Ayano  | Дмитрий   | 1990-12-05 |
