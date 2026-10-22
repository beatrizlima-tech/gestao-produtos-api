package io.github.beatrizlimatech.gestaoprodutos.exceptions;

public class ProdutoNaoEncontradoException extends RuntimeException {

    //Implementar um método para
    //retornar a mensagem de erro

    @Override
    public String getMessage() {
        return "Produto não encontrado. Verifique o ID informado.";
    }
}
