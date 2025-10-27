import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import AnimeFilters from "../src/components/AnimeFilters";

const Home = () => {
  const [animes, setAnimes] = useState([]);
  const [filteredAnimes, setFilteredAnimes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [currentFilters, setCurrentFilters] = useState({});

  useEffect(() => {
    carregarAnimes();
  }, []);

  const carregarAnimes = () => {
    setLoading(true);
    axios.get("http://localhost:8080/api/animes")
      .then(response => {
        // Filtro de segurança adicional no frontend (backend já filtra)
        const animesFiltrados = filtrarAnimesInadequados(response.data);
        setAnimes(animesFiltrados);
        setFilteredAnimes(animesFiltrados);
        setLoading(false);
      })
      .catch(error => {
        console.error("Erro ao carregar animes:", error);
        setLoading(false);
      });
  };
  
  // Função para filtrar classificações proibidas no frontend
  const filtrarAnimesInadequados = (animes) => {
    const classificacoesProibidas = [
      'R+ - Mild Nudity',
      'Rx - Hentai',
      'R+',
      'Rx',
      'Hentai'
    ];
    
    return animes.filter(anime => {
      if (!anime.classificacao) return true;
      
      const classificacaoLower = anime.classificacao.toLowerCase();
      return !classificacoesProibidas.some(proibida => 
        classificacaoLower.includes(proibida.toLowerCase())
      );
    });
  };

  const buscarComFiltros = (filters) => {
    setLoading(true);
    setCurrentFilters(filters);
    
    const params = new URLSearchParams();
    if (filters.genero) params.append('genero', filters.genero);
    if (filters.classificacao) params.append('classificacao', filters.classificacao);
    if (filters.status) params.append('status', filters.status);
    if (filters.palavraChave && filters.palavraChave.trim()) {
      params.append('palavraChave', filters.palavraChave.trim());
    }

    const url = `http://localhost:8080/api/animes/buscar?${params.toString()}`;
    
    axios.get(url)
      .then(response => {
        // Filtro de segurança adicional no frontend
        const animesFiltrados = filtrarAnimesInadequados(response.data);
        setFilteredAnimes(animesFiltrados);
        setLoading(false);
      })
      .catch(error => {
        console.error("Erro ao buscar animes com filtros:", error);
        setLoading(false);
      });
  };

  const limparFiltros = () => {
    setFilteredAnimes(animes);
    setCurrentFilters({});
  };

  const getNoResultsMessage = () => {
    const hasSearchTerm = currentFilters.palavraChave && currentFilters.palavraChave.trim();
    const hasOtherFilters = currentFilters.genero || currentFilters.classificacao || currentFilters.status;
    
    if (hasSearchTerm && hasOtherFilters) {
      return `Nenhum anime encontrado com o termo "${currentFilters.palavraChave}" e os filtros aplicados.`;
    } else if (hasSearchTerm) {
      return `Nenhum anime encontrado com o termo "${currentFilters.palavraChave}". Verifique se o nome está correto.`;
    } else if (hasOtherFilters) {
      return "Nenhum anime encontrado com os filtros aplicados.";
    } else {
      return "Nenhum anime encontrado.";
    }
  };

  return (
    <div>
      <div className="main-container">
        <div className="filters-section">
          <AnimeFilters 
            onFilterChange={buscarComFiltros}
            onClearFilters={limparFiltros}
          />
        </div>
        
        <div className="results-section">
          <h2>Animes Encontrados ({filteredAnimes.length})</h2>
          
          {loading && <p>Carregando...</p>}
          
          <div className="banner-container">
            {filteredAnimes.map(anime => (
              <div key={anime.id} className="banner">
                <Link to={`/anime/${anime.id}`}>
                  <img
                    src={anime.imagens?.urlImagemMedia}
                    alt={anime.tituloPrincipal}
                    style={{ width: "100%", borderRadius: "8px" }}
                  />
                </Link>
                <h3 style={{ marginTop: "10px" }}>
                  <Link to={`/anime/${anime.id}`} style={{ textDecoration: "none", color: "inherit" }}>
                    {anime.tituloPrincipal}
                  </Link>
                </h3>
                <div className="anime-info">
                  <span className="status">{anime.status}</span>
                  <span className="classification">{anime.classificacao}</span>
                  <div className="genres">
                    {anime.generos?.slice(0, 3).map(genero => (
                      <span key={genero} className="genre-tag">{genero}</span>
                    ))}
                  </div>
                </div>
              </div>
            ))}
          </div>
          
          {!loading && filteredAnimes.length === 0 && (
            <p className="no-results">{getNoResultsMessage()}</p>
          )}
        </div>
      </div>
    </div>
  );
}

export default Home;