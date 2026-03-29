package org.eudynexc.springbootexcercise.service;

import org.eudynexc.springbootexcercise.entities.dto.FilmDto;

import java.util.List;

public interface FilmService {
  List<FilmDto> findAll();
  List<FilmDto> findAllByRating(String rating);
  List<FilmDto> findByRentalDuration(Integer rentalDuration);
  FilmDto findById(Integer filmId);
  FilmDto addFilm(FilmDto filmDto);
  void deleteFilmById(int id);
  FilmDto updateFilm(int id, FilmDto filmDto);
  List<FilmDto> filmsPriceBracket(Integer low, Integer high);
  List<FilmDto> findFilmsPerStore(int storeId, int filmId);
}
