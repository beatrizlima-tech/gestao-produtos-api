package br.com.cotiinformatica.api_produtos;

import br.com.cotiinformatica.api_produtos.entities.Produto;
import br.com.cotiinformatica.api_produtos.repositories.ProdutoRepository;
import br.com.cotiinformatica.api_produtos.services.ProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void deveLimparProdutosExpirados() {

        //ARRANGE - preparar o teste
        var produto = new Produto();

        produto.setDataExclusao(
                LocalDateTime.now().minusDays(31)
        );

        when(produtoRepository.findByDataExclusaoLessThanEqual(
                any(LocalDateTime.class)
        )).thenReturn(List.of(produto));

        //ACT - Executar o método
        produtoService.limparLixeiraExpirada();

        //ASSERT - Verificar o resultado
        verify(produtoRepository).deleteAll(List.of(produto));
    }

    @Test
    void deveCalcularLimiteDeTrintaDias() {

        //ARRANGE
        LocalDateTime antes = LocalDateTime.now().minusDays(30);

        //ACT
        produtoService.limparLixeiraExpirada();

        //ASSERT
        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);

        verify(produtoRepository).findByDataExclusaoLessThanEqual(captor.capture());

        LocalDateTime depois = LocalDateTime.now().minusDays(30);

        assertFalse(captor.getValue().isBefore(antes));
        assertFalse(captor.getValue().isAfter(depois));
    }

}
