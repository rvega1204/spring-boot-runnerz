package dev.ricardovega.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Runs the Spring Boot application with a random port for integration testing
class RunControllerIntTest {

    @LocalServerPort
    int randomServerPort; // Injects the random port allocated for the test server

    RestClient restClient; // Instance of RestClient used to send HTTP requests

    // Setup method that initializes the RestClient with the dynamic random port before each test
    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort); // Initialize the RestClient with the random port
    }

    // Test case to check if all runs are correctly fetched from the API
    @Test
    void shouldFindAllRuns() {
        List<Run> runs = restClient.get() // Send a GET request to /api/runs to fetch all runs
                .uri("/api/runs")
                .retrieve() // Retrieve the response body
                .body(new ParameterizedTypeReference<>() {}); // Parse the response body into a list of Run objects

        assert runs != null; // Assert that the list of runs is not null
        assertEquals(10, runs.size()); // Assert that the expected number of runs (10) are returned
    }

    // Test case to check if a run with a valid ID is correctly retrieved from the API
    @Test
    void shouldFindRunById() {
        Run run = restClient.get() // Send a GET request to /api/runs/1 to fetch the run with ID 1
                .uri("/api/runs/1")
                .retrieve() // Retrieve the response body
                .body(Run.class); // Parse the response body into a Run object

        // Assert that all properties of the fetched run match the expected values
        assertAll(
                () -> assertEquals(1, run.id()),
                () -> assertEquals("Noon Run", run.title()),
                () -> assertEquals("2024-02-20T06:05", run.startedOn().toString()),
                () -> assertEquals("2024-02-20T10:27", run.completedOn().toString()),
                () -> assertEquals(24, run.miles()),
                () -> assertEquals(Location.INDOOR, run.location()));
    }

    // Test case to check if a new run can be successfully created through the API
    @Test
    void shouldCreateNewRun() {
        Run run = new Run(11, "Evening Run", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 10, Location.OUTDOOR, null); // Create a new Run instance

        ResponseEntity<Void> newRun = restClient.post() // Send a POST request to /api/runs to create the new run
                .uri("/api/runs")
                .body(run) // Include the run object in the request body
                .retrieve() // Retrieve the response
                .toBodilessEntity(); // Return the response as an entity without a body

        assertEquals(HttpStatus.CREATED, newRun.getStatusCode()); // Assert that the status code is 201 (Created)
    }

    // Test case to check if an existing run can be updated through the API
    @Test
    void shouldUpdateExistingRun() {
        Run run = restClient.get() // Send a GET request to /api/runs/1 to fetch the run with ID 1
                .uri("/api/runs/1")
                .retrieve()
                .body(Run.class); // Parse the response body into a Run object

        assert run != null; // Assert that the run is not null

        ResponseEntity<Void> updatedRun = restClient.put() // Send a PUT request to /api/runs/1 to update the existing run
                .uri("/api/runs/1")
                .body(run) // Include the updated run object in the request body
                .retrieve() // Retrieve the response
                .toBodilessEntity(); // Return the response as an entity without a body

        assertEquals(HttpStatus.NO_CONTENT, updatedRun.getStatusCode()); // Assert that the status code is 204 (No Content), indicating successful update
    }

    // Test case to check if a run can be deleted through the API
    @Test
    void shouldDeleteRun() {
        ResponseEntity<Void> run = restClient.delete() // Send a DELETE request to /api/runs/1 to delete the run with ID 1
                .uri("/api/runs/1")
                .retrieve() // Retrieve the response
                .toBodilessEntity(); // Return the response as an entity without a body

        assertEquals(HttpStatus.NO_CONTENT, run.getStatusCode()); // Assert that the status code is 204 (No Content), indicating successful deletion
    }
}