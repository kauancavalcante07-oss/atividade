package com.exemplo.clientes.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exemplo.clientes.exception.ConflitoException;
import com.exemplo.clientes.model.Cliente;
import com.exemplo.clientes.repository.ClienteRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Cliente> listar(@RequestParam(required = false) String busca) {
        if (busca == null || busca.isBlank()) {
            return repository.findAll(Sort.by("id"));
        }
        return repository.buscar(busca.trim());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente entrada) {
        normalizar(entrada);
        verificarDuplicidade(entrada, null);
        entrada.setId(null);
        Cliente salvo = repository.save(entrada);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente entrada) {
        return repository.findById(id)
                .map(atual -> {
                    normalizar(entrada);
                    verificarDuplicidade(entrada, id);
                    atual.setNome(entrada.getNome());
                    atual.setEmail(entrada.getEmail());
                    atual.setTelefone(entrada.getTelefone());
                    atual.setCpf(entrada.getCpf());
                    return ResponseEntity.ok(repository.save(atual));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void normalizar(Cliente cliente) {
        cliente.setNome(cliente.getNome().trim());
        cliente.setEmail(cliente.getEmail().trim().toLowerCase());
        if (cliente.getTelefone() != null && cliente.getTelefone().isBlank()) {
            cliente.setTelefone(null);
        }
    }

    private void verificarDuplicidade(Cliente cliente, Long idAtual) {
        repository.findByEmail(cliente.getEmail())
                .filter(outro -> !outro.getId().equals(idAtual))
                .ifPresent(outro -> {
                    throw new ConflitoException("já existe um cliente com este e-mail");
                });
        repository.findByCpf(cliente.getCpf())
                .filter(outro -> !outro.getId().equals(idAtual))
                .ifPresent(outro -> {
                    throw new ConflitoException("já existe um cliente com este CPF");
                });
    }
}
