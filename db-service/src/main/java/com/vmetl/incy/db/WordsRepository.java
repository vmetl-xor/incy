package com.vmetl.incy.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WordsRepository {
    @Modifying
    @Query("insert into words(value) values (:word) on conflict do nothing")
    int updateWords(String word);
}
