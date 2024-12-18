package dev.ricardovega.runnerz.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RunController.class) // Specifies that only the RunController and related components will be tested, using mock beans for the repository
class RunControllerTest {

    @Autowired
    MockMvc mvc; // Used to send HTTP requests and verify responses

    @Autowired
    ObjectMapper objectMapper; // Used for serializing and deserializing JSON content

    @MockBean
    RunRepository repository; // Mocked repository to simulate database interactions

    private final List<Run> runs = new ArrayList<>(); // A list to hold mock run data

    // Setup method to initialize test data before each test
    @BeforeEach
    void setUp() {
        // Adding a sample run object to the list
        runs.add(new Run(1,
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                5,
                Location.INDOOR, null
        ));
    }

    // Test case to check if all runs are correctly fetched
    @Test
    void shouldFindAllRuns() throws Exception {
        // Mocking the behavior of the repository to return the predefined list of runs
        when(repository.findAll()).thenReturn(runs);

        // Performing a GET request to /api/runs and verifying the response status and body
        mvc.perform(get("/api/runs"))
                .andExpect(status().isOk()) // Asserts that the HTTP status is 200 OK
                .andExpect(jsonPath("$.length()").value(runs.size())); // Asserts that the response contains the correct number of runs
    }

    // Test case to check if a specific run is fetched by its ID
    @Test
    void shouldFindOneRun() throws Exception {
        Run run = runs.get(0); // Retrieve the first run in the list

        // Mocking the repository to return the run for any integer ID
        when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(run));

        // Performing a GET request to /api/runs/1 and verifying the response content
        mvc.perform(get("/api/runs/1"))
                .andExpect(status().isOk()) // Asserts that the HTTP status is 200 OK
                .andExpect(jsonPath("$.id", is(run.id()))) // Verifies that the run ID in the response matches
                .andExpect(jsonPath("$.title", is(run.title()))) // Verifies that the title in the response matches
                .andExpect(jsonPath("$.miles", is(run.miles()))) // Verifies the miles in the response
                .andExpect(jsonPath("$.location", is(run.location().toString()))); // Verifies the location in the response
    }

    // Test case to check if an invalid run ID returns a 404 Not Found status
    @Test
    void shouldReturnNotFoundWithInvalidId() throws Exception {
        // Performing a GET request to an invalid run ID and asserting that the status is 404 Not Found
        mvc.perform(get("/api/runs/99"))
                .andExpect(status().isNotFound()); // Asserts that the response status is 404
    }

    // Test case to check if a new run can be successfully created
    @Test
    void shouldCreateNewRun() throws Exception {
        var run = new Run(null,"test", LocalDateTime.now(), LocalDateTime.now(), 1, Location.INDOOR, null); // Create a new run instance

        // Performing a POST request to /api/runs with the run data as JSON in the request body
        mvc.perform(post("/api/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(run)) // Serializes the run object into a JSON string
                )
                .andExpect(status().isCreated()); // Asserts that the status code is 201 Created
    }

    // Test case to check if an existing run can be updated successfully
    @Test
    void shouldUpdateRun() throws Exception {
        var run = new Run(null,"test", LocalDateTime.now(), LocalDateTime.now(), 1, Location.INDOOR, null); // Create a new run instance for update

        // Performing a PUT request to /api/runs/1 to update the run with ID 1
        mvc.perform(put("/api/runs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(run)) // Serializes the run object into a JSON string
                )
                .andExpect(status().isNoContent()); // Asserts that the status code is 204 No Content, indicating a successful update
    }

    // Test case to check if a run can be deleted successfully
    @Test
    public void shouldDeleteRun() throws Exception {
        // Mocking the repository to return true when checking if the run exists
        when(repository.existsById(1)).thenReturn(true);

        // Mocking the repository to do nothing when deleting a run
        doNothing().when(repository).deleteById(1);

        // Performing a DELETE request to /api/runs/1 to delete the run with ID 1
        mvc.perform(delete("/api/runs/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Asserts that the status code is 204 No Content, indicating a successful deletion
    }
}