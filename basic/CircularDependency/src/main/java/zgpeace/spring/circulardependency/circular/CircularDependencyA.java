package zgpeace.spring.circulardependency.circular;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
//public class CircularDependencyA {

  //private CircularDependencyB circB;

  /* 1.
BeanCurrentlyInCreationException: Error creating bean with name 'circularDependencyA':
Requested bean is currently in creation: Is there an unresolvable circular reference?
   */
  //@Autowired
  //public CircularDependencyA(CircularDependencyB circB) {
  //  this.circB = circB;
  //}

  // 2. Use @Lazy

  //@Autowired
  //public CircularDependencyA(@Lazy CircularDependencyB circB) {
  //  this.circB = circB;
  //}


  // 3. Use Setter/Field Injection
  //@Autowired
  //public void setCircB(CircularDependencyB circB) {
  //  this.circB = circB;
  //}
  //
  //public CircularDependencyB getCircB() {
  //  return circB;
  //}


  // 4. Use @PostConstruct
  //@Autowired
  //private CircularDependencyB circB;
  //
  //@PostConstruct
  //public void init() {
  //  circB.setCircA(this);
  //}
  //
  //public CircularDependencyB getCircB() {
  //  return circB;
  //}
//}

// 5. Implement ApplicationContextAware and InitializingBean
@Component
public class CircularDependencyA implements ApplicationContextAware, InitializingBean {

  private CircularDependencyB circB;

  private ApplicationContext context;

  public CircularDependencyB getCircB() {
    return circB;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    circB = context.getBean(CircularDependencyB.class);
  }
}
