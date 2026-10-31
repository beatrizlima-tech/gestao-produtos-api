package io.github.beatrizlimatech.gestaoprodutos.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequestDto(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
        String nome,

        @Size(max = 150, message = "A descrição deve ter no máximo 500 caracteres.")
        String descricao,

        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(
                value = "0.01",
                message = "Informe um preço válido a partir de R$ 0,01"
        )
        @Digits(
                integer = 8,
                fraction = 2,
                message = "O preço deve ter no máximo 8 dígitos inteiros e 2 casas decimais."
        )
        BigDecimal preco,

        @NotNull(message = "A quantidade é obrigatória.")
        @PositiveOrZero(message = "A quantidade não pode ser negativa.")
        Integer quantidade
) {
}
