package br.com.cotiinformatica.api_produtos.controllers;

import br.com.cotiinformatica.api_produtos.dtos.ProdutoRequestDto;
import br.com.cotiinformatica.api_produtos.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        var response = produtoService.criar(request);
        return ResponseEntity.status(201).body(response);
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

        var response = produtoService.atualizar(id, request);
        return ResponseEntity.ok(response);
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
        var response = produtoService.excluir(id);
        return ResponseEntity.ok(response);
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
        var response = produtoService.consultar();
        return ResponseEntity.status(200).body(response);
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
        var response = produtoService.obterPorId(id);
        return ResponseEntity.ok(response);
    }
}
