# USER API Spec

## Register user

Endpoint : POST /api/users

Request body : 

```json
{
  "username": "zuha",
  "password": "test123",
  "name": "Aziz Alfauzi"
}      
```

Response body (Success):

```json
{
  "data": "OK"
}      
```

Response body (Failed):

```json
{
  "errors": "username must not blank"
}      
```

## Login user

Endpoint : POST /api/auth/login

Request body :

```json
{
  "username": "zuha",
  "password": "test123"
}      
```

Response body (Success):

```json
{
  "data": {
    "token": "token",
    "expiredAt": 123121213
  }
}      
```

Response body (Failed,401):

```json
{
  "errors": "username or password is wrong"
}      
```

## Get user

Endpoint : GET /api/users/current

Request header :

X-API-TOKEN : Token (Mandatory)

Response body (Success):

```json
{
  "data": {
    "username": "zuha",
    "name": "Aziz Alfauzi"
  }
}      
```

Response body (Failed,401):

```json
{
  "errors": "Unauthorized"
}      
```

## Update user

Endpoint : PATCH /api/users/current

Request header :

X-API-TOKEN : Token (Mandatory)

Request body (Parsial):

```json
{
  "name": "Aziz Alfa",
  "password": "test231"
}      
```

Response body (Success):

```json
{
  "data": {
    "username": "zuha",
    "name": "Aziz Alfauzi"
  }
}      
```

Response body (Failed,401):

```json
{
  "errors": "Unauthorized"
}      
```

## Logout user

Endpoint : DELETE /api/auth/logout

Request header :

X-API-TOKEN : Token (Mandatory)

```json
{
  "data": "Ok"
}      
```