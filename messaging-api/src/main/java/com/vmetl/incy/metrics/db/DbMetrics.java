package com.vmetl.incy.metrics.db;

import java.util.OptionalInt;

public interface DbMetrics {
    OptionalInt wordsCount();
    OptionalInt sitesCount();
}
