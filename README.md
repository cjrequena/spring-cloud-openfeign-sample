seq 1 10 | xargs -n1 -P10 \
curl -X POST localhost:8080/account-service/api/accounts/deposit \
-H 'Content-Type: application/json' \
-H 'Accept-Version: application/vnd.sample-service.v1' \
-d '{"account_id": "233b1eb4-33f4-4317-a21d-adbafb310a28","amount": "100"}'
