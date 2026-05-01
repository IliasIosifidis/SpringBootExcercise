package org.eudynexc.springbootexcercise.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eudynexc.springbootexcercise.entities.dto.FilmDto;
import org.eudynexc.springbootexcercise.search.FilmDocument;
import org.eudynexc.springbootexcercise.search.FilmSearchService;
import org.eudynexc.springbootexcercise.service.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/film")
@RequiredArgsConstructor
public class FilmController {

  private final FilmService filmService;
  private final FilmSearchService filmSearchService;

  @GetMapping
  public ResponseEntity<Page<FilmDto>> findAllFilms(
          Pageable pageable
  ) {
    return ResponseEntity.ok(filmService.findAll(pageable));
  }

  @GetMapping("/rating")
  public ResponseEntity<Page<FilmDto>> findByRating(
          @RequestParam String rating,
          Pageable pageable) {
    return ResponseEntity.ok(filmService.findAllByRating(rating, pageable));
  }

  @GetMapping("/rental-duration")
  public ResponseEntity<Page<FilmDto>> findByRentalDuration(
          @RequestParam Integer duration,
          Pageable pageable) {
    return ResponseEntity.ok(filmService.findByRentalDuration(duration, pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<FilmDto> findById(@PathVariable Integer id) {
    return ResponseEntity.ok(filmService.findById(id));
  }

  @PostMapping
  public ResponseEntity<FilmDto> addFilm(@Valid @RequestBody FilmDto filmDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(filmService.addFilm(filmDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@RequestParam int id) {
    filmService.deleteFilmById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<FilmDto> updateFilm(@RequestParam int id, @Valid @RequestBody FilmDto filmDto) {
    return ResponseEntity.ok(filmService.updateFilm(id, filmDto));
  }

  @GetMapping("/rental-braket")
  public ResponseEntity<Page<FilmDto>> filmPriceBracket(
          @RequestParam BigDecimal low,
          @RequestParam BigDecimal high,
          Pageable pageable) {
    return ResponseEntity.ok(filmService.filmsPriceBracket(low, high, pageable));
  }

  @GetMapping("/films-per-store")
  public ResponseEntity<Integer> countCopiesAtStore(
          @RequestParam int storeId,
          @RequestParam int filmId) {
    return ResponseEntity.ok(filmService.countCopiesAtStore(storeId, filmId));
  }

  @GetMapping("/search")
  public ResponseEntity<Page<FilmDocument>> searchByTitle(
          @RequestParam String title,
          Pageable pageable
  ) {
    return ResponseEntity.ok(filmSearchService.search(title, pageable));
  }
}
