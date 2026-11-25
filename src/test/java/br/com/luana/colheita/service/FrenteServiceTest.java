package br.com.luana.colheita.service;

import br.com.luana.colheita.dto.FrenteRequest;
import br.com.luana.colheita.dto.FrenteResponse;
import br.com.luana.colheita.exception.RecursoNaoEncontradoException;
import br.com.luana.colheita.exception.RegraNegocioException;
import br.com.luana.colheita.model.Frente;
import br.com.luana.colheita.model.StatusFrente;
import br.com.luana.colheita.repository.FrenteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários: o repository é simulado (mock), então testamos
 * só a regra de negócio, sem banco de dados.
 */
@ExtendWith(MockitoExtension.class)
class FrenteServiceTest {

    @Mock
    private FrenteRepository repository;

    @InjectMocks
    private FrenteService service;

    @Test
    void deveCriarFrenteComStatusAtivaPorPadrao() {
        FrenteRequest request = new FrenteRequest("F-11", "Fornecedor A", 850.0, null);
        when(repository.existsByCodigo("F-11")).thenReturn(false);
        when(repository.save(any(Frente.class))).thenAnswer(inv -> {
            Frente f = inv.getArgument(0);
            f.setId(1L);
            return f;
        });

        FrenteResponse resposta = service.criar(request);

        assertThat(resposta.id()).isEqualTo(1L);
        assertThat(resposta.codigo()).isEqualTo("F-11");
        assertThat(resposta.status()).isEqualTo(StatusFrente.ATIVA);
    }

    @Test
    void naoDeveCriarFrenteComCodigoDuplicado() {
        FrenteRequest request = new FrenteRequest("F-11", "Fornecedor A", 850.0, null);
        when(repository.existsByCodigo("F-11")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(request))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("F-11");

        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarErroQuandoFrenteNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    void deveAtualizarFrente() {
        Frente existente = new Frente("F-11", "Fornecedor A", 850.0);
        existente.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Frente.class))).thenAnswer(inv -> inv.getArgument(0));

        FrenteRequest request = new FrenteRequest("F-11", "Fornecedor B", 900.0, StatusFrente.PARADA);
        FrenteResponse resposta = service.atualizar(1L, request);

        assertThat(resposta.fornecedor()).isEqualTo("Fornecedor B");
        assertThat(resposta.toneladasDia()).isEqualTo(900.0);
        assertThat(resposta.status()).isEqualTo(StatusFrente.PARADA);
    }
}
