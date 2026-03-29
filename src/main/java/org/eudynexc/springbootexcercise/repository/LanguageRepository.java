package org.eudynexc.springbootexcercise.repository;

import org.eudynexc.springbootexcercise.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
//  Language findAllById(@NotNull(message = "language is required") Integer languageId);
}
