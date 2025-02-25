package com.howtodoinjava.app;

import com.howtodoinjava.app.entity.Employee;
import com.howtodoinjava.app.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class EmployeeRestApiApplication implements CommandLineRunner {

  @Autowired
  EmployeeRepository employeeRepository;

  public static void main(String[] args) {
    SpringApplication.run(EmployeeRestApiApplication.class, args);
  }

  @Override
  @Transactional(propagation=Propagation.REQUIRED, readOnly=false)
  public void run(String... args) throws Exception {
    employeeRepository.save(
        new Employee("Lokesh", "123456", "admin@howtodoinjava", "Author", "Java Learner")
    );

    employeeRepository.save(
        new Employee("Alex", "999999", "info@howtodoinjava", "Author", "Another Java Learner")
    );
  }
}
