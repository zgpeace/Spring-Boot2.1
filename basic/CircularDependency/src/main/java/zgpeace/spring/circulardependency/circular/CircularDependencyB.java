package zgpeace.spring.circulardependency.circular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zgpeace.spring.circulardependency.circular.CircularDependencyA;

@Component
public class CircularDependencyB {

  private CircularDependencyA circA;

  private String message = "Hi!";

  //@Autowired
  //public CircularDependencyB(CircularDependencyA circA) {
  //  this.circA = circA;
  //}

  // 3. Use Setter/Field Injection
  //@Autowired
  //public void setCircA(CircularDependencyA circA) {
  //  this.circA = circA;
  //}
  //
  //public String getMessage() {
  //  return message;
  //}

  // 4. Use @PostConstruct
  //public void setCircA(CircularDependencyA circA) {
  //  this.circA = circA;
  //}
  //
  //public String getMessage() {
  //  return message;
  //}

  // 5. Implement ApplicationContextAware and InitializingBean
  @Autowired
  public void setCircA(CircularDependencyA circA) {
    this.circA = circA;
  }

  public String getMessage() {
    return message;
  }
}
