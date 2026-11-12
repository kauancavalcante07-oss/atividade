package com.exemplo.clientes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:teste")
class CadastroClientesApplicationTests {

    @Test
    void contextLoads() {
    }
}
