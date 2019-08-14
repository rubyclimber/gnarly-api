package com.ohgnarly.gnarlyapi.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class MessagesRequest {
    private String searchDate;
    private String searchText;

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public LocalDate getSearchDateValue() {
        if (isBlank(searchDate)) {
            return null;
        }

        return LocalDate.parse(searchDate, DateTimeFormatter.ISO_DATE);
    }
}
