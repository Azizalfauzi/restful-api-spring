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
  "data": "KO",
  "errors": "username must not blank"
}      
```

## Login user

## Update user

## Get user

## Logout user