Register group for gathering data from wall:
- send POST request on /register with body, contains JSON object:
```json
{
    "accessToken": "value",
    "groupId": 1234,
    "secretKey": "optional value"
}
```
- send PUT request on /register with body, contains JSON object:
```json
{
    "confirmationCode": "value",
    "groupId": 1234,
    "secretKey": "optional value"
}
```
