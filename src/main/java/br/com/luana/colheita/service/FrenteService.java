package br.com.luana.colheita.service;

import br.com.luana.colheita.dto.FrenteRequest;
import br.com.luana.colheita.dto.FrenteResponse;
import br.com.luana.colheita.exception.RecursoNaoEncontradoException;
import br.com.luana.colheita.exception.RegraNegocioException;
import br.com.luana.colheita.model.Frente;
import br.com.luana.colheita.model.StatusFrente;
import br.com.luana.colheita.repository.FrenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Regras de negócio das frentes de colheita.
 */
@Service
public class FrenteService {

    private final FrenteRepository repository;

    public FrenteService(FrenteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public FrenteResponse criar(FrenteRequest request) {
        if (repository.existsByCodigo(request.codigo())) {
            throw new RegraNegocioException("Já existe uma frente com o código " + request.codigo());
        }
        Frente frente = new Frente(request.codigo(), request.fornecedor(), request.toneladasDia());
        if (request.status() != null) {
            frente.setStatus(request.status());
        }
        return FrenteResponse.de(repository.save(frente));
    }

    @Transactional(readOnly = true)
    public List<FrenteResponse> listar(StatusFrente status) {
        List<Frente> frentes = (status == null)
                ? repository.findAll()
                : repository.findByStatus(status);
        return frentes.stream().map(FrenteResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public FrenteResponse buscarPorId(Long id) {
        return FrenteResponse.de(buscarEntidade(id));
    }

    @Transactional
    public FrenteResponse atualizar(Long id, FrenteRequest request) {
        Frente frente = buscarEntidade(id);

        boolean mudouCodigo = !frente.getCodigo().equals(request.codigo());
        if (mudouCodigo && repository.existsByCodigo(request.codigo())) {
            throw new RegraNegocioException("Já existe uma frente com o código " + request.codigo());
        }

        frente.setCodigo(request.codigo());
        frente.setFornecedor(request.fornecedor());
        frente.setToneladasDia(request.toneladasDia());
        if (request.status() != null) {
            frente.setStatus(request.status());
        }
        return FrenteResponse.de(repository.save(frente));
    }

    @Transactional
    public void excluir(Long id) {
        Frente frente = buscarEntidade(id);
        repository.delete(frente);
    }

    private Frente buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Frente " + id + " não encontrada"));
    }
}
