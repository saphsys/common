package com.saphsys.string.batchfilter;

import java.util.ArrayList;

public class BatchStringFilter {
    private static final String TRIM_TYPE = "trim";
    private static final String UPPER_TYPE = "upper";
    private static final String BEGIN_TYPE = "begin";
    private static final String REGEX_TYPE = "regex";
    private final ArrayList<StringFilter> workers = new ArrayList<>();


    public BatchStringFilter() {
    }

    public BatchStringFilter(FilterDtoArrayList filters) {
        this();
        filters.forEach(filter -> {
            addFilter(createFilter(filter));
        });
    }

    private String checkNotEmpty(String value) {
        if(value == null || value.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    public StringFilter createFilter(FilterDto filter) {
        switch(filter.type) {
            case TRIM_TYPE: return new TrimStringFilter();
            case UPPER_TYPE: return new UpperCaseStringFilter();
            case BEGIN_TYPE: return new BeginStringFilter(Integer.parseInt(checkNotEmpty(filter.length)));
            case REGEX_TYPE: {
                String regex = checkNotEmpty(filter.regex);
                String replace = checkNotEmpty(filter.replace);
                String flags = filter.flags;
                return flags == null || flags.isEmpty() ? new RegexStringFilter(regex, replace) :
                        new RegexStringFilter(regex, replace, flags);
            }
            default: throw new IllegalArgumentException("Unknown type: " + filter.type);
        }
    }

    public void addFilter(StringFilter filter) {  workers.add(filter); }

    public void addUpperCaseFilter() {
        addFilter(new UpperCaseStringFilter());
    }

    public void addTrimFilter() {
        addFilter(new TrimStringFilter());
    }

    public void addRegexFilter(String regex, String replaceString, int flags) {
        addFilter(new RegexStringFilter(regex, replaceString, flags));
    }

    public void addRegexFilter(String regex, String replaceString, String stringOfFlags) {
        addFilter(new RegexStringFilter(regex, replaceString, stringOfFlags));
    }

    public void addRegexFilter(String regex, String replaceString) {
        addFilter(new RegexStringFilter(regex, replaceString));
    }

    public void addBeginFilter(int length) {
        addFilter(new BeginStringFilter(length));
    }

    public String filter(String source) {
        for(StringFilter worker: workers) {
            source = worker.filter(source);
        }
        return source;
    }
}