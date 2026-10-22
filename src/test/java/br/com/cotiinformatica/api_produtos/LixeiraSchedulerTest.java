package br.com.cotiinformatica.api_produtos;

import br.com.cotiinformatica.api_produtos.schedulers.LixeiraScheduler;
import br.com.cotiinformatica.api_produtos.services.ProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LixeiraSchedulerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private LixeiraScheduler lixeiraScheduler;

    @Test
    void deveExecutarLimpezaAgendada(){

        lixeiraScheduler.executarLimpeza();

        verify(produtoService).limparLixeiraExpirada();
    }
}
