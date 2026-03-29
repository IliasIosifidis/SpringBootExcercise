package org.eudynexc.springbootexcercise.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eudynexc.springbootexcercise.entities.dto.FilmDto;
import org.eudynexc.springbootexcercise.service.FilmService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/film")
@RequiredArgsConstructor
public class FilmController {

  private FilmService filmService;

  @GetMapping
  public ResponseEntity<List<FilmDto>> findAllFilms(){
    return ResponseEntity.ok(filmService.findAll());
  }

  @GetMapping("/rating")
  public ResponseEntity<List<FilmDto>> findByRating(@RequestParam String rating){
    return ResponseEntity.ok(filmService.findAllByRating(rating));
  }

  @GetMapping("/rental-duration")
  public ResponseEntity<List<FilmDto>> findByRentalDuration(@RequestParam Integer duration){
    return ResponseEntity.ok(filmService.findByRentalDuration(duration));
  }

  @GetMapping("/{id}")
  public ResponseEntity<FilmDto> findById(@PathVariable Integer id){
    return ResponseEntity.ok(filmService.findById(id));
  }

  @PostMapping
  public ResponseEntity<FilmDto> addFilm(@Valid @RequestBody FilmDto filmDto){
    return ResponseEntity.status(HttpStatus.CREATED).body(filmService.addFilm(filmDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@RequestParam int id){
    filmService.deleteFilmById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<FilmDto> updateFilm(@RequestParam int id, @Valid @RequestBody FilmDto filmDto){
    return ResponseEntity.ok(filmService.updateFilm(id, filmDto));
  }

  @GetMapping("/rental-braket")
  public ResponseEntity<List<FilmDto>> filmPriceBracket(@RequestParam int low,@RequestParam int high){
    return ResponseEntity.ok(filmService.filmsPriceBracket(low, high));
  }

  @GetMapping("/films-per-store")
  public ResponseEntity<List<FilmDto>> findFilmsPerStore(@RequestParam int storeId,@RequestParam int filmId){
    return ResponseEntity.ok(filmService.findFilmsPerStore(storeId,filmId));
  }
}
