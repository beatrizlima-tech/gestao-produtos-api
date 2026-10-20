package br.com.cotiinformatica.api_produtos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProdutoRequestDto(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 150 caracteres.")
        String nome,

        @NotBlank(message = "O preço é obrigatório.")
        @Positive(message = "Informe um preço válido a partir de R$ 0,01.")
        Double preco,

        @NotBlank(message = "A quantidade é obrigatória.")
        @PositiveOrZero(message = "A quantidade não pode ser negativa.")
        Integer quantidade
) {
}
