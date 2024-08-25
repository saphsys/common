package com.saphsys.string.batchfilter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.regex.Pattern;

public class BatchStringFilterTests {

    @Test
    void trimFilter() {
        BatchStringFilter batchFilter = new BatchStringFilter();
        batchFilter.addTrimFilter();
        String filtered = batchFilter.filter("  trim   ");
        Assertions.assertEquals("trim", filtered);
    }

    @Test
    void upperCaseFilter() {
        BatchStringFilter batchFilter = new BatchStringFilter();
        batchFilter.addUpperCaseFilter();
        String filtered = batchFilter.filter("lower");
        Assertions.assertEquals("LOWER", filtered);
    }

    @Test
    void regexFilterSimple() {
        BatchStringFilter batchFilter = new BatchStringFilter();
        batchFilter.addRegexFilter("\\d+", "N");
        String filtered = batchFilter.filter("3 hours, 10 minutes");
        Assertions.assertEquals("N hours, N minutes", filtered);
    }

    @Test
    void regexFilterIntFlags() {
        BatchStringFilter batchFilter = new BatchStringFilter();
        batchFilter.addRegexFilter("TEST", "run", Pattern.CASE_INSENSITIVE);
        String filtered = batchFilter.filter("RegexpFilterIntFlags test failed");
        Assertions.assertEquals("RegexpFilterIntFlags run failed", filtered);
    }

    @Test
    void regexFilterStringFlags() {
        BatchStringFilter batchFilter = new BatchStringFilter();
        batchFilter.addRegexFilter("TEST", "run", "CASE_INSENSITIVE;MULTILINE");
        String filtered = batchFilter.filter("RegexpFilterIntFlags test failed\nand another test failed");
        Assertions.assertEquals("RegexpFilterIntFlags run failed\nand another run failed", filtered);
    }

    @Test
    void beginFilter() {
        BatchStringFilter batchFilter = new BatchStringFilter();
        batchFilter.addBeginFilter(5);
        String filtered = batchFilter.filter("Filter string");
        Assertions.assertEquals("Filte", filtered);
    }

    void cumulative(BatchStringFilter batchFilter) {
        String filtered = batchFilter.filter(" RegexpFilterIntFlags test failed\nand another test failed\nthird ");
        Assertions.assertEquals("REGEXPFILTERINTFLAGS RUN FAILED\nBUT ANOTHER RUN SUCCESSFUL ENDED", filtered);
    }

    @Test
    void cumulative() {
        BatchStringFilter batchFilter = new BatchStringFilter();
        batchFilter.addBeginFilter(58);
        batchFilter.addRegexFilter("TEST", "run", "CASE_INSENSITIVE;MULTILINE");
        batchFilter.addTrimFilter();
        batchFilter.addRegexFilter("failed$", "successful ended");
        batchFilter.addRegexFilter("^and", "but", "MULTILINE");
        batchFilter.addUpperCaseFilter();
        cumulative(batchFilter);
    }

    @Test
    void loadFromJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FiltersTestDto filtersTest = mapper.readValue(
                """
{
  "filters": [
    {
      "type": "begin",
      "length": 58
    },
    {
      "type": "regex",
      "regex": "TEST",
      "replace": "run",
      "flags": "CASE_INSENSITIVE;MULTILINE"
    },
    {
      "type": "trim"
    },
    {
      "type": "regex",
      "regex": "failed$",
      "replace": "successful ended"
    },
    {
      "type": "regex",
      "regex": "^and",
      "replace": "but",
      "flags": "MULTILINE"
    },
    {
      "type": "upper"
    }
  ]
}
                """,
                FiltersTestDto.class);
        cumulative(new BatchStringFilter(filtersTest.filters));
    }
}

class FiltersTestDto {
    @JsonProperty
    FilterDtoArrayList filters;
}