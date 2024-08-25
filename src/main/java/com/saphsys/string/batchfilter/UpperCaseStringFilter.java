package com.saphsys.string.batchfilter;

public class UpperCaseStringFilter implements StringFilter {
    @Override
    public String filter(String source) {
        return source.toUpperCase();
    }
}
