package com.saphsys.string.batchfilter;

public class BeginStringFilter implements StringFilter {
    private final int length;

    public BeginStringFilter(int length) {
        this.length = length;
    }

    @Override
    public String filter(String source) {
        return source.substring(0, length);
    }
}
