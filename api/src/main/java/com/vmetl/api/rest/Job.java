package com.vmetl.api.rest;

public class Job {
    private String url;
    private int depth;

    public Job() {
    }

    public Job(String url, int depth) {
        this.url = url;
        this.depth = depth;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public static Job of(String url, int depth) {
        return new Job(url, depth);
    }

}
