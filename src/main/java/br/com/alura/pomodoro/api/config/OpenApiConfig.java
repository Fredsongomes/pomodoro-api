package br.com.alura.pomodoro.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pomodoroOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pomodoro API")
                        .description("API REST para gerenciamento de tarefas: criar, listar, filtrar por status de conclusão, atualizar e remover tarefas.")
                        .version("1.0.0"));
    }
}
