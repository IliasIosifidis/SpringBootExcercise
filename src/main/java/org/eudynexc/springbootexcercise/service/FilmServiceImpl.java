package org.eudynexc.springbootexcercise.service;

import lombok.RequiredArgsConstructor;
import org.eudynexc.springbootexcercise.entities.Film;
import org.eudynexc.springbootexcercise.entities.Language;
import org.eudynexc.springbootexcercise.entities.dto.FilmDto;
import org.eudynexc.springbootexcercise.repository.ActorsRepository;
import org.eudynexc.springbootexcercise.repository.FilmRepository;
import org.eudynexc.springbootexcercise.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService{
  private final FilmRepository filmRepository;
  private final LanguageRepository languageRepository;
  private final ActorsRepository actorsRepository;

  public List<FilmDto> findAll(){
    return filmRepository.findAll()
            .stream()
            .map(this::toDto)
            .toList();
  }

  @Override
  public List<FilmDto> findAllByRating(String rating) {
    return filmRepository.findAllByRating(rating)
            .stream()
            .map(this::toDto)
            .toList();
  }

  @Override
  public List<FilmDto> findByRentalDuration(Integer rentalDuration) {
    return filmRepository.findByRentalDuration(rentalDuration)
            .stream()
            .map(this::toDto)
            .toList();
  }

  @Override
  public FilmDto findById(Integer filmId) {
    return filmRepository.findById(filmId)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Film not found"));
  }

  @Override
  public FilmDto addFilm(FilmDto filmDto) {
    Film film = toEntity(filmDto);
    Film saved = filmRepository.save(film);
    return toDto(saved);
  }

  @Override
  public void deleteFilmById(int id) {
    Film film = filmRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("film not found"));
    filmRepository.delete(film);
  }

  @Override
  public FilmDto updateFilm(int id, FilmDto filmDto) {
    Film film = filmRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Film not found"));
    film.setTitle(filmDto.getTitle());
    film.setDescription(filmDto.getDescription());
    film.setRentalRate(filmDto.getRentalRate());
    film.setFilmId(id);
    film.setLength(filmDto.getLength());
    Film saved = filmRepository.save(film);
    return toDto(saved);
  }

  @Override
  public List<FilmDto> filmsPriceBracket(Integer low, Integer high) {
    return filmRepository.findFilmsVyPriceBracket(low, high)
            .stream()
            .map(this::toDto)
            .toList();
  }

  @Override
  public List<FilmDto> findFilmsPerStore(int storeId, int filmId) {
    return filmRepository.findFilmsPerStore(storeId, filmId)
            .stream()
            .map(this::toDto)
            .toList();
  }

  private FilmDto toDto(Film film) {
    FilmDto dto = new FilmDto();
    dto.setTitle(film.getTitle());
    dto.setDescription(film.getDescription());
    dto.setReleaseYear(film.getReleaseYear());
    dto.setLength(film.getLength());
    dto.setRating(film.getRating());
    dto.setReleaseYear(film.getReleaseYear());
    return dto;
  }

  private Film toEntity(FilmDto filmDto){
    Film film = new Film();
    film.setTitle(filmDto.getTitle());
    film.setDescription(filmDto.getDescription());
    film.setReleaseYear(filmDto.getReleaseYear());
    film.setLength(filmDto.getLength());
    film.setRating(filmDto.getRating());
    film.setRentalDuration(filmDto.getRentalDuration());
    film.setRentalRate(filmDto.getRentalRate());
    film.setReplacementCost(filmDto.getReplacementCost());
    // Language
    Optional<Language> language = languageRepository.findById(filmDto.getLanguageId());
    film.setLanguage(language.orElseThrow(() -> new RuntimeException("language not found")));
    Optional<Language> originalLanguage = languageRepository.findById(filmDto.getOriginalLanguageId());
    film.setOriginalLanguage(originalLanguage.orElseThrow(() -> new RuntimeException("Language not found")));
    //
    return film;
  }
}

































