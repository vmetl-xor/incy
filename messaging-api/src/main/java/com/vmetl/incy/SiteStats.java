package com.vmetl.incy;

import java.util.List;

public record SiteStats(String name, Long id, List<WordStats> wordStats) {}
