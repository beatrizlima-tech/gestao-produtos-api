package io.github.beatrizlimatech.gestaoprodutos.schedulers;

import io.github.beatrizlimatech.gestaoprodutos.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LixeiraScheduler {

    @Autowired
    private ProdutoService produtoService;

    @Scheduled(cron = "0 0 3 * * *", zone = "America/Sao_Paulo")
    public void executarLimpeza() {

        produtoService.limparLixeiraExpirada();
    }
}
