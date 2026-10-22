package br.com.cotiinformatica.api_produtos.services;

import br.com.cotiinformatica.api_produtos.dtos.ProdutoRequestDto;
import br.com.cotiinformatica.api_produtos.dtos.ProdutoResponseDto;
import br.com.cotiinformatica.api_produtos.entities.Produto;
import br.com.cotiinformatica.api_produtos.exceptions.ProdutoNaoEncontradoException;
import br.com.cotiinformatica.api_produtos.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public ProdutoResponseDto criar(ProdutoRequestDto request) {

        var produto = new Produto();

        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPreco(BigDecimal.valueOf(request.preco()));
        produto.setQuantidade(request.quantidade());

        produtoRepository.save(produto);

        return toResponse(produto);
    }

    public ProdutoResponseDto atualizar(Integer id, ProdutoRequestDto request) {

        var produto = produtoRepository.findById(id)
                .orElseThrow(ProdutoNaoEncontradoException::new);

        if (produto.getDataExclusao() !=null) {
            throw new IllegalStateException(
                    "Não é possível editar um produto que está na lixeira."
            );
        }

        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPreco(BigDecimal.valueOf(request.preco()));
        produto.setQuantidade(request.quantidade());

        produtoRepository.save(produto);

        return toResponse(produto);
    }

    public ProdutoResponseDto excluir(Integer id) {

        var produto = produtoRepository.findById(id)
                .orElseThrow(ProdutoNaoEncontradoException::new);

        if (produto.getDataExclusao() !=null) {
            throw new IllegalStateException(
                    "Produto já está na lixeira."
            );
        }

        produto.setDataExclusao(LocalDateTime.now());

        produtoRepository.save(produto);

        return toResponse(produto);

    }

    public ProdutoResponseDto restaurar(Integer id) {

        var produto = produtoRepository.findById(id)
                .orElseThrow(ProdutoNaoEncontradoException::new);

        if (produto.getDataExclusao() == null) {
            throw new IllegalStateException(
                    "O produto não está na lixeira."
            );
        }

        LocalDateTime limite = produto.getDataExclusao().plusDays(30);

        if (!LocalDateTime.now().isBefore(limite)) {
            throw new IllegalStateException(
                    "O prazo de recuperação expirou."
            );
        }

        produto.setDataExclusao(null);

        produtoRepository.save(produto);

        return toResponse(produto);

    }

    public void excluirDefinitivamente(Integer id) {

        var produto = produtoRepository.findById(id)
                .orElseThrow(ProdutoNaoEncontradoException::new);

        if (produto.getDataExclusao() == null) {
            throw new IllegalStateException(
                    "O produto precisa estar na lixeira para ser excluído definitivamente."
            );
        }
    }

    public List<ProdutoResponseDto> consultar() {

        var produtos = produtoRepository.findByDataExclusaoIsNull();

        return produtos
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProdutoResponseDto obterPorId(Integer id) {

        var produto = produtoRepository.findById(id)
                .orElseThrow(ProdutoNaoEncontradoException::new);

        if (produto.getDataExclusao() !=null) {
            throw new ProdutoNaoEncontradoException();
        }

        return toResponse(produto);
    }

    public List<ProdutoResponseDto> consultarLixeira() {

        var produtos = produtoRepository.findByDataExclusaoIsNotNull();

        return produtos
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void limparLixeiraExpirada() {

        LocalDateTime limite = LocalDateTime.now().minusDays(30);

        var produtosExpirados = produtoRepository.findByDataExclusaoLessThanEqual(limite);

        produtoRepository.deleteAll(produtosExpirados);
    }

    private ProdutoResponseDto toResponse(Produto produto) {
        return new ProdutoResponseDto(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco().doubleValue(),
                produto.getQuantidade(),
                produto.getPreco().doubleValue() * produto.getQuantidade(),
                produto.getDataCadastro(),
                produto.getDataAtualizacao(),
                produto.getDataExclusao()
        );
    }
}
