package com.vmetl.incy.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table("Sites")
public class Site {
    @Id
    private Long id;
    private String name;

    public Site() {
    }

    public Site(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
