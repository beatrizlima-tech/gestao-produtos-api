package io.github.beatrizlimatech.gestaoprodutos.factories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class ConnectionFactory {


    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    // Método para abrir conexão com o banco de dados

    public Connection getConnection() throws Exception {

        return DriverManager.getConnection(url, username, password);
    }

}
