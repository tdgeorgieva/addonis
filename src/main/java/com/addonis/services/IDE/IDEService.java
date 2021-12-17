package com.addonis.services.IDE;

import com.addonis.models.IDE;

import java.util.List;

public interface IDEService {
    List<IDE> getAll();

    IDE getById(int id);

    void create(IDE ide);

    void update(IDE ide);

    void delete(int id);
}
