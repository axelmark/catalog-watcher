package ru.example.catalogwatcherdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.example.catalogwatcherdb.entity.Catalog;

@Repository
@Component
public interface CatalogRepository extends JpaRepository<Catalog, Integer> {
}