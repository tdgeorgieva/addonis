package com.addonis.services.IDE;

import com.addonis.exceptions.DuplicateEntityException;
import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.models.IDE;
import com.addonis.repositories.IDE.IDERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IDEServiceImpl implements IDEService {

    private final IDERepository ideRepository;

    @Autowired
    public IDEServiceImpl(IDERepository ideRepository) {
        this.ideRepository = ideRepository;
    }


    @Override
    public List<IDE> getAll() {
        return ideRepository.getAll();
    }

    @Override
    public IDE getById(int id) {
        return ideRepository.getById(id);
    }

    @Override
    public void create(IDE ide) {
        checkDuplicateExists("name", ide.getName());
        ideRepository.create(ide);
    }

    @Override
    public void update(IDE ide) {
        checkDuplicateExists("name", ide.getName());
        ideRepository.update(ide);
    }

    @Override
    public void delete(int id) {
        ideRepository.delete(id);
    }

    private void checkDuplicateExists(String attribute, String value) {
        boolean duplicateExists = true;
        try {
            ideRepository.getByField(attribute, value);
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("IDE", attribute, value);
        }
    }
}
