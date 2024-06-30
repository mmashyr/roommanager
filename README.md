To run the project execute below commands from the root directory of the project:
`./gradlew clean build && ./gradlew bootRun`

Run tests separately:
`./gradlew clean build && ./gradlew test`

Access the endpoint:
```
curl --location 'http://localhost:8080/hotel/occupancy' \
--header 'Content-Type: application/json' \
--data '[
    {
        "roomType": "ECONOMY",
        "numberOfRooms": 3
    },
        {
        "roomType": "PREMIUM",
        "numberOfRooms": 3
    }
]'
```