package dev.ricardovega.runnerz.run;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/runs") // Maps the controller to the base path "/api/runs"
public class RunController {

    private final RunRepository runRepository;

    // Constructor to inject the RunRepository dependency
    public RunController(RunRepository runRepository) {
        this.runRepository = runRepository;
    }

    // Get all runs from the repository
    @GetMapping("")
    List<Run> findAll() {
        return runRepository.findAll(); // Return the list of all runs
    }

    // Find a specific run by its ID
    @GetMapping("/{id}") // Mapping for GET request with a path variable for the ID
    Run findById(@PathVariable Integer id) {
        // Attempt to find the run by ID
        Optional<Run> run = runRepository.findById(id);
        if (run.isEmpty()) {
            throw new RunNotFoundException(); // Throw a custom exception if not found
        }

        return run.get(); // Return the found run
    }

    // Create a new run
    @ResponseStatus(HttpStatus.CREATED) // Set the response status to 201 (Created)
    @PostMapping("") // Mapping for POST request to create a new run
    void create(@Valid @RequestBody Run run) {
        runRepository.save(run); // Save the run to the repository
    }

    // Update an existing run by its ID
    @ResponseStatus(HttpStatus.NO_CONTENT) // Set the response status to 204 (No Content) for successful update
    @PutMapping("/{id}") // Mapping for PUT request to update a run
    void update(@Valid @RequestBody Run run, @PathVariable Integer id) {
        runRepository.save(run); // Save the updated run to the repository
    }

    // Delete a run by its ID
    @ResponseStatus(HttpStatus.NO_CONTENT) // Set the response status to 204 (No Content) for successful deletion
    @DeleteMapping("/{id}") // Mapping for DELETE request to remove a run
    void delete(@PathVariable Integer id) {
        Optional<Run> run = runRepository.findById(id);
        if (run.isEmpty()) {
            throw new RunNotFoundException(); // Throw a custom exception if run is not found
        }

        runRepository.delete(run.get()); // Delete the run from the repository
    }

    // Find runs by their location
    @GetMapping("/location/{location}") // Mapping for GET request to fetch runs by location
    List<Run> findByLocation(@PathVariable String location) {
        return runRepository.findAllByLocation(location); // Return the list of runs that match the location
    }
}
