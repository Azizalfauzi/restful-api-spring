# CONTACT API SPEC

## Create Contact

Endpoint : POST /api/contacts

Request header :

X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "firstName": "Aziz",
  "lastName": "Alfa",
  "email": "aziz@gmail.com",
  "phone": "082332"
}    
```

Response Body (Success) :

```json
{
  "data": {
    "id": "random string",
    "firstName": "Aziz",
    "lastName": "Alfa",
    "email": "aziz@gmail.com",
    "phone": "082332"
  }
}    
```

Response Body (Failed) :

```json
{
  "errors": "Email format invalid,phone format invalid, ..."
}    
```

## Update Contact

Endpoint : PUT /api/contacts/{idContact}

Request header :

X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "firstName": "Aziz",
  "lastName": "Alfa",
  "email": "aziz@gmail.com",
  "phone": "082332"
}    
```

Response Body (Success) :

```json
{
  "data": {
    "ic": "random string",
    "firstName": "Aziz",
    "lastName": "Alfa",
    "email": "aziz@gmail.com",
    "phone": "082332"
  }
}    
```

Response Body (Failed) :

```json
{
  "errors": "Email format invalid,phone format invalid, ..."
}    
```

## Get Contact

Endpoint : GET /api/contacts/{idContact}

Request header :

X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data": {
    "ic": "random string",
    "firstName": "Aziz",
    "lastName": "Alfa",
    "email": "aziz@gmail.com",
    "phone": "082332"
  }
}    
```

Response Body (Failed,404) :

```json
{
  "errors": "Contact is not found"
}    
```

## Search Contact

Endpoint : GET /api/contacts

Query param :

- name : String ,contact firstname or lastname,using like query ,optional
- phone : String,contact phone ,using like query,optional
- email : String,contact email,using like query,optional
- page : Integer , start form 0, default 0
- size : Integer, default 10

Request header :

X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data": [
    {
      "ic": "random string",
      "firstName": "Aziz",
      "lastName": "Alfa",
      "email": "aziz@gmail.com",
      "phone": "082332"
    }
  ],
  "paging": {
    "currentPage": 0,
    "totalPage": 10,
    "size": 10
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized"
}    
```

## Remove Contact

Endpoint :DELETE /api/contacts/{idContact}

Request header :

X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data": "Ok"
}    
```

Response Body (Failed) :

```json
{
  "errors": "Contact is not found"
}    
```
