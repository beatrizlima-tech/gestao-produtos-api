package io.github.beatrizlimatech.gestaoprodutos.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponseDto(
        Integer id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidade,
        BigDecimal total,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao,
        LocalDateTime dataExclusao
) {
}
