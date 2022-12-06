# cloud-library
The REST app with using Spring Boot, Spring Data (PostgreSQL), Spring Security, JWT, Flyway, Lombok 

# Endpoints

- `for authorized` means you have a `Bearer token` in `Authorization`
- `for unauthorized` means you don't have one

## Authorization

### Registration (only for `unauthorized`)
POST - `/api/auth/register`

Request:
```
{
   "login": "login",
   "password": "password",
   "email": "email@gmail.com"
}
```
Response:
```
{
    "Id": 1,
    "Login": "login",
    "Email": "email@gmail.com",
    "Role": "ROLE_USER",
    "Token": "token"
}
```
Errors:
- User with login 'login' already exists
- Email 'email@gmail.com' already in use

### Login (only for `unauthorized`)
POST - `/api/auth/login`

Request:
```
{
   "login": "login",
   "password": "password"
}
```
Response:
```
{
    "Id": 1,
    "Login": "login",
    "Email": "email@gmail.com",
    "Role": "ROLE_USER",
    "Token": "token"
}
```
Errors:
- User with login 'login' not found
- Invalid password

### Get own profile (only for `authorized`)
GET - `/api/auth/profile`

Response:
```
{
    "Id": 1,
    "Login": "login",
    "Email": "email@gmail.com",
    "Role": "ROLE_USER / ROLE_ADMIN"
}
```

## Users

### Create user (only for `admins`)

POST - `/api/users`

Request:
```
{
   "login": "login",
   "password": "password",
   "email": "email@gmail.com",
   "role": {
        "name": "ROLE_USER / ROLE_ADMIN"
   }
}
```
Response:
```
{
    "Id": 1,
    "Login": "login",
    "Email": "email@gmail.com",
    "Role": "ROLE_USER / ROLE_ADMIN"
}
```
Errors:
- User with login 'login' already exists
- Email 'email@gmail.com' already in use

### Get user (for everyone)

GET - `/api/users/id`

Response:
```
{
    "Id": 1,
    "Login": "login",
    "Email": "email@gmail.com",
    "Role": "ROLE_USER / ROLE_ADMIN"
}
```
Errors:
- User with id: 1 doesn't found

### Get users by sort, page, size

GET - `/api/posts`

Query params:
- sort: (`old` or `new`, default: `old`)
- size: default 10
- page: default 0

Response:
```
[
    {
        "Id": 1,
        "Login": "login",
        "Email": "email@gmail.com",
        "Role": "ROLE_USER / ROLE_ADMIN"
    },
    ...
]
```

### Update user (USER can edit only own profile, ADMIN can edit everyone)

PUT - `/api/users/id`

Request (choose field you want to update):
```
{
   "login": "login",
   "password": "password",
   "email": "email@gmail.com",
   "role": {
        "name": "ROLE_USER / ROLE_ADMIN"
   }
}
```
Response: 202
- User with login 'login' already exists (if you edit login)

### Delete user (only authorized, USER can delete only own profile, ADMIN can delete USER, but can't delete another ADMIN)

DELETE - `/api/users/id`

Response: 204

Errors:
- User with id: 1 doesn't found (f.e.)

## Posts

### Create post (only for `authorized`)

POST - `/api/posts`

Request:
```
{
   "title": "title",
   "content": "content"
}
```
Response:
```
{
    "Id": 1,
    "User id": 1,
    "User login": "login",
    "Title": "title",
    "Content": "content",
    "Publish date": 1670269955022 (time in msc)
}
```
Errors:
- You already have a post with the title 'title'

### Get post by id (for everyone)

GET - `/api/posts/id`

Response:
```
{
    "Id": 1,
    "User id": 1,
    "User login": "login",
    "Title": "title",
    "Content": "content",
    "Publish date": 1670269955022 (time in msc)
}
```
Errors:
- Post with id: 1 doesn't found (f.e.)

### Get posts by sort, page, size

GET - `/api/posts`

Query params:
- sort: (`old` or `new`, default: `old`)
- size: default 10
- page: default 0

Response:
```
[
    {
        "Id": 1,
        "User id": 1,
        "User login": "login",
        "Title": "title",
        "Content": "content",
        "Publish date": 1670269955022 (time in msc)
    },
    ...
]
```

### Get posts by sort, page, size for the user

GET - `/api/users/1/posts`

Query params:
- sort: (`old` or `new`, default: `old`)
- size: default 10
- page: default 0

Response:
```
[
    {
        "Id": 1,
        "User id": 1,
        "User login": "login",
        "Title": "title",
        "Content": "content",
        "Publish date": 1670269955022 (time in msc)
    },
    ...
]
```

### Update post by id (USER can edit only own posts, ADMIN can edit everyone)

PUT - `/api/posts/1`

Request (choose field you want to update):
```
{
   "title": "title",
   "content": "content"
}
```
Response: 202

Errors:
- You already have a post with the title 'title'

### Delete post (USER can delete only own posts, ADMIN can delete everyone)

DELETE - `/api/posts/1`

Response: 204

Errors:
- Post with id: 1 doesn't found (f.e.)

