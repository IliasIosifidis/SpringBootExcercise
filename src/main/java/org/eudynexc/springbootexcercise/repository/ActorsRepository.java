package org.eudynexc.springbootexcercise.repository;

import org.eudynexc.springbootexcercise.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorsRepository extends JpaRepository<Actor, Integer> {
}
