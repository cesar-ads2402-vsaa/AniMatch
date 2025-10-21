# Sistema de Filtros de Animes - AniMatch

## Funcionalidades Implementadas

### ✅ Sistema de Filtros Completo

O sistema de filtros permite aos usuários buscar animes através de três critérios principais:

1. **Gênero**: Filtra animes por gênero específico (Action, Adventure, Comedy, Drama, etc.)
2. **Classificação Indicativa**: Filtra por faixa etária (G, PG, PG-13, R, R+, Rx)
3. **Status**: Filtra por status de exibição (Currently Airing, Finished Airing, Not yet aired)

### 🎯 Critérios de Aceitação Atendidos

- ✅ O usuário pode filtrar animes por gênero, classificação indicativa ou status
- ✅ O sistema exibe apenas os animes que atendem aos filtros selecionados
- ✅ O usuário pode limpar os filtros aplicados
- ✅ Busca em tempo real - os resultados são atualizados automaticamente ao selecionar filtros
- ✅ Interface responsiva e intuitiva

## Arquitetura da Solução

### Backend (Spring Boot)

#### 1. Controller (`AnimeController.java`)
```java
@GetMapping("/buscar")
public List<Anime> buscarAnimesComFiltros(
    @RequestParam(required = false) String genero,
    @RequestParam(required = false) String classificacao,
    @RequestParam(required = false) String status)
```

#### 2. Service (`AnimeService.java`)
```java
public List<Anime> buscarAnimesComFiltros(String genero, String classificacao, String status)
```

#### 3. Repository (`AnimeRepository.java`)
```java
@Query("SELECT a FROM Anime a WHERE " +
       "(:genero IS NULL OR :genero IN (SELECT g FROM a.generos g)) AND " +
       "(:classificacao IS NULL OR a.classificacao = :classificacao) AND " +
       "(:status IS NULL OR a.status = :status)")
List<Anime> findByFiltros(@Param("genero") String genero, 
                         @Param("classificacao") String classificacao, 
                         @Param("status") String status);
```

### Frontend (React)

#### 1. Componente de Filtros (`AnimeFilters.jsx`)
- Interface intuitiva com dropdowns para cada filtro
- Botão para limpar todos os filtros
- Estados controlados para cada filtro

#### 2. Página Principal (`Home.jsx`)
- Integração com o componente de filtros
- Busca em tempo real via API
- Exibição dos resultados filtrados
- Contador de resultados encontrados

## Como Usar

### 1. Acessar a Página Principal
- Navegue para a página inicial do AniMatch
- Os filtros estão localizados na barra lateral esquerda

### 2. Aplicar Filtros
- Selecione um ou mais filtros nos dropdowns:
  - **Gênero**: Escolha entre Action, Adventure, Comedy, Drama, Fantasy, Horror, Mystery, Romance, Sci-Fi, Slice of Life, Sports, Supernatural
  - **Classificação**: Selecione G, PG, PG-13, R, R+, Rx
  - **Status**: Escolha entre Currently Airing, Finished Airing, Not yet aired

### 3. Visualizar Resultados
- Os resultados são atualizados automaticamente
- O contador mostra quantos animes foram encontrados
- Cada card mostra informações básicas: título, status, classificação e gêneros

### 4. Limpar Filtros
- Clique no botão "Limpar Filtros" para remover todos os filtros aplicados
- Os resultados voltam a mostrar todos os animes disponíveis

## Tecnologias Utilizadas

### Backend
- **Spring Boot**: Framework principal
- **Spring Data JPA**: Para consultas no banco de dados
- **JPQL**: Para consultas customizadas com filtros múltiplos

### Frontend
- **React**: Biblioteca para interface de usuário
- **Axios**: Para requisições HTTP
- **CSS3**: Para estilização responsiva

## Melhorias Futuras Sugeridas

1. **Filtros Adicionais**:
   - Filtro por ano de lançamento
   - Filtro por nota mínima
   - Filtro por número de episódios

2. **Funcionalidades**:
   - Busca por texto (título)
   - Ordenação dos resultados
   - Paginação para grandes volumes de dados

3. **UX/UI**:
   - Filtros com múltipla seleção
   - Indicadores visuais de filtros ativos
   - Histórico de buscas recentes

## Estrutura de Arquivos

```
AniMatch-main/
├── backend/
│   └── src/main/java/com/example/animematch/
│       ├── controller/AnimeController.java
│       ├── service/AnimeService.java
│       └── repository/AnimeRepository.java
└── frontend/
    ├── src/components/AnimeFilters.jsx
    ├── pages/Home.jsx
    └── src/global.css
```

## Testando o Sistema

1. **Inicie o Backend**:
   ```bash
   cd AniMatch-main/backend
   ./mvnw spring-boot:run
   ```

2. **Inicie o Frontend**:
   ```bash
   cd AniMatch-main/frontend
   npm install
   npm run dev
   ```

3. **Acesse**: `http://localhost:5173`

O sistema de filtros está totalmente funcional e pronto para uso!
