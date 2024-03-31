package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.InfoService;

@RestController
public class InfoController {
    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("getPort")
    public int getPort() {
        return infoService.getPort();
    }
    @GetMapping("getValue")
    public String getValue() {
        return infoService.getValue();
    }
}
