# Sakila Film Catalog — Backend

A Spring Boot REST API serving as the backend for a film catalog application built on the [Sakila sample database](https://dev.mysql.com/doc/sakila/en/). Demonstrates a multi-store architecture combining a relational system of record (MySQL) with a derived search index (Elasticsearch) for full-text search and faceted filtering.

**Live demo:** https://sakila-frontend-production.up.railway.app
**API base:** https://springbootexcercise-production.up.railway.app/api/film
**Frontend repo:** https://github.com/Iliaslosifidis/sakila-frontend-nuxt

---

## Architecture
┌─────────────────┐
│  Nuxt Frontend  │
└────────┬────────┘
│
┌────────▼────────┐
│  Spring Boot    │
│      API        │
└────────┬────────┘
│
┌──────────┴──────────┐
▼                     ▼
MySQL              Elasticsearch
(truth, JPA)        (search index)
▲                     ▲
└─── indexer (CommandLineRunner)
syncs on startup
- **MySQL** holds the canonical Sakila data (1000 films, 200 actors, 16k rentals).
- **Elasticsearch** holds a denormalized read-optimized index of films, populated from MySQL on application startup.
- **Spring Boot API** routes single-entity reads to JPA, list/search/filter reads to Elasticsearch, and writes to MySQL with a follow-up sync to Elasticsearch (dual-write pattern).

This is a small-scale implementation of the **CQRS** (Command Query Responsibility Segregation) pattern: writes go through one path (MySQL), reads through stores best suited to the query type.

---

## Tech Stack

- Java 25 / Spring Boot 4.0.4
- Spring Data JPA / Hibernate ORM 7
- Spring Data Elasticsearch 6 / Elasticsearch 9.2.6
- MySQL 8 (Sakila schema)
- Lombok, Bean Validation
- Maven, Docker
- Deployed on Railway

---

## Endpoints

### Search & filter (Elasticsearch-backed)
- `GET /api/film/search` — combined full-text search and structured filtering. All parameters optional.
    - `q` — text search across title (boost 3x) and description, with AUTO fuzziness for typo tolerance
    - `language` — exact match (e.g. `English`, `Italian`)
    - `priceBracket` — `cheap` (<$2), `standard` ($2–$4), `premium` ($4+)
    - `lengthBracket` — `short` (<90 min), `medium` (90–120 min), `long` (>120 min)
    - `rating` — multi-select from `G`, `PG`, `PG-13`, `R`, `NC-17`
    - Standard `page`, `size`, `sort` Spring Data pagination params
- `GET /api/film/autocomplete?q=...&limit=...` — edge n-gram prefix matching for search-as-you-type

### CRUD (JPA-backed)
- `GET /api/film/{id}` — single film with relationships
- `POST /api/film` — create
- `PUT /api/film/{id}` — update (also re-indexes in Elasticsearch)
- `DELETE /api/film/{id}` — delete (also removes from Elasticsearch index; will fail with 500 on films linked to existing rentals due to FK constraints — by design, demonstrating real referential integrity behavior)

### Legacy JPA filter endpoints
Several JPA-derived filter endpoints (`/rating`, `/rental-duration`, `/rental-bracket`, etc.) remain in the codebase from earlier development. They are superseded by `/search` but kept as historical context showing the project's iteration from per-filter endpoints to a unified Elasticsearch search.

---

## Notable Engineering Decisions

**N+1 query prevention.** Initial `findAll()` triggered ~1000 follow-up queries when accessing lazy `@ManyToMany` relationships in DTO mapping. Solved with `@Query` + `LEFT JOIN FETCH` and `DISTINCT`, collapsing 1001 queries to 1.

**Custom analyzer for autocomplete.** The `title` field is indexed twice via Spring Data ES `@MultiField` — once with the standard analyzer for relevance search, once with a custom edge n-gram analyzer (2–15 grams, lowercase) for prefix matching. The query-time analyzer differs from the index-time analyzer to ensure user input matches the indexed prefixes correctly.

**Boosted multi-match search.** Search queries use `MultiMatchQuery` with `title^3` boost, so title matches outrank description matches. AUTO fuzziness allows ≤2 character edits on longer query terms (typo tolerance) without false-matching short words.

**Bool query composition for unified search + filter.** `must` clauses contribute to BM25 scoring (text search), `filter` clauses are binary include/exclude with no scoring contribution (structured filters). Filters are added conditionally based on which parameters are present, allowing arbitrary combinations.

**Denormalization at index time.** `language_id` from MySQL is resolved to the language name string at index time and stored in the `FilmDocument`, so filter dropdowns can display readable values without an additional lookup.

**MySQL type quirks.** Sakila uses `SMALLINT UNSIGNED` and a MySQL-specific `YEAR` type. Handled via `@Column(columnDefinition = ...)` annotations to keep Spring's schema validator happy without changing the original Sakila DDL.

---

## Running Locally

Prerequisites: Java 25, Maven, MySQL 8 with the Sakila database loaded, Elasticsearch 9.2.6 running on `localhost:9200`.

```bash
mvn spring-boot:run
```

Default `application.properties` falls back to `localhost:3306` and `localhost:9200`. Override via env vars in production: `MYSQL_URL`, `MYSQL_USER`, `MYSQL_PASSWORD`, `ELASTICSEARCH_URI`.

A `Dockerfile` is included for containerized deployment.

---

## Roadmap

- Migration to outbox pattern for MySQL → ES sync (currently dual-write, has consistency gap risks)
- Soft delete to handle FK-constrained film deletion gracefully
- Redis caching for popular detail endpoints
- Replace dual-write with Debezium-based CDC for production-scale sync