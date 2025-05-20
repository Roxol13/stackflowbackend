package com.ProyectoDeAula5.Proyecto5.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/ping")
    public String ping() {
        return "Backend funcionando correctamente en Railway 🚀";
    }
}