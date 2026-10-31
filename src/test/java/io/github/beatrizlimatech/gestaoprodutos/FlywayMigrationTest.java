package io.github.beatrizlimatech.gestaoprodutos;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class FlywayMigrationTest {

    @Container
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:16");

    @Test
    void deveCriarEstruturaERegistrarMigracaoV1() throws Exception {

        // ARRANGE: configurar Flyway para o banco temporario
        Flyway flyway = Flyway.configure()
                .dataSource(
                        postgres.getJdbcUrl(),
                        postgres.getUsername(),
                        postgres.getPassword()
                )
                .locations("classpath:db/migration")
                .load();

        // ACT: executar a migracao V1
        var resultado = flyway.migrate();

        // ASSERT: a V1 deve ter sido executada uma vez
        assertEquals(1, resultado.migrationsExecuted);

        try (var conexao = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        ); var comando = conexao.createStatement()) {

            // Verificar tabela produtos
            try (var rs = comando.executeQuery(
                    "SELECT to_regclass('public.produtos')"
            )) {
                assertTrue(rs.next());
                assertEquals("produtos", rs.getString(1));
            }

            // Verificar as 8 colunas da tabela
            try (var rs = comando.executeQuery("""
                    SELECT COUNT(*)
                    FROM information_schema.columns
                    WHERE table_schema = 'public'
                      AND table_name = 'produtos'
                    """)) {
                assertTrue(rs.next());
                assertEquals(8, rs.getInt(1));
            }

            // Verificar a sequencia utilizada pelo Hibernate
            try (var rs = comando.executeQuery("""
                    SELECT increment_by
                    FROM pg_sequences
                    WHERE schemaname = 'public'
                      AND sequencename = 'produtos_seq'
                    """)) {
                assertTrue(rs.next());
                assertEquals(50, rs.getLong(1));
            }

            // Verificar registro da V1 no historico
            try (var rs = comando.executeQuery("""
                    SELECT version, type, success
                    FROM flyway_schema_history
                    WHERE version = '1'
                    """)) {
                assertTrue(rs.next());
                assertEquals("1", rs.getString("version"));
                assertEquals("SQL", rs.getString("type"));
                assertTrue(rs.getBoolean("success"));
                assertFalse(rs.next());
            }
        }

        // Executar novamente nao deve repetir a V1
        var segundaExecucao = flyway.migrate();
        assertEquals(0, segundaExecucao.migrationsExecuted);
    }
}
