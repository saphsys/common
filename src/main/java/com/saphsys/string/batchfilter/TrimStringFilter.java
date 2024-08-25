package com.saphsys.string.batchfilter;

public class TrimStringFilter implements StringFilter {
    @Override
    public String filter(String source) {
        return source.trim();
    }
}
