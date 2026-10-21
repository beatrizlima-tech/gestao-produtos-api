package br.com.cotiinformatica.api_produtos.handlers;

import br.com.cotiinformatica.api_produtos.exceptions.ProdutoNaoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(
            MethodArgumentNotValidException exception){

        var mensagens = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .distinct()
                .toList();

        return ResponseEntity.badRequest().body(
                Map.of(
                        "status", 400,
                        "mensagens", mensagens
                )
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> tratarJsonInvalido(
            HttpMessageNotReadableException exception){
        return ResponseEntity.badRequest().body(
                Map.of(
                        "status", 400,
                        "mensagens", List.of(
                                "JSON inválido. Verifique os dados enviados."
                        )
                )
        );
    }


    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarProdutoNaoEncontrado(
            ProdutoNaoEncontradoException exception) {

        return ResponseEntity.status(404).body(
                Map.of(
                        "status", 404,
                        "mensagens", List.of(exception.getMessage())
                )
        );
    }

}
