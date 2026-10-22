package br.com.cotiinformatica.api_produtos;

import br.com.cotiinformatica.api_produtos.dtos.ProdutoRequestDto;
import br.com.cotiinformatica.api_produtos.dtos.ProdutoResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiProdutosApplicationTests {

	@Autowired
	private MockMvc mockMvc; //Acessar a API do projeto

	@Autowired
	private ObjectMapper objectMapper; //manipular JSON

	@BeforeEach
	void configurarObjectMapper(){
		objectMapper.registerModule(new JavaTimeModule());
	}

	private static Integer produtoId;

	@Test
	@Order(1)
	void deveCadastrarProduto() throws Exception {

		var request = new ProdutoRequestDto(
				"Notebook Dell",
				"Notebook Dell para trabalho e estudos",
				5000.0,
				10
		);

		//ACT
		var result = mockMvc.perform(post("/api/v1/produtos")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andReturn();

		var content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		var response = objectMapper.readValue(content, ProdutoResponseDto.class);

		//Guardar o ID para os próximos testes
		produtoId = response.id();

		//ASSERT
		assertNotEquals(0, response.id());
		assertEquals(request.nome(), response.nome());
		assertEquals(request.descricao(), response.descricao());
		assertEquals(request.preco(), response.preco());
		assertEquals(request.quantidade(), response.quantidade());
	}

	@Test
	@Order(2)
	void deveConsultarProdutos() throws Exception {

		//ACT
		var result = mockMvc.perform(get("/api/v1/produtos")
						.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();

		var content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		var response = objectMapper.readValue(content, ProdutoResponseDto[].class);

		//ASSERT
		assertNotNull(response);
		assertTrue(response.length > 0);
	}

	@Test
	@Order(3)
	void deveObterProdutoPorId() throws Exception {

		//ACT
		var result = mockMvc.perform(get("/api/v1/produtos/" + produtoId)
						.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();

		var content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		var response = objectMapper.readValue(content, ProdutoResponseDto.class);

		//ASSERT
		assertNotNull(response);
		assertEquals(produtoId, response.id());
		assertEquals("Notebook Dell", response.nome());
		assertEquals(5000.0, response.preco());
		assertEquals(10, response.quantidade());
	}

	@Test
	@Order(4)
	void deveAtualizarProduto() throws Exception {

		var request = new ProdutoRequestDto(
				"Notebook Dell Gamer",
				"Notebook gamer de alto desempenho.",
				6500.0,
				8
		);

		var result = mockMvc.perform(put("/api/v1/produtos/" + produtoId)
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andReturn();

		var content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		var response = objectMapper.readValue(content, ProdutoResponseDto.class);

		//ASSERT
		assertNotNull(response);
		assertEquals(produtoId, response.id());
		assertEquals(request.nome(), response.nome());
		assertEquals(request.descricao(), response.descricao());
		assertEquals(request.preco(), response.preco());
		assertEquals(request.quantidade(), response.quantidade());
	}

	@Test
	@Order(5)
	void deveExcluirProduto() throws Exception {

		//ACT
		mockMvc.perform(delete("/api/v1/produtos/" + produtoId)
						.contentType("application/json"))
				.andExpect(status().isOk());

		//ASSERT
		mockMvc.perform(get("/api/v1/produtos/" + produtoId)
						.contentType("application/json"))
				.andExpect(status().isNotFound());
	}
}