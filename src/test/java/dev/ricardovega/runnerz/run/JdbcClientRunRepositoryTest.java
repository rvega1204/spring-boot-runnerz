package dev.ricardovega.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest // Marks the test class as a JDBC test that provides an embedded database for testing
@Import(JdbcClientRunRepository.class) // Imports the JdbcClientRunRepository to be tested
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Ensures that the existing database configuration is used, not replaced with an embedded one
class JdbcClientRunRepositoryTest {

    @Autowired
    JdbcClientRunRepository repository; // Injects the JdbcClientRunRepository to be tested

    // Setup method to initialize the repository with sample data before each test
    @BeforeEach
    void setUp() {
        repository.create(new Run(1, // Create and add a Run instance to the repository with ID 1
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                3,
                Location.INDOOR, null));

        repository.create(new Run(2, // Create and add another Run instance with ID 2
                "Wednesday Evening Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60),
                6,
                Location.INDOOR, null));
    }

    // Test case to check if all runs are correctly fetched from the database
    @Test
    void shouldFindAllRuns() {
        List<Run> runs = repository.findAll(); // Retrieve all runs from the repository
        assertEquals(2, runs.size()); // Assert that there are 2 runs in the repository
    }

    // Test case to check if a run with a valid ID is correctly found
    @Test
    void shouldFindRunWithValidId() {
        var run = repository.findById(1).get(); // Retrieve the run by its ID
        assertEquals("Monday Morning Run", run.title()); // Assert that the title is correct
        assertEquals(3, run.miles()); // Assert that the miles value is correct
    }

    // Test case to check if trying to find a run with an invalid ID returns an empty Optional
    @Test
    void shouldNotFindRunWithInvalidId() {
        var run = repository.findById(3); // Try to find a run with an invalid ID
        assertTrue(run.isEmpty()); // Assert that the result is empty, meaning no run was found
    }

    // Test case to check if a new run is created and added correctly to the database
    @Test
    void shouldCreateNewRun() {
        repository.create(new Run(3, // Create and add a new run with ID 3
                "Friday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                3,
                Location.INDOOR, null));
        List<Run> runs = repository.findAll(); // Retrieve all runs after adding the new one
        assertEquals(3, runs.size()); // Assert that there are now 3 runs in the repository
    }

    // Test case to check if an existing run is updated correctly
    @Test
    void shouldUpdateRun() {
        repository.update(new Run(1, // Update the run with ID 1
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
        repository.delete(1); // Delete the run with ID 1
        List<Run> runs = repository.findAll(); // Retrieve all runs after deletion
        assertEquals(1, runs.size()); // Assert that there is now only 1 run left in the repository
    }
}