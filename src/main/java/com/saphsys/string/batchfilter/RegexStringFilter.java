package com.saphsys.string.batchfilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexStringFilter implements StringFilter {
    private final Pattern pattern;
    private final String replace;

    private static int getFlagsFromString(String stringOfFlags) {
        Pattern flagNames = Pattern.compile("(\\w+)");
        Matcher matcher = flagNames.matcher(stringOfFlags);
        int flags = 0;
        while (matcher.find()) {
            String flagName = matcher.group(1);
            int value;
            try {
                value = (Integer) Pattern.class.getDeclaredField(flagName).get(null);
            } catch (Exception ex) {
                throw new RuntimeException(String.format("Error getting value from \"java.util.regex.Pattern.%s\"", flagName), ex);
            }
            flags += value;
        }
        return flags;
    }

    public RegexStringFilter(String regex, String replace) {
        this(regex, replace, 0);
    }

    public RegexStringFilter(String regex, String replace, int flags) {
        this.pattern = Pattern.compile(regex, flags);
        this.replace = replace;
    }

    public RegexStringFilter(String regex, String replace, String stringOfFlags) {
        this(regex, replace, getFlagsFromString(stringOfFlags));
    }

    @Override
    public String filter(String source) {
        Matcher matcher = this.pattern.matcher(source);
        return matcher.replaceAll(this.replace);
    }
}
