# Cadastro de Clientes

Sistema de cadastro de clientes com front-end e back-end no mesmo projeto Spring Boot.

- Back-end: Spring Boot 3, Spring Data JPA, Bean Validation
- Front-end: HTML, CSS e JavaScript em `src/main/resources/static/`
- Banco: H2 em arquivo (local) e PostgreSQL (Render)

## Rodar localmente

Requisitos: Java 17 ou superior e internet na primeira execução.

```
./mvnw spring-boot:run
```

No Windows: `mvnw.cmd spring-boot:run`

Acesse http://localhost:8080

Os dados locais ficam na pasta `data/`.

## API

| Método | Rota             | Descrição                                   |
|--------|------------------|---------------------------------------------|
| GET    | `/clientes`      | Lista clientes (`?busca=` filtra)           |
| GET    | `/clientes/{id}` | Busca um cliente                            |
| POST   | `/clientes`      | Cria um cliente                             |
| PUT    | `/clientes/{id}` | Atualiza um cliente                         |
| DELETE | `/clientes/{id}` | Exclui um cliente                           |

Exemplo de corpo (JSON):

```
{
  "nome": "Maria Silva",
  "email": "maria@exemplo.com",
  "telefone": "11912345678",
  "cpf": "12345678901"
}
```

Regras: nome, e-mail e CPF obrigatórios; CPF com 11 dígitos; telefone com 10 ou 11 dígitos (opcional); e-mail e CPF únicos.

## Publicar no Render

### 1. Subir no GitHub

```
git init
git add .
git commit -m "Cadastro de clientes"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/cadastro-clientes.git
git push -u origin main
```

### 2. Banco PostgreSQL

1. No Render: **New > PostgreSQL**, escolha o plano gratuito e crie.
2. Copie a **Internal Database URL**.

### 3. API (Web Service)

1. **New > Web Service** e conecte o repositório.
2. Em **Language/Runtime** escolha **Docker** (o `Dockerfile` já está no projeto).
3. Em **Environment**, crie a variável `DATABASE_URL` com a Internal Database URL copiada.
4. Faça o deploy. A tabela é criada automaticamente.

Alternativa: **New > Blueprint** usando o `render.yaml`, que cria o banco e o serviço já ligados.

### 4. Front-end em serviço separado (opcional)

Para publicar o front-end à parte, crie um **Static Site** apontando para a pasta `src/main/resources/static` e, no início do `script.js`, informe a URL completa da API:

```
const API_BASE_URL = 'https://SEU-SERVICO.onrender.com/clientes';
```

Na API, defina a variável `CORS_ORIGENS` com a URL do front-end (por exemplo `https://seu-front.onrender.com`).

No plano gratuito, o serviço "dorme" após inatividade e a primeira requisição pode demorar cerca de um minuto.
