package org.eudynexc.springbootexcercise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@Slf4j
@EnableElasticsearchRepositories(basePackages = "org.eudynexc.springbootexcercise.search")
public class SpringBootExerciseApplication {

  static void main(String[] args) {
    SpringApplication.run(SpringBootExerciseApplication.class, args);
    log.info("Systems are up and running");
  }

}
