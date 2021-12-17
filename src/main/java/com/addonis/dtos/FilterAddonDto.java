package com.addonis.dtos;

public class FilterAddonDto {

    private Integer ideId;

    private String name;

    private String sort;

    public FilterAddonDto() {
    }

    public FilterAddonDto(Integer ideId, String name, String sort) {
        this.ideId = ideId;
        this.name = name;
        this.sort = sort;

    }

    public Integer getIdeId() {
        return ideId;
    }

    public void setIdeId(Integer ideId) {
        this.ideId = ideId;
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
