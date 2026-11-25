package br.com.luana.colheita.dto;

import br.com.luana.colheita.model.StatusFrente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * Dados que o cliente envia para criar ou atualizar uma frente.
 * As anotações validam a entrada antes de chegar na regra de negócio.
 */
public record FrenteRequest(
        @NotBlank(message = "O código é obrigatório")
        @Pattern(regexp = "F-\\d+", message = "O código deve seguir o padrão F-123")
        String codigo,

        @NotBlank(message = "O fornecedor é obrigatório")
        String fornecedor,

        @NotNull(message = "As toneladas/dia são obrigatórias")
        @Positive(message = "As toneladas/dia devem ser maiores que zero")
        Double toneladasDia,

        StatusFrente status
) {
}
