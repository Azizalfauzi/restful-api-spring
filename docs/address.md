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

Endpoint : DELETE /api/contacts/{idContact}/addresses/{idAddress}

Request header :

X-API-TOKEN : Token (Mandatory)

Response body (Success):

```json
{
  "data": "Ok"
}
```

Response body (Failed):

```json
{
  "data": "Address not found"
}
```

## List Address

Endpoint : GET /api/contacts/{idContact}/addresses

Request header :

X-API-TOKEN : Token (Mandatory)


Response body (Success):
```json
{
  "data": [
    {
      "id": "random string",
      "street": "Jl.Papandayan",
      "city": "Tulungagung",
      "province": "Jawa",
      "country": "Indo",
      "postalCode": "1234321"
    }
  ]
}
```

Response body (Failed):

```json
{
  "data": "Contact not found"
}
```