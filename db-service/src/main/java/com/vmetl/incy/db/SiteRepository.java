package com.vmetl.incy.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends CrudRepository<Site, Long> {



    @Modifying
    @Query("insert into sites(name) values (:name)")
    boolean addSite(@Param("name") String name);
}
