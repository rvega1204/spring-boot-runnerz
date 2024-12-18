package dev.ricardovega.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryRunRepositoryTest {

    InMemoryRunRepository repository;

    // Setup method to initialize the repository and add two runs for testing
    @BeforeEach
    void setUp() {
        repository = new InMemoryRunRepository(); // Initialize the repository
        repository.create(new Run(1, // Create and add a Run instance to the repository
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                3,
                Location.INDOOR, null));

        repository.create(new Run(2, // Create and add another Run instance
                "Wednesday Evening Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60),
                6,
                Location.INDOOR, null));
    }

    // Test case to check if all runs are fetched correctly
    @Test
    void shouldFindAllRuns() {
        List<Run> runs = repository.findAll(); // Retrieve all runs from the repository
        assertEquals(2, runs.size()); // Assert that there are 2 runs in the repository
    }

    // Test case to check if a run with a valid ID is found
    @Test
    void shouldFindRunWithValidId() {
        var run = repository.findById(1).get(); // Retrieve a run by its ID
        assertEquals("Monday Morning Run", run.title()); // Assert that the title is correct
        assertEquals(3, run.miles()); // Assert that the miles value is correct
    }

    // Test case to check if trying to find a run with an invalid ID throws the expected exception
    @Test
    void shouldNotFindRunWithInvalidId() {
        RunNotFoundException notFoundException = assertThrows( // Assert that the exception is thrown
                RunNotFoundException.class,
                () -> repository.findById(3).get() // Try to retrieve a run with an invalid ID
        );

        assertEquals("Run Not Found", notFoundException.getMessage()); // Assert that the exception message is correct
    }

    // Test case to check if a new run is created and added correctly to the repository
    @Test
    void shouldCreateNewRun() {
        repository.create(new Run(3, // Create and add a new run
                "Friday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                3,
                Location.INDOOR, null));
        List<Run> runs = repository.findAll(); // Retrieve all runs
        assertEquals(3, runs.size()); // Assert that the number of runs is now 3
    }

    // Test case to check if an existing run is updated correctly
    @Test
    void shouldUpdateRun() {
        repository.update(new Run(1, // Update an existing run
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                5,
                Location.OUTDOOR, null), 1);
        var run = repository.findById(1).get(); // Retrieve the updated run by its ID
        assertEquals("Monday Morning Run", run.title()); // Assert that the title remains the same
        assertEquals(5, run.miles()); // Assert that the miles value has been updated
        assertEquals(Location.OUTDOOR, run.location()); // Assert that the location has been updated
    }

    // Test case to check if a run is deleted correctly
    @Test
    void shouldDeleteRun() {
        repository.delete(1); // Delete a run by its ID
        List<Run> runs = repository.findAll(); // Retrieve all runs after deletion
        assertEquals(1, runs.size()); // Assert that there is now only 1 run left in the repository
    }
}