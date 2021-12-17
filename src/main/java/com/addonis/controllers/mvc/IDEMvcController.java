package com.addonis.controllers.mvc;

import com.addonis.services.IDE.IDEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ides")
public class IDEMvcController {

    private final IDEService ideService;

    @Autowired
    public IDEMvcController(IDEService ideService) {
        this.ideService = ideService;
    }

    @GetMapping
    public String showAllIdea(Model model) {
        model.addAttribute("ides", ideService.getAll());
        return "ides";
    }
}
