package com.exemplo.clientes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.exemplo.clientes.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByCpf(String cpf);

    @Query("select c from Cliente c "
            + "where lower(c.nome) like lower(concat('%', :termo, '%')) "
            + "or lower(c.email) like lower(concat('%', :termo, '%')) "
            + "or c.cpf like concat('%', :termo, '%') "
            + "order by c.id")
    List<Cliente> buscar(@Param("termo") String termo);
}
