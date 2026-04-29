package org.eudynexc.springbootexcercise.service;

import org.eudynexc.springbootexcercise.entities.dto.FilmDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface FilmService {
  Page<FilmDto> findAll(Pageable pageable);
  Page<FilmDto> findAllByRating(String rating, Pageable pageable);
  Page<FilmDto> findByRentalDuration(Integer rentalDuration,Pageable pageable);
  FilmDto findById(Integer filmId);
  FilmDto addFilm(FilmDto filmDto);
  void deleteFilmById(int id);
  FilmDto updateFilm(int id, FilmDto filmDto);
  Page<FilmDto> filmsPriceBracket(BigDecimal low, BigDecimal high, Pageable pageable);
  int countCopiesAtStore(int storeId, int filmId );
}
