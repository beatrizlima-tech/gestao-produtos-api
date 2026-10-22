package io.github.beatrizlimatech.gestaoprodutos;

import io.github.beatrizlimatech.gestaoprodutos.schedulers.LixeiraScheduler;
import io.github.beatrizlimatech.gestaoprodutos.services.ProdutoService;
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
    void deveExecutarLimpezaAgendada() {

        lixeiraScheduler.executarLimpeza();

        verify(produtoService).limparLixeiraExpirada();
    }
}
