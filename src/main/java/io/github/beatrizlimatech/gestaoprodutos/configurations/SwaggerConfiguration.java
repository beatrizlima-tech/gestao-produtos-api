
package io.github.beatrizlimatech.gestaoprodutos.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestão de Produtos | API REST")
                        .version("1.0.0")
                        .description("""
                                API REST para gerenciamento de produtos e
                                controle de estoque.

                                Desenvolvida com Java, Spring Boot,
                                Spring Data JPA e PostgreSQL.

                                Funcionalidades:
                                - Cadastro de produtos
                                - Consulta de produtos
                                - Atualização de informações
                                - Exclusão de produtos
                                - Cálculo do valor total em estoque
                                """)
                        .contact(new Contact()
                                .name("Beatriz Lima")
                                .url("https://github.com/beatrizlima-tech")));
    }
}
