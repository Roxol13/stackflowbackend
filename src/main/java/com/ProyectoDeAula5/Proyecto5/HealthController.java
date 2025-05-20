package com.ProyectoDeAula5.Proyecto5;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String healthCheck() {
        return "✔️ Proyecto5 está corriendo correctamente.";
    }
}
