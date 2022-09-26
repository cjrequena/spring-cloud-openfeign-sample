seq 1 10 | xargs -n1 -P10 \
curl -X POST localhost:8080/account-service/api/accounts/deposit \
-H 'Content-Type: application/json' \
-H 'Accept-Version: application/vnd.sample-service.v1' \
-d '{"account_id": "b137611f-7494-4a50-b40a-dd25e76dc9ff","amount": "100"}'
