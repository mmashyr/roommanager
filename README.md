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

TODO:

1)Controller-level validations
2)Business logic level validations
3)Unit tests for everything :)
4)Separate integration tests layer from unit tests layer
5)Add abstraction to run the chain of bookers instead of using a loop and having it autowired with @Qualifier("baseHotelChain") -
 mechanism to get booker chain for specific hotel