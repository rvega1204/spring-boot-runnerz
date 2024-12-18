package dev.ricardovega.runnerz.user;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class UserRestClient {

    private final RestClient restClient; // The RestClient used to make HTTP requests

    // Constructor to initialize the RestClient with a custom builder
    public UserRestClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com/") // Set the base URL for the API
                .build(); // Build the RestClient instance
    }

    // Method to fetch all users from the external API
    public List<User> findAll() {
        // Perform a GET request to "/users" endpoint and map the response body to a list of User objects
        return restClient.get()
                .uri("/users") // Define the URI for fetching users
                .retrieve() // Execute the HTTP request
                .body(new ParameterizedTypeReference<List<User>>() {}); // Convert the response body to a list of User objects
    }

    // Method to fetch a user by their ID from the external API
    public User findById(Integer id) {
        // Perform a GET request to "/users/{id}" endpoint, substituting the ID value
        return restClient.get()
                .uri("/users/{id}", id) // Define the URI with a dynamic ID parameter
                .retrieve() // Execute the HTTP request
                .body(User.class); // Convert the response body to a single User object
    }
}
