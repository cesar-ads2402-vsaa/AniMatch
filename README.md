<h1 align="center">AniMatch</h1>

Este projeto faz parte da cadeira de **Programação Orientada à Objeto**, do 3° período do curso de Análise e Desenvolvimento de Sistemas.  
A proposta do projeto é desenvolver um site voltado àqueles que buscam animes, com funcionalidade de recomendação com IA por base em gostos predefinidos utilizando a linguagem ```Java```.

## Sumário
1. [Integrantes](#integrantes)  
2. [Tema Escolhido](#tema-escolhido)  
3. [Desafio](#desafio)  
4. [Objetivos](#objetivos)  
5. [Requisitos](#requisitos)
6. [Issues](#issues)
7. [Como Contribuir](#como-contribuir)
8. [Entregas](#entregas)
9. [Licença](#licença)
10. [Como Rodar O Projeto](#como-rodar-o-projeto) 

---

## Integrantes

- ### Arthur Borba Lins  
- ### Bruno Dornelas Costa Ciro da Penha  
- ### Felipe Cisneiros Agostinho  
- ### Júlia Oliveira Veríssimo  
- ### Michelangelo Morais do Rego  
- ### Paulo Henrique Alves de Barros Pereira  
- ### Victor Simas Azevedo de Almeida  

---

## Tema escolhido
**Site de Recomendação de Animes com IA**

---

## Desafio
**Desenvolver um site voltado àqueles que buscam animes, com funcionalidade de recomendação com IA por base em gostos predefinidos.**

---

## Objetivos
- Criar um site responsivo para recomendação de animes.
- Implementar sistema de recomendação baseado em gostos pré-definidos.
- Garantir integração entre backend em Java e frontend em React.

---

## Requisitos
Para ver as especificações do projeto, veja o [arquivo de especificações](./docs/SPECIFICATIONS.md).

---

## Issues
Acompanhe o planejamento e as issues deste projeto na seção de [Issues](https://github.com/orgs/cesar-ads2402-vsaa/projects/3).

---

## Como Contribuir
Confira o [Contributing](./docs/CONTRIBUTING.md) para mais informações de como contribuir no projeto, bem como informações sobre inicialização e boas práticas para desenvolvimento do projeto.

---
## Entregas

### Entrega 01
- **Dia 09/09**
- Protótipo navegável no Figma: [Clique aqui para acessar](https://www.figma.com/proto/eSigW1lnlTjWfLkQKmEQtu/AniMatch?t=c5YbvD3IlAGpNBqu-1&node-id=3-2&starting-point-node-id=3%3A2)  
- Histórias propostas no Trello: [Clique aqui para acessar](https://trello.com/b/gJjPryz0/animatch)  
- Documento com as histórias propostas no Trello: [Clique aqui para acessar](https://drive.google.com/drive/folders/1IHKS6pm2pFUY_g6aT9TqggyUjyL3GvmE?usp=drive_link)  
- Screencast do protótipo Lo-fi: [Clique aqui para acessar](https://www.youtube.com/watch?v=shaaQ5WRWR8)

### Entrega 02
- **Dia 30/09**
- Histórias implementadas: [Clique aqui para acessar](https://docs.google.com/document/d/1y94APl-Q7mjcfqLj36QyZZkEuVCdV_nsnwYIdZMIuy4/edit?tab=t.0)
- Print do bug tracker:  
  <img width="1365" height="767" alt="image" src="https://github.com/user-attachments/assets/d2e26d95-1a20-487e-b258-0cb8645d66dd" />
- Screencast do sistema: [Clique aqui para acessar](https://www.youtube.com/watch?v=IKB8UQPakdo)

### Entrega 03
- **Dia 28/10**
- Histórias implementadas: [Clique aqui para acessar](https://docs.google.com/document/d/1pVNqIl5-YHWlRt9GD5kzm-vwBTC0tY3vseSQLEO9Iok/edit?tab=t.0)
- Print do bug tracker atualizado:  
  <img width="1919" height="911" alt="Captura de tela 2025-10-28 112258" src="https://github.com/user-attachments/assets/c6b95be5-8c3f-4edc-a630-0614981f0536" />
- Screencast do sistema: [Clique aqui para acessar](https://www.youtube.com/watch?v=8uwtXBvhzFs)
- Screencast dos Testes automatizados: [Clique aqui para acessar](https://www.youtube.com/watch?v=Hm63CztdTTk)
- Quadro do Trello atualizado: [Clique aqui para acessar](https://trello.com/b/gJjPryz0/animatch)

### Entrega 04
- **Dia 17/11**
- Histórias implementadas: [Clique aqui para acessar](https://docs.google.com/document/d/1L3-7TUe1X7KwaPHbsdD2ZhPy57GWovIw7peYKIT6Tbo/edit?tab=t.0)
- Print do bug tracker atualizado:  
 <img width="1919" height="886" alt="Captura de tela 2025-11-17 121917" src="https://github.com/user-attachments/assets/66c9592f-4314-4893-8ea6-7e2bf8948bc6" />

- Quadro do Trello atualizado: [Clique aqui para acessar](https://trello.com/b/gJjPryz0/animatch)
## Licença
Esse projeto está licenciado sob a [Licença MIT](./LICENSE). 

---
### Como rodar o projeto

#### Pré-requisitos
- **Java 17** ou superior
- **Maven** instalado
- **Node.js** e **npm** instalados

#### Passo 1: Rodando o Backend
No diretório `backend`, siga os passos abaixo:

```bash
# Execute o backend com o Spring Boot
mvn spring-boot:run
```

#### Passo 2: Rodando o Frontend
No diretório `frontend`, siga os passos abaixo:

```bash
# Instale as dependências do projeto
npm install

# Instale o axios (necessário para chamadas HTTP)
npm install axios

# Inicie o servidor de desenvolvimento
npm run dev
```

#### Testes: Como rodar
No diretório `backend`, siga os passos abaixo:

```bash
# Execute os testes com o Maven
mvn test
```

