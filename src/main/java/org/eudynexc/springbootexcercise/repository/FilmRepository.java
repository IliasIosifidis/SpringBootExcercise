package org.eudynexc.springbootexcercise.repository;

import org.eudynexc.springbootexcercise.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {
  List<Film> findAllByRating(String rating);
  List<Film> findByRentalDuration(Integer rentalDuration);

  @Query(value = "select * from film where rental_rate between :low and :high", nativeQuery = true)
  List<Film> findFilmsVyPriceBracket(@Param("low") Integer low,@Param("high") Integer high);

  @Query(value = "select f.* from film f join inventory i on f.film_id = i. film_id where i.store_id = :storeId and f.film_id = :filmId", nativeQuery = true)
  List<Film> findFilmsPerStore(@Param("storeId") int storeId, @Param("filmId") int filmId);
}
