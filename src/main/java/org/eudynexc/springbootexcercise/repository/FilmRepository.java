package org.eudynexc.springbootexcercise.repository;

import org.eudynexc.springbootexcercise.entities.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {
  Page<Film> findAllByRating(String rating, Pageable pageable);
  Page<Film> findByRentalDuration(Integer rentalDuration, Pageable pageable);
  Page<Film> findByTitleContainingIgnoreCase(String title, Pageable pageable);

  @Query(
          value = "select * from film where rental_rate between :low and :high",
          countQuery = "Select count(*) from film where rental_rate between :low and :high",
          nativeQuery = true)
  Page<Film> findFilmsByPriceBracket(
          @Param("low") BigDecimal low,
          @Param("high") BigDecimal high,
          Pageable pageable);

  @Query(
          value = "select count(*) from inventory where store_id = :storeId and film_id = :filmId",
          nativeQuery = true)
  int countCopiesAtStore(
          @Param("storeId") int storeId,
          @Param("filmId") int filmId);
}
