# Pinterest Clone

Um clone full stack do Pinterest, um app para praticar o desenvolvimento full stack, construído com **Java Spring Boot**, **Vite + React** e **Android Jetpack Compose**.

## Descrição

Este projeto tem o objetivo de replicar as principais funcionalidades do Pinterest, permitindo que usuários criem boards, adicionem pins, sigam outros usuários e naveguem busque por pins, tanto na web quanto em dispositivos Android.

## Tecnologias

- **Backend:** Java 21, Spring Boot, Spring Cloud OpenFeign, RabbitMQ, Eureka, MapStruct, JWT, PostgreSQL
- **Frontend Web:** Vite, React
- **Mobile:** Android Jetpack Compose
- **Outros:** Lombok, JPA, Docker, Kubernetes, VPS Server (Hostinger)

## Principais Funcionalidades

- Cadastro, autenticação e gerenciamento de usuários
- Criação, edição, deleção e visualização de boards
- Adição de pins a boards
- Seguir e deixar de seguir outros usuários
- Listagem de boards e pins por usuário
- Integração entre serviços via Feign Client, RabbitMQ, Eureka (arquitetura de microsserviços)
- Autenticação via JWT

## Estrutura do Projeto

```
services/
  api-gateway/
  eureka-server/
  board-service/      # Gerenciamento de boards
  pin-service/        # Gerenciamento de pins
  user-service/       # Gerenciamento de usuários
  follow-service/     # Gerenciamento de seguidores
  pin-board-service/  # Relacionamento entre pins e boards
frontend/             # Aplicação web em React
mobile/               # Aplicação Android (Jetpack Compose)
```

## Como Executar

> **Pré-requisitos:** Java 21+, Node.js, Docker (opcional para banco de dados), Android Studio (opcional para mobile)

### Backend

```bash
cd services/eureka-server
./mvnw spring-boot:run
# Repita para os outros serviços (user-service, pin-service, etc.)
```

### Frontend Web

```bash
cd frontend
npm install
npm run dev
```

### Mobile

Abra o projeto `mobile/` no Android Studio e execute em um emulador ou dispositivo físico.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).
