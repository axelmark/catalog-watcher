package ru.example.catalogwatcherdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.catalogwatcherdb.entity.Catalog;
import ru.example.catalogwatcherdb.repository.CatalogRepository;

import java.util.Optional;

@Service
public class CatalogService {

    private final CatalogRepository catalogRepository;

    @Autowired
    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public Iterable<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    public Optional<Catalog> getCatalogById(Integer id) {
        return catalogRepository.findById(id);
    }

    public Catalog createCatalog(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    public Catalog updateCatalog(Integer id, Catalog catalog) {
        if (catalogRepository.existsById(id)) {
            catalog.setId(id);
            return catalogRepository.save(catalog);
        } else {
            return null;
        }
    }

    public void deleteCatalog(Integer id) {
        catalogRepository.deleteById(id);
    }
}