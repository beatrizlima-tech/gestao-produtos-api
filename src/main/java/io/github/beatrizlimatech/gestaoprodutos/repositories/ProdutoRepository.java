package io.github.beatrizlimatech.gestaoprodutos.repositories;

import io.github.beatrizlimatech.gestaoprodutos.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    List<Produto> findByDataExclusaoIsNull();

    List<Produto> findByDataExclusaoIsNotNull();

    List<Produto> findByDataExclusaoLessThanEqual(LocalDateTime limite);

}
