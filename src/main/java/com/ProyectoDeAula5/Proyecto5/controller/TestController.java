package com.ProyectoDeAula5.Proyecto5.controller;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/ping")
    public String ping() {
        return "Backend funcionando correctamente en Railway 🚀";
    }
}