package com.challenge.encomendas.encomendas.adapters.controllers;

import com.challenge.encomendas.encomendas.domain.entities.Encomenda;
import com.challenge.encomendas.encomendas.usecase.cadastro.EncomendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/encomendas")
public class EncomendaController {

    private final EncomendaService encomendaService;

    @Autowired
    public EncomendaController(EncomendaService encomendaService) {
        this.encomendaService = encomendaService;
    }

    @PreAuthorize("hasRole('PORTEIRO') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Encomenda> criarEncomenda(@RequestBody Encomenda encomenda) {
        return ResponseEntity.ok(encomendaService.cadastrarEncomenda(encomenda));
    }

    @PreAuthorize("hasRole('PORTEIRO')")
    @PutMapping("/{id}/retirada")
    public ResponseEntity<Encomenda> confirmarRetirada(@PathVariable Long id) {
        return ResponseEntity.ok(encomendaService.confirmarRetirada(id));
    }
}

