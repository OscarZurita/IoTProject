# IoT Plant Monitoring System Backend

A Spring Boot backend application for an IoT plant monitoring system. This system collects data from sensors (humidity, light, temperature) and controls a water pump system for automated plant care.

## Features

- Sensor data collection and storage
- Weather API integration
- RESTful API endpoints for data access
- Automated scheduling for weather updates
- MariaDB database integration

## Prerequisites

- Java 21
- Maven
- MariaDB
- Docker (optional, for containerized database)

## Setup

1. Clone the repository
```bash
git clone [your-repo-url]
cd iot-backend
```

2. Configure the database
- Create a MariaDB database named `iot_plant`
- Create a user with appropriate permissions
- Update `src/main/resources/application.properties` with your database credentials

3. Configure Weather API
- Get an API key from [WeatherAPI.com](https://www.weatherapi.com/)
- Update `weather.api.key` in `application.properties`

4. Build and run the application
```bash
./mvnw spring-boot:run
```

## API Endpoints

- `POST /api/sensor-data`: Submit new sensor data
- `GET /api/sensor-data`: Retrieve all sensor data
- `GET /api/sensor-data/{id}`: Retrieve specific sensor data by ID

## Development

The project uses:
- Spring Boot 3.4.5
- Spring Data JPA
- MariaDB
- Maven

## License

[Your chosen license] 