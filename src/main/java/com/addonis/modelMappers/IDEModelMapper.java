package com.addonis.modelMappers;

import com.addonis.dtos.IDEDto;
import com.addonis.models.IDE;
import com.addonis.repositories.IDE.IDERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IDEModelMapper {
    private final IDERepository ideRepository;

    @Autowired
    public IDEModelMapper(IDERepository ideRepository) {
        this.ideRepository = ideRepository;
    }

    public IDE fromDto(IDEDto ideDto) {
        IDE ide = new IDE();
        dtoToObject(ideDto, ide);
        return ide;
    }

    public IDE fromDto(IDEDto ideDto, int id) {
        IDE ide = ideRepository.getById(id);
        dtoToObject(ideDto, ide);
        return ide;
    }

    private void dtoToObject(IDEDto ideDto, IDE ide) {
        ide.setName(ideDto.getName());
    }
}
