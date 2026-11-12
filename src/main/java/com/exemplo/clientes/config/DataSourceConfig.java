package com.exemplo.clientes.config;

import java.net.URI;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "DATABASE_URL")
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(@Value("${DATABASE_URL}") String valor,
                                 @Value("${DB_USER:}") String usuario,
                                 @Value("${DB_PASSWORD:}") String senha) {
        if (valor.startsWith("jdbc:")) {
            return DataSourceBuilder.create()
                    .url(valor)
                    .username(usuario)
                    .password(senha)
                    .build();
        }

        URI uri = URI.create(valor);
        String[] credenciais = uri.getUserInfo().split(":", 2);
        String host = uri.getHost();
        int porta = uri.getPort() > 0 ? uri.getPort() : 5432;
        String url = "jdbc:postgresql://" + host + ":" + porta + uri.getPath();
        if (host.contains(".")) {
            url += "?sslmode=require";
        }

        return DataSourceBuilder.create()
                .url(url)
                .username(credenciais[0])
                .password(credenciais[1])
                .build();
    }
}
