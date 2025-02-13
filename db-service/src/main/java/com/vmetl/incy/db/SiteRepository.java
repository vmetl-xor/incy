package com.vmetl.incy.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteRepository extends CrudRepository<Site, Long> {

    @Modifying
    @Query("insert into sites(name) values (:name) on conflict (name) do nothing")
    boolean addSite(@Param("name") String name);

    @Query("select id from sites where name = :name")
    Optional<Integer> getSiteIdByName(@Param("name") String name);

}
