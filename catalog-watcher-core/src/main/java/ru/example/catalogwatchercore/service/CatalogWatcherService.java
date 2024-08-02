package ru.example.catalogwatchercore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.example.catalogwatcherdb.entity.Catalog;
import ru.example.catalogwatcherdb.repository.CatalogRepository;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Component
@EnableAsync
public class CatalogWatcherService {
    private final ExecutorService executorService;
    private final List<String> allowedExtensions;
    Logger logger = Logger.getLogger("catalogWatcherLog");

    private final CatalogRepository catalogRepository;

    @Autowired
    public CatalogWatcherService(@Value("${max-threads}") int maxThreads,
                                 @Value("${allowed-extensions}") String allowedExtensions, CatalogRepository catalogRepository) {
        this.executorService = Executors.newFixedThreadPool(maxThreads);
        this.allowedExtensions = Arrays.asList(allowedExtensions.split(","));
        this.catalogRepository = catalogRepository;
    }

    @Async
    @Scheduled(fixedDelayString = "${scan-interval}")
    public void scanCatalog() {
        List<Catalog> catalogs = catalogRepository.findAll();
        for (Catalog catalog : catalogs) {
            executorService.submit(() -> scanCatalog(catalog.getPath()));
        }
    }

    private void scanCatalog(String path) {
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            logger.warning("Invalid directory: " + path);
            return;
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    processFile(file);
                }
            }
        }
    }

    private void processFile(File file) {
        String extension = getFileExtension(file);
        System.out.println(extension);
        if (!extension.equals("failed")) {
            if (!allowedExtensions.contains(extension)) {
                File newFile = new File(file.getAbsolutePath() + ".failed");
                if (file.renameTo(newFile)) {
                    logger.info("File renamed: " + file.getName() + " -> " + newFile.getName());
                } else {
                    logger.warning("Failed to rename file: " + file.getName());
                }
            } else {
                logger.info("Deleting file: " + file.getName());
                if (!file.delete()) {
                    logger.warning("Failed to delete file: " + file.getName());
                }
            }
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf + 1).toLowerCase();
    }
}
