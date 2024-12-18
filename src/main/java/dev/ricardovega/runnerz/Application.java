package dev.ricardovega.runnerz;

import dev.ricardovega.runnerz.user.User;
import dev.ricardovega.runnerz.user.UserHttpClient;
import dev.ricardovega.runnerz.user.UserRestClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@SpringBootApplication
public class Application {

	private static final Log LOG = LogFactory.getLog(Application.class);

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
		LOG.info("Application changed");
	}

	@Bean
	UserHttpClient userHttpClient() {
		RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com/");
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
		return factory.createClient(UserHttpClient.class);
	}

//	@Bean
//	CommandLineRunner runner(UserHttpClient client) {
//		return args -> {
//			List<User> users = client.findAll();
//			System.out.println(users);
//        };
//	}

}
