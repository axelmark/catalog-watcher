package ru.example.catalogwatcherdb.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "catalog")
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String path;
}
