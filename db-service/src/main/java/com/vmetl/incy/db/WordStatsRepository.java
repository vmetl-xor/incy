package com.vmetl.incy.db;

import java.util.Map;

public interface WordStatsRepository {

    boolean updateWordsStats(Map<String, Integer> dictionary);

}
