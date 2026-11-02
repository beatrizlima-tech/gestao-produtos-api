package io.github.beatrizlimatech.gestaoprodutos;

import io.github.beatrizlimatech.gestaoprodutos.entities.Produto;
import io.github.beatrizlimatech.gestaoprodutos.repositories.ProdutoRepository;
import io.github.beatrizlimatech.gestaoprodutos.services.ProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void deveExcluirDefinitivamenteProdutoDaLixeira() {

        //ARRANGE
        var produto = new Produto();

        produto.setId(1);
        produto.setDataExclusao(LocalDateTime.now().minusDays(5));

        when(produtoRepository.findById(1))
                .thenReturn(Optional.of(produto));

        //ACT
        produtoService.excluirDefinitivamente(1);

        //ASSERT
        verify(produtoRepository).delete(produto);

    }

    @Test
    void deveCalcularTotalMonetarioSemImprecisao(){

        //ARRANGE
        var produto = new Produto();

        produto.setId(1);
        produto.setNome("Produto de teste");
        produto.setPreco(new BigDecimal("0.10"));
        produto.setQuantidade(3);

        when(produtoRepository.findById(1))
                .thenReturn(Optional.of(produto));

        //ACT
        var resposta = produtoService.obterPorId(1);

        //ASSERT
        //R$ 0,10 x 3 deve resultar exatamente em R$ 0,30.
        assertEquals(
                0,
                new BigDecimal(resposta.total().toString())
                        .compareTo(new BigDecimal("0.30"))
        );
    }

    @Test
    void deveRestaurarProdutoExcluidoHaDezDias() {

        //ARRANGE
        var produto = new Produto();

        produto.setId(1);
        produto.setNome("Produto de teste");
        produto.setPreco(new BigDecimal("100.00"));
        produto.setQuantidade(2);
        produto.setDataExclusao(
                LocalDateTime.now().minusDays(10)
        );

            when(produtoRepository.findById(1))
                    .thenReturn(Optional.of(produto));

        //ACT
        var resposta = produtoService.restaurar(1);

        //ASSERT
        assertNull(produto.getDataExclusao());
        assertNull(resposta.dataExclusao());

        verify(produtoRepository).save(produto);

    }

    @Test
    void naoDeveRestaurarProdutoComPrazoExpirado() {

        //ARRANGE
        var produto = new Produto();

        produto.setId(1);
        produto.setDataExclusao(
                LocalDateTime.now().minusDays(31)
        );

        when(produtoRepository.findById(1))
                .thenReturn(Optional.of(produto));

        //ACT
        var excecao = assertThrows(
                IllegalStateException.class,
                () -> produtoService.restaurar(1)
        );

        //ASSERT
        assertNotNull(produto.getDataExclusao());
        assertEquals(
                "O prazo de recuperação expirou.",
                        excecao.getMessage()
        );

        verify(produtoRepository, never()).save(produto);
    }

    @Test
    void naoDeveRestaurarProdutoAtivo() {

        //ARRANGE
        var produto = new Produto();

        produto.setId(1);
        produto.setDataExclusao(null);

        when(produtoRepository.findById(1))
                .thenReturn(Optional.of(produto));

        //ACT
        var excecao = assertThrows(
          IllegalStateException.class,
        () -> produtoService.restaurar(1)
        );

        //ASSERT
        assertNull(produto.getDataExclusao());
        assertEquals("O produto não está na lixeira.",
                excecao.getMessage());

        verify(produtoRepository, never()).save(produto);

    }

    @Test
    void naoDeveExcluirDefinitivamenteProdutoAtivo() {

        //ARRANGE
        var produto = new Produto();

        produto.setId(1);
        produto.setDataExclusao(null);

        when(produtoRepository.findById(1))
                .thenReturn(Optional.of(produto));

        //ACT
        var excecao = assertThrows(
                IllegalStateException.class,
                () -> produtoService.excluirDefinitivamente(1)
        );

        //ASSERT
        assertNull(produto.getDataExclusao());
        assertEquals("O produto precisa estar na lixeira para ser excluído definitivamente.",
                excecao.getMessage());

        verify(produtoRepository, never()).delete(produto);

    }

}
