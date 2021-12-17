package com.addonis.controllers.rest;

import com.addonis.dtos.IDEDto;
import com.addonis.modelMappers.IDEModelMapper;
import com.addonis.models.IDE;
import com.addonis.services.IDE.IDEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ides")
public class IDEController {

    private final IDEModelMapper ideModelMapper;
    private final IDEService ideService;

    @Autowired
    public IDEController(IDEModelMapper ideModelMapper, IDEService ideService) {
        this.ideModelMapper = ideModelMapper;
        this.ideService = ideService;
    }

    @GetMapping
    public List<IDE> getAll() {
        return ideService.getAll();
    }

    @GetMapping("/{id}")
    public IDE getById(@PathVariable int id) {
        return ideService.getById(id);

    }

    @PostMapping
    public IDE create(@Valid @RequestBody IDEDto ideDto) {
        IDE ide = ideModelMapper.fromDto(ideDto);
        ideService.create(ide);
        return ide;

    }

    @PutMapping("/{id}")
    public IDE update(@PathVariable int id, @Valid @RequestBody IDEDto ideDto) {
        IDE ide = ideModelMapper.fromDto(ideDto, id);
        ideService.update(ide);
        return ide;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        ideService.delete(id);

    }
}
