package org.eudynexc.springbootexcercise.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eudynexc.springbootexcercise.entities.Film;
import org.eudynexc.springbootexcercise.repository.FilmRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FIlmIndexer implements CommandLineRunner {

  private final FilmRepository filmRepository;
  private final FilmSearchRepository filmSearchRepository;

  @Override
  public void run(String... args) throws Exception {
    log.info("Starting film indexing into ElasticSearch...");

    long existingCount = filmSearchRepository.count();
    if (existingCount > 0){
      log.info("Index already populated ({} documents), skipping reindex.", existingCount);
      return;
    }

    List<Film> films = filmRepository.findAll();
    List<FilmDocument> documents = films.stream()
            .map(this::toDocument)
            .toList();

    filmSearchRepository.saveAll(documents);
    log.info("Indexed {} films into ElasticSearch.", documents.size());
  }

  private FilmDocument toDocument(Film film) {
    return FilmDocument.builder()
            .id(film.getFilmId())
            .title(film.getTitle())
            .description(film.getDescription())
            .rating(film.getRating())
            .releaseYear(film.getReleaseYear() != null? film.getReleaseYear().intValue() : null)
            .build();
  }
}
