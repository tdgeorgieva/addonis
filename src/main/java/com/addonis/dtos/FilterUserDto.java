package com.addonis.dtos;

public class FilterUserDto {

    private String name;

    private String sort;

    public FilterUserDto() {
    }

    public FilterUserDto(String name, String sort) {
        this.name = name;
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
