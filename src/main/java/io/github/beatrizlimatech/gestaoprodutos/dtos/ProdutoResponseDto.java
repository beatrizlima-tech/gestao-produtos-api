package io.github.beatrizlimatech.gestaoprodutos.dtos;

import java.time.LocalDateTime;

public record ProdutoResponseDto(
        Integer id,
        String nome,
        String descricao,
        Double preco,
        Integer quantidade,
        Double total,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao,
        LocalDateTime dataExclusao
) {
}
