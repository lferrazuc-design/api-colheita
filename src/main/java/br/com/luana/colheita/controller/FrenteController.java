package br.com.luana.colheita.controller;

import br.com.luana.colheita.dto.FrenteRequest;
import br.com.luana.colheita.dto.FrenteResponse;
import br.com.luana.colheita.model.StatusFrente;
import br.com.luana.colheita.service.FrenteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints REST de frentes de colheita.
 *
 * POST   /frentes          cria
 * GET    /frentes          lista (filtro opcional ?status=ATIVA)
 * GET    /frentes/{id}     busca por id
 * PUT    /frentes/{id}     atualiza
 * DELETE /frentes/{id}     exclui
 */
@RestController
@RequestMapping("/frentes")
public class FrenteController {

    private final FrenteService service;

    public FrenteController(FrenteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FrenteResponse> criar(@RequestBody @Valid FrenteRequest request) {
        FrenteResponse criada = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @GetMapping
    public List<FrenteResponse> listar(@RequestParam(required = false) StatusFrente status) {
        return service.listar(status);
    }

    @GetMapping("/{id}")
    public FrenteResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public FrenteResponse atualizar(@PathVariable Long id, @RequestBody @Valid FrenteRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
