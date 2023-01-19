package com.pizzapp.controller;

import com.pizzapp.dto.ClientDTO;
import com.pizzapp.dto.PizzaDTO;
import com.pizzapp.service.ClientService;
import com.pizzapp.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/pizapp")
public class PizzaContoller {

    @Autowired
    PizzaService pizzaService;

    @Autowired
    ClientService clientService;

    private void createDefaultPizzas() {
        try {
            pizzaService.defaultPizzas();
        } catch (Exception ignored) {
        }
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<PizzaDTO>> getPizzaList(@RequestParam(value = "pizzaId", required = false) Long pizzaId) {
        ResponseEntity<List<PizzaDTO>> response = ResponseEntity.noContent().build();
        createDefaultPizzas();
        if (pizzaId != null) {
            response = ResponseEntity.ok(Collections.singletonList(pizzaService.getPizzaByIdDTO(pizzaId)));
        } else {
            List<PizzaDTO> list = pizzaService.getAll();
            if (!list.isEmpty()) {
                response = ResponseEntity.ok(list);
            }
        }
        return response;
    }

    @PostMapping(value = "/create_user")
    public ResponseEntity<String> createUser(@RequestParam(value = "username") String username) {
        ResponseEntity<String> response = ResponseEntity.badRequest().build();
        if (clientService.saveClient(username)) {
            response = ResponseEntity.ok().build();
        }
        return response;
    }

    @GetMapping(value = "/get_user")
    public ResponseEntity<ClientDTO> getClientList(@RequestParam(value = "username") String username) {
        ResponseEntity<ClientDTO> response = ResponseEntity.badRequest().build();
        if (!Objects.equals(username, "")) {
            response = ResponseEntity.ok(clientService.getClientByUsernameDTO(username));
            if (response.getBody().getUsername() == null) {
                response = ResponseEntity.noContent().build();
            }
        }
        return response;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:4200/");
            }
        };
    }
}