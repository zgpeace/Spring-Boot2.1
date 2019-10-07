package zgpeace.spring.circulardependency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CircularDependencyApplication {

  public static void main(String[] args) {
    SpringApplication.run(CircularDependencyApplication.class, args);
  }

}
