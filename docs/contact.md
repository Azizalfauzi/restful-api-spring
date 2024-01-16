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
Endpoint :

Request header :

X-API-TOKEN : Token (Mandatory)

Request Body :

Response Body (Success) :

Response Body (Failed) :


## Get Contact
Endpoint :

Request header :

X-API-TOKEN : Token (Mandatory)

Request Body :

Response Body (Success) :

Response Body (Failed) :


## Search Contact
Endpoint :

Request header :

X-API-TOKEN : Token (Mandatory)

Request Body :

Response Body (Success) :

Response Body (Failed) :


## Remove Contact
Endpoint :

Request header :

X-API-TOKEN : Token (Mandatory)

Request Body :

Response Body (Success) :

Response Body (Failed) :
