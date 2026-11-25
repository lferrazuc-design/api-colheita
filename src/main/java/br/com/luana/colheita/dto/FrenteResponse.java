package br.com.luana.colheita.dto;

import br.com.luana.colheita.model.Frente;
import br.com.luana.colheita.model.StatusFrente;

/**
 * Dados que a API devolve. Separar entrada (Request) e saída (Response)
 * evita expor a entidade do banco diretamente.
 */
public record FrenteResponse(
        Long id,
        String codigo,
        String fornecedor,
        Double toneladasDia,
        StatusFrente status
) {
    public static FrenteResponse de(Frente frente) {
        return new FrenteResponse(
                frente.getId(),
                frente.getCodigo(),
                frente.getFornecedor(),
                frente.getToneladasDia(),
                frente.getStatus()
        );
    }
}
