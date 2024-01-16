# Addrees API Spec

## Create Address

Endpoint : POST /api/contacts/{idContact}/addresses

Request header :

X-API-TOKEN : Token (Mandatory)

Request body :

```json
{
  "street": "Jl.Papandayan",
  "city": "Tulungagung",
  "province": "Jawa",
  "country": "Indo",
  "postalCode": "1234321"
}
```

Response body (Success):

```json
{
  "data": {
    "id": "random string",
    "street": "Jl.Papandayan",
    "city": "Tulungagung",
    "province": "Jawa",
    "country": "Indo",
    "postalCode": "1234321"
  }
}
```

Response body (Failed):

```json
{
  "errors": "Contact is not found"
}
```

## Update Address

Endpoint : PUT /api/contacts/{idContact}/adresses/{idAddress}

Request header :

X-API-TOKEN : Token (Mandatory)

Request body :

```json
{
  "street": "Jl.Papandayan",
  "city": "Tulungagung",
  "province": "Jawa",
  "country": "Indo",
  "postalCode": "1234321"
}
```

Response body (Success):

```json
{
  "data": {
    "id": "random string",
    "street": "Jl.Papandayan",
    "city": "Tulungagung",
    "province": "Jawa",
    "country": "Indo",
    "postalCode": "1234321"
  }
}
```

Response body (Failed):

```json
{
  "errors": "Address is not found"
}
```

## Get Address

Endpoint : GET /api/contacts/{idContact}/addresses/{idAddress}

Request header :

X-API-TOKEN : Token (Mandatory)

Response body (Success):

```json
{
  "data": {
    "id": "random string",
    "street": "Jl.Papandayan",
    "city": "Tulungagung",
    "province": "Jawa",
    "country": "Indo",
    "postalCode": "1234321"
  }
}
```

Response body (Failed):

```json
{
  "errors": "Address is not found"
}
```

## Remove Address

Endpoint :

Request header :

X-API-TOKEN : Token (Mandatory)

Request body :

Response body (Success):

Response body (Failed):

## List Address

Endpoint :

Request header :

X-API-TOKEN : Token (Mandatory)

Request body :

Response body (Success):

Response body (Failed):