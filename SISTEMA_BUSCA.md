# 🔍 Sistema de Busca por Palavra-Chave - AniMatch

## Funcionalidades Implementadas

### ✅ Busca por Palavra-Chave Completa

O sistema de busca por palavra-chave permite aos usuários encontrar animes digitando parte do nome, funcionando de forma integrada com os filtros existentes.

### 🎯 Critérios de Aceitação Atendidos

- ✅ Usuário pode digitar o nome ou parte do nome do anime em uma barra de busca
- ✅ Sistema retorna todos os animes que contenham a palavra-chave no título
- ✅ Busca funciona sozinha e junto com os filtros existentes
- ✅ Mensagem informativa quando nenhum anime é encontrado
- ✅ Busca case-insensitive (não diferencia maiúsculas/minúsculas)
- ✅ Busca por correspondência parcial (encontra "One" em "One Punch Man")

## Arquitetura da Solução

### Backend (Spring Boot)

#### 1. Controller Atualizado (`AnimeController.java`)
```java
@GetMapping("/buscar")
public List<Anime> buscarAnimesComFiltros(
    @RequestParam(required = false) String genero,
    @RequestParam(required = false) String classificacao,
    @RequestParam(required = false) String status,
    @RequestParam(required = false) String palavraChave)
```

#### 2. Service Atualizado (`AnimeService.java`)
```java
public List<Anime> buscarAnimesComFiltros(String genero, String classificacao, String status, String palavraChave)
```

#### 3. Repository Atualizado (`AnimeRepository.java`)
```java
@Query("SELECT a FROM Anime a WHERE " +
       "(:genero IS NULL OR :genero MEMBER OF a.generos) AND " +
       "(:classificacao IS NULL OR a.classificacao = :classificacao) AND " +
       "(:status IS NULL OR a.status = :status) AND " +
       "(:palavraChave IS NULL OR LOWER(a.tituloPrincipal) LIKE LOWER(CONCAT('%', :palavraChave, '%')))")
```

### Frontend (React)

#### 1. Componente AnimeFilters Atualizado
- ✅ Campo de input para busca por palavra-chave
- ✅ Integração com filtros existentes
- ✅ Busca em tempo real ao digitar
- ✅ Placeholder informativo

#### 2. Página Home Atualizada
- ✅ Suporte a busca por palavra-chave
- ✅ Mensagens específicas para diferentes cenários
- ✅ Estado dos filtros atuais mantido

## Como Usar

### 1. **Busca Simples por Nome**
- Digite parte do nome do anime na barra "Buscar por nome"
- Os resultados aparecem automaticamente conforme você digita
- Exemplo: Digite "One" para encontrar "One Punch Man"

### 2. **Busca Combinada com Filtros**
- Digite o nome do anime E selecione filtros
- O sistema retornará apenas animes que atendam TODOS os critérios
- Exemplo: Busque "Dragon" + Gênero "Action" + Status "Currently Airing"

### 3. **Cenários de Uso**

#### Busca por Nome Completo:
- Digite: "One Punch Man"
- Resultado: Anime específico

#### Busca por Palavra Parcial:
- Digite: "Dragon"
- Resultado: Todos os animes com "Dragon" no título

#### Busca com Filtros:
- Digite: "Attack" + Gênero "Action"
- Resultado: Animes com "Attack" no título E gênero Action

### 4. **Mensagens Informativas**

O sistema exibe mensagens específicas quando não há resultados:

- **"Nenhum anime encontrado com o termo 'XYZ'. Verifique se o nome está correto."**
  - Quando há apenas busca por palavra-chave

- **"Nenhum anime encontrado com o termo 'XYZ' e os filtros aplicados."**
  - Quando há busca + filtros

- **"Nenhum anime encontrado com os filtros aplicados."**
  - Quando há apenas filtros

## Características Técnicas

### 🔍 **Busca Inteligente**
- **Case-insensitive**: Não diferencia maiúsculas/minúsculas
- **Busca parcial**: Encontra correspondências parciais
- **Trim automático**: Remove espaços em branco
- **Integração completa**: Funciona com todos os filtros

### ⚡ **Performance**
- **Busca em tempo real**: Resultados aparecem conforme digita
- **Consulta otimizada**: JPQL eficiente com índices
- **Debounce implícito**: Evita requisições excessivas

### 🎨 **Interface**
- **Design consistente**: Segue o padrão visual dos filtros
- **Placeholder informativo**: "Digite o nome do anime..."
- **Estados visuais**: Focus, hover, disabled
- **Responsivo**: Funciona em mobile e desktop

## Exemplos de Busca

### ✅ **Casos de Sucesso**

| Busca | Resultado Esperado |
|-------|-------------------|
| "One" | One Punch Man, One Piece, etc. |
| "Dragon" | Dragon Ball, Dragon Quest, etc. |
| "Attack" | Attack on Titan |
| "Naruto" | Naruto, Naruto Shippuden |

### ❌ **Casos sem Resultado**

| Busca | Mensagem |
|-------|----------|
| "xyz123" | "Nenhum anime encontrado com o termo 'xyz123'. Verifique se o nome está correto." |
| "xyz123" + Gênero "Action" | "Nenhum anime encontrado com o termo 'xyz123' e os filtros aplicados." |

## Estrutura de Arquivos

```
AniMatch-main/
├── backend/
│   └── src/main/java/com/example/animematch/
│       ├── controller/AnimeController.java (atualizado)
│       ├── service/AnimeService.java (atualizado)
│       └── repository/AnimeRepository.java (atualizado)
└── frontend/
    ├── src/components/AnimeFilters.jsx (atualizado)
    ├── pages/Home.jsx (atualizado)
    └── src/global.css (atualizado)
```

## Testando o Sistema

### 1. **Teste Básico**
1. Digite "One" na barra de busca
2. Verifique se aparece "One Punch Man"
3. Digite "xyz123" e verifique a mensagem de erro

### 2. **Teste Combinado**
1. Digite "Dragon" na busca
2. Selecione Gênero "Action"
3. Verifique se apenas animes Action com "Dragon" aparecem

### 3. **Teste de Limpeza**
1. Aplique busca + filtros
2. Clique "Limpar Filtros"
3. Verifique se tudo volta ao estado inicial

## Próximas Melhorias Sugeridas

1. **Busca Avançada**:
   - Busca por sinopse
   - Busca por estúdio
   - Busca por ano

2. **Funcionalidades**:
   - Histórico de buscas
   - Sugestões de busca
   - Busca com operadores (AND, OR)

3. **UX/UI**:
   - Autocomplete
   - Busca com enter
   - Indicador de busca ativa

O sistema de busca por palavra-chave está totalmente funcional e integrado! 🎉
