package com.addonis.dtos;

import javax.validation.constraints.NotBlank;

public class IDEDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    public IDEDto() {
    }

    public IDEDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
