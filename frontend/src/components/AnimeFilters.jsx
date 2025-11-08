import React, { useState } from 'react';

const AnimeFilters = ({ onFilterChange, onClearFilters }) => {
  const [filters, setFilters] = useState({
    genero: '',
    classificacao: '',
    status: '',
    palavraChave: ''
  });

  // UI em PT, value em EN (o que o backend espera)
  const generoOptions = [
    { value: 'Action',        label: 'Ação' },
    { value: 'Adventure',     label: 'Aventura' },
    { value: 'Comedy',        label: 'Comédia' },
    { value: 'Drama',         label: 'Drama' },
    { value: 'Fantasy',       label: 'Fantasia' },
    { value: 'Horror',        label: 'Terror' },
    { value: 'Mystery',       label: 'Mistério' },
    { value: 'Romance',       label: 'Romance' },
    { value: 'Sci-Fi',        label: 'Ficção científica' },
    { value: 'Slice of Life', label: 'Cotidiano' },
    { value: 'Sports',        label: 'Esportes' },
    { value: 'Supernatural',  label: 'Sobrenatural' },
  ];

  // Permitidas (mantém valor original para filtrar no backend)
  const classificacoes = [
    { value: 'G - All Ages',                 label: 'Livre (todas as idades)' },
    { value: 'PG - Children',                label: 'PG - Crianças' },
    { value: 'PG-13 - Teens 13 or older',    label: 'PG-13 - 13+ anos' },
    { value: 'R - 17+ (violence & profanity)', label: 'R - 17+ (violência e linguagem)' },
  ];

  const statusOptions = [
    { value: 'Currently Airing', label: 'Em exibição' },
    { value: 'Finished Airing',  label: 'Finalizado' },
    { value: 'Not yet aired',    label: 'Ainda não lançado' },
  ];

  const handleFilterChange = (filterType, value) => {
    const newFilters = { ...filters, [filterType]: value };
    setFilters(newFilters);

    const hasActiveFilters =
      newFilters.genero ||
      newFilters.classificacao ||
      newFilters.status ||
      newFilters.palavraChave.trim();

    if (hasActiveFilters) {
      onFilterChange(newFilters); // envia values (EN) para o backend
    } else {
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
          {generoOptions.map((g) => (
            <option key={g.value} value={g.value}>{g.label}</option>
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
          {classificacoes.map((c) => (
            <option key={c.value} value={c.value}>{c.label}</option>
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
          {statusOptions.map((s) => (
            <option key={s.value} value={s.value}>{s.label}</option>
          ))}
        </select>
      </div>

      <button
        className="clear-filters-btn"
        onClick={handleClearFilters}
        disabled={
          !filters.genero &&
          !filters.classificacao &&
          !filters.status &&
          !filters.palavraChave.trim()
        }
      >
        Limpar Filtros
      </button>
    </div>
  );
};

export default AnimeFilters;
