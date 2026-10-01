package br.com.alura.pomodoro.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("message", "Pomodoro API está no ar. Documentação em /swagger-ui.html");
    }
}
