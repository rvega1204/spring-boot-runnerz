# Run Management System

This project is a Run Management System built using Spring Boot. It provides RESTful APIs to manage running events with various CRUD operations. The project integrates with a PostgreSQL database, uses Docker for containerization, and includes comprehensive testing for all components.

## Technologies Used

- **Spring Framework**: Used to build the application, manage dependencies, and configure beans.
- **Spring Boot**: Simplifies application setup and provides a production-ready framework.
- **Spring Web**: Used to create RESTful web services to interact with the system.
- **Spring Data JPA**: For interacting with the PostgreSQL database.
- **Spring Test**: Provides a framework for writing and executing tests (unit, integration).
- **RestClient**: To perform RESTful operations for external API calls.
- **PostgreSQL**: The relational database used to persist data.
- **Docker**: Used for containerization of the application and PostgreSQL database.

## Setup and Configuration

### Prerequisites

- Java 17 or later
- Docker
- PostgreSQL

### Running the Application Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/rvega1204/spring-boot-runnerz.git
   cd RunManagementSystem
   ```
2. Build the application using Maven or Gradle (if not already built)
3. Run the application locally with Spring Boot

### Docker Setup
Ensure Docker is installed on your system.
Run the application and PostgreSQL in Docker containers:
   ```bash
   docker-compose up
   ```
This will set up the application in one container and PostgreSQL in another, ensuring that the two are connected.

### Database Configuration
1. The application uses PostgreSQL for storing run data.
2. Update application.properties or application.yml for the PostgreSQL connection settings, if necessary.

### Accessing the API
1. GET /api/runs: Retrieves a list of all runs.
2. GET /api/runs/{id}: Retrieves a specific run by ID.
3. POST /api/runs: Creates a new run.
4. PUT /api/runs/{id}: Updates an existing run by ID.
5. DELETE /api/runs/{id}: Deletes a run by ID.

### Testing
The project includes several test classes to verify the functionality of the system. Tests include:
1. Unit Tests: Verify the logic of individual components such as repositories and controllers.
2. Integration Tests: Ensure that the components work together, including database interactions and REST endpoints.
3. Controller Tests: Validate the REST API using MockMvc.
4. Database Tests: Ensure proper database interactions and CRUD functionality.

### Example Run Data
The system includes sample run data, which can be preloaded into the database upon startup. If the database is empty, the application will load a JSON file containing mock data.

### API Documentation
1. GET /api/runs: Returns a list of all runs.
2. GET /api/runs/{id}: Retrieves a specific run by ID.
3. POST /api/runs: Creates a new run with a JSON body containing the run details.
4. PUT /api/runs/{id}: Updates a specific run.
5. DELETE /api/runs/{id}: Deletes a specific run.

### Conclusion
This project demonstrates the use of Spring Boot to build a RESTful application with PostgreSQL, testing, and Docker for easy setup. It offers all essential CRUD operations for managing running events and can be easily extended with additional features such as authentication, advanced queries, or integrations with external services.

### ## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
