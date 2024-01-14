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

Endpoint : POST /api/auth.login

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

## Update user

## Get user

## Logout user