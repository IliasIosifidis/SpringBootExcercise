package org.eudynexc.springbootexcercise.service;

import lombok.RequiredArgsConstructor;
import org.eudynexc.springbootexcercise.entities.Film;
import org.eudynexc.springbootexcercise.entities.Language;
import org.eudynexc.springbootexcercise.entities.dto.FilmDto;
import org.eudynexc.springbootexcercise.repository.ActorsRepository;
import org.eudynexc.springbootexcercise.repository.FilmRepository;
import org.eudynexc.springbootexcercise.repository.LanguageRepository;
import org.eudynexc.springbootexcercise.search.FIlmIndexer;
import org.eudynexc.springbootexcercise.util.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService{
  private final FilmRepository filmRepository;
  private final LanguageRepository languageRepository;
  private final ActorsRepository actorsRepository;
  private final FIlmIndexer fIlmIndexer;

  public Page<FilmDto> findAll(Pageable pageable){
    return filmRepository.findAll(pageable)
            .map(this::toDto);
  }

  @Override
  public Page<FilmDto> findAllByRating(String rating, Pageable pageable) {
    return filmRepository.findAllByRating(rating, pageable)
            .map(this::toDto);
  }

  @Override
  public Page<FilmDto> findByRentalDuration(Integer rentalDuration,Pageable pageable) {
    return filmRepository.findByRentalDuration(rentalDuration, pageable)
            .map(this::toDto);

  }

  @Override
  public FilmDto findById(Integer filmId) {
    return filmRepository.findById(filmId)
            .map(this::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Film not found"));
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
            .orElseThrow(() -> new ResourceNotFoundException("film not found"));
    filmRepository.delete(film);
    // ElasticSearch re-indexing on delete
    fIlmIndexer.deleteFilm(id);
  }

  @Override
  public FilmDto updateFilm(int id, FilmDto filmDto) {
    Film film = filmRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Film not found"));
    film.setTitle(filmDto.getTitle());
    film.setDescription(filmDto.getDescription());
    film.setReleaseYear(filmDto.getReleaseYear());
    film.setRentalRate(filmDto.getRentalRate());
    film.setRentalDuration(filmDto.getRentalDuration());
    film.setLength(filmDto.getLength());
    film.setRating(filmDto.getRating());
    film.setFilmId(id);

    Language language = languageRepository.findById(filmDto.getLanguageId())
            .orElseThrow(() -> new ResourceNotFoundException("Language not found"));
    film.setLanguage(language);
    // ElasticSearch Re-indexing on update
    fIlmIndexer.indexFilm(film);

    return toDto(film);
  }

  @Override
  public Page<FilmDto> filmsPriceBracket(BigDecimal low, BigDecimal high, Pageable pageable) {
    return filmRepository.findFilmsByPriceBracket(low, high, pageable)
            .map(this::toDto);
  }

  @Override
  public int countCopiesAtStore(int storeId, int filmId) {
    return filmRepository.countCopiesAtStore(storeId, filmId);
  }

  @Override
  public Page<FilmDto> searchByTitle(String title, Pageable pageable) {
    return filmRepository.findByTitleContainingIgnoreCase(title,pageable)
            .map(this::toDto);
  }

  private FilmDto toDto(Film film) {
    FilmDto dto = new FilmDto();
    dto.setRentalRate(film.getRentalRate());
    dto.setRentalDuration(film.getRentalDuration());
    dto.setReplacementCost(film.getReplacementCost());
    dto.setLanguageId(film.getLanguage().getId());
    dto.setTitle(film.getTitle());
    dto.setDescription(film.getDescription());
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
    film.setLanguage(language.orElseThrow(() -> new ResourceNotFoundException("language not found")));
    Optional<Language> originalLanguage = languageRepository.findById(filmDto.getOriginalLanguageId());
    film.setOriginalLanguage(originalLanguage.orElseThrow(() -> new ResourceNotFoundException("Language not found")));
    //
    return film;
  }
}

































