import React, { useState } from 'react';

const AnimeFilters = ({ onFilterChange, onClearFilters }) => {
  const [filters, setFilters] = useState({
    genero: '',
    classificacao: '',
    status: '',
    palavraChave: ''
  });

  const generos = [
    'Action', 'Adventure', 'Comedy', 'Drama', 'Fantasy', 'Horror', 
    'Mystery', 'Romance', 'Sci-Fi', 'Slice of Life', 'Sports', 'Supernatural'
  ];

  const classificacoes = [
    'G - All Ages', 'PG - Children', 'PG-13 - Teens 13 or older', 
    'R - 17+ (violence & profanity)', 'R+ - Mild Nudity', 'Rx - Hentai'
  ];

  const statusOptions = [
    'Currently Airing', 'Finished Airing', 'Not yet aired'
  ];

  const handleFilterChange = (filterType, value) => {
    const newFilters = { ...filters, [filterType]: value };
    setFilters(newFilters);
    
    // Só chama a busca se pelo menos um filtro estiver selecionado
    const hasActiveFilters = newFilters.genero || newFilters.classificacao || newFilters.status || newFilters.palavraChave.trim();
    if (hasActiveFilters) {
      onFilterChange(newFilters);
    } else {
      // Se não há filtros ativos, limpa os resultados
      onClearFilters();
    }
  };

  const handleClearFilters = () => {
    const clearedFilters = { genero: '', classificacao: '', status: '', palavraChave: '' };
    setFilters(clearedFilters);
    onClearFilters();
  };

  return (
    <div className="filters-container">
      <h3>Filtros de Busca</h3>
      
      <div className="filter-group">
        <label htmlFor="palavraChave">Buscar por nome:</label>
        <input
          id="palavraChave"
          type="text"
          placeholder="Digite o nome do anime..."
          value={filters.palavraChave}
          onChange={(e) => handleFilterChange('palavraChave', e.target.value)}
          className="search-input"
        />
      </div>

      <div className="filter-group">
        <label htmlFor="genero">Gênero:</label>
        <select 
          id="genero"
          value={filters.genero} 
          onChange={(e) => handleFilterChange('genero', e.target.value)}
        >
          <option value="">Todos os gêneros</option>
          {generos.map(genero => (
            <option key={genero} value={genero}>{genero}</option>
          ))}
        </select>
      </div>

      <div className="filter-group">
        <label htmlFor="classificacao">Classificação:</label>
        <select 
          id="classificacao"
          value={filters.classificacao} 
          onChange={(e) => handleFilterChange('classificacao', e.target.value)}
        >
          <option value="">Todas as classificações</option>
          {classificacoes.map(classificacao => (
            <option key={classificacao} value={classificacao}>{classificacao}</option>
          ))}
        </select>
      </div>

      <div className="filter-group">
        <label htmlFor="status">Status:</label>
        <select 
          id="status"
          value={filters.status} 
          onChange={(e) => handleFilterChange('status', e.target.value)}
        >
          <option value="">Todos os status</option>
          {statusOptions.map(status => (
            <option key={status} value={status}>{status}</option>
          ))}
        </select>
      </div>

      <button 
        className="clear-filters-btn" 
        onClick={handleClearFilters}
        disabled={!filters.genero && !filters.classificacao && !filters.status && !filters.palavraChave.trim()}
      >
        Limpar Filtros
      </button>
    </div>
  );
};

export default AnimeFilters;
