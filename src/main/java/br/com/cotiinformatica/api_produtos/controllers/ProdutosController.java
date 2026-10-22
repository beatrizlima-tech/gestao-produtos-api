package br.com.cotiinformatica.api_produtos.controllers;

import br.com.cotiinformatica.api_produtos.dtos.ProdutoRequestDto;
import br.com.cotiinformatica.api_produtos.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(
        name = "Gestão de Produtos",
        description = "Operações para gerenciamento de produtos e controle de estoque."
)
@RestController
@RequestMapping("api/v1/produtos")
public class ProdutosController {

    @Autowired
    private ProdutoService produtoService;

    @Operation(
            summary = "Cadastrar produto",
            description = "Cadastra um novo produto com nome, preço unitário e quantidade em estoque."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Produto cadastrado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos. Verifique nome, preço e quantidade."
            )
    })
    @PostMapping
    public ResponseEntity<?> post(@Valid @RequestBody ProdutoRequestDto request) {

        return ResponseEntity.status(201).body(produtoService.criar(request));
    }

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza o nome, preço unitário e quantidade de um produto existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto atualizado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos. Verifique nome, preço e quantidade."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado para o ID informado."
            )
    })
    @PutMapping("{id}")
    public ResponseEntity<?> put(
            @PathVariable Integer id,
            @Valid @RequestBody ProdutoRequestDto request) {

        return ResponseEntity.ok(produtoService.atualizar(id, request));
    }

    @Operation(
            summary = "Excluir produto",
            description = "Remova um produto cadastrado a partir do seu identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto excluído com sucesso."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado para o ID informado."
            )
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        return ResponseEntity.ok(produtoService.excluir(id));
    }

    @Operation(
            summary = "Listar produtos",
            description = "Retorna todos os produtos cadastrados e seus respectivos valores."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta realizada com sucesso. Retorna a lista de produtos cadastrados."
            )
    })
    @GetMapping
    public ResponseEntity<?> getAll() {

        return ResponseEntity.status(200).body(produtoService.consultar());

    }

    @Operation(
            summary = "Buscar produto por ID",
            description = "Consulta as informações de um produto específico utilizando seu identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto encontrado e retornado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado para o ID informado."
            )
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(produtoService.obterPorId(id));
    }

    @Operation(
            summary = "Consultar lixeira",
            description = "Lista os produtos enviados para a lixeira."
    )
    @GetMapping("/lixeira")
    public ResponseEntity<?> getLixeira() {

        return ResponseEntity.ok(produtoService.consultarLixeira());

    }

    @Operation(
            summary = "Restaurar produto",
            description = "Restaura um produto excluído dentro do prazo de 30 dias."
    )
    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<?> restaurar(@PathVariable Integer id) {

        return ResponseEntity.ok(produtoService.restaurar(id));
    }

    @Operation(
            summary = "Excluir produto definitivamente.",
            description = "Remove permanentemente um produto que está na lixeira."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Produto excluído definitivamente."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado."
            )
    })
    @DeleteMapping("/{id}/definitivo")
    public ResponseEntity<Void> excluirDefinitivamente(
            @PathVariable Integer id) {

        produtoService.excluirDefinitivamente(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> tratarConflito(
            IllegalStateException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("erro", ex.getMessage()));
    }
}
