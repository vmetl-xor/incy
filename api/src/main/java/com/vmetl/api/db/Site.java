package com.vmetl.api.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Sites")
public class Site {
    @Id
    private Long id;
    private String name;

}
