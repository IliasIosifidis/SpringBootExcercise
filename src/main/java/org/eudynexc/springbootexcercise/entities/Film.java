package org.eudynexc.springbootexcercise.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "film")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Film {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private int filmId;

  @Column(length = 255, nullable = false)
  private String title;

  @Column(columnDefinition = "text")
  private String description;

  @Column(columnDefinition = "year")
  private Short releaseYear;

  @JoinColumn(name = "language_id")
  @ManyToOne
  private Language language;

  @JoinColumn(name = "original_language_id")
  @ManyToOne
  private Language originalLanguage;

  @Column(columnDefinition = "tinyint unsigned", nullable = false)
  private int rentalDuration;

  @Column(nullable = false)
  private BigDecimal rentalRate;

  @Column(columnDefinition = "smallint unsigned")
  private Integer length;

  @Column(nullable = false)
  private BigDecimal replacementCost;

  @Column
  private String rating;

  @Column
  private String specialFeatures;

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime lastUpdate;

  @ManyToMany
  @JoinTable(
          name = "film_actor",
          joinColumns = @JoinColumn(name = "film_id"),
          inverseJoinColumns = @JoinColumn(name = "actor_id")
  )
  private List<Actor> actors;

  @ManyToMany
  @JoinTable(
          name = "film_category",
          joinColumns = @JoinColumn(name = "film_id"),
          inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private List<Category> categories;
}



































