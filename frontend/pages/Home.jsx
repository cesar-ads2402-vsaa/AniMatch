import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import AnimeFilters from "../src/components/AnimeFilters";
import { API_URL } from "../src/config/api";

const Home = () => {
  const [animes, setAnimes] = useState([]);
  const [filteredAnimes, setFilteredAnimes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [currentFilters, setCurrentFilters] = useState({});
  const [error, setError] = useState("");

  useEffect(() => {
    carregarAnimes();
  }, []);

  const carregarAnimes = async () => {
    try {
      setLoading(true);
      setError("");
      const { data } = await axios.get(`${API_URL}/api/animes/temporada/atual`);
      const animesFiltrados = filtrarAnimesInadequados(data);
      setAnimes(animesFiltrados);
      setFilteredAnimes(animesFiltrados);
    } catch (err) {
      console.error("Erro ao carregar animes:", err);
      setError("Não foi possível carregar os animes agora. Tente novamente em instantes.");
    } finally {
      setLoading(false);
    }
  };

  // ======== Traduções ========

  const traduzirStatus = (status) => {
    if (!status) return "";
    const mapa = {
      "Currently Airing": "Em exibição",
      "Finished Airing": "Finalizado",
      "Not yet aired": "Ainda não lançado",
    };
    return mapa[status] || status;
  };

  const traduzirClassificacao = (cls) => {
    if (!cls) return "Sem classificação";
    const mapa = {
      "G - All Ages": "Livre (todas as idades)",
      "PG - Children": "PG - Crianças",
      "PG-13 - Teens 13 or older": "PG-13 - 13+ anos",
      "R - 17+ (violence & profanity)": "R - 17+ (violência e linguagem)",
      
    };
    return mapa[cls] || cls;
  };

  const traduzirGenero = (g) => {
    if (!g) return "";
    const mapa = {
      "Action": "Ação",
      "Adventure": "Aventura",
      "Comedy": "Comédia",
      "Drama": "Drama",
      "Fantasy": "Fantasia",
      "Horror": "Terror",
      "Mystery": "Mistério",
      "Romance": "Romance",
      "Sci-Fi": "Ficção científica",
      "Slice of Life": "Cotidiano",
      "Sports": "Esportes",
      "Supernatural": "Sobrenatural",
    };
    return mapa[g] || g;
  };

 

  const filtrarAnimesInadequados = (lista) => {
    const proibidas = ["R+ - Mild Nudity", "Rx - Hentai", "R+", "Rx", "Hentai"];
    return (lista || []).filter((anime) => {
      if (!anime?.classificacao) return true;
      const c = anime.classificacao.toLowerCase();
      return !proibidas.some((p) => c.toLowerCase().includes(p.toLowerCase()));
    });
  };

  const buscarComFiltros = async (filters) => {
    try {
      setLoading(true);
      setError("");
      setCurrentFilters(filters);

      const params = new URLSearchParams();
      
      if (filters.genero) params.append("genero", filters.genero);
      if (filters.classificacao) params.append("classificacao", filters.classificacao);
      if (filters.status) params.append("status", filters.status);
      if (filters.palavraChave?.trim()) {
        params.append("palavraChave", filters.palavraChave.trim());
      }

      const { data } = await axios.get(`${API_URL}/api/animes/buscar?${params.toString()}`);

      const animesFiltrados = filtrarAnimesInadequados(data);
      setFilteredAnimes(animesFiltrados);
    } catch (err) {
      console.error("Erro ao buscar animes com filtros:", err);
      setError("Falha ao buscar com os filtros. Revise os filtros ou tente novamente.");
    } finally {
      setLoading(false);
    }
  };

  const limparFiltros = () => {
    setFilteredAnimes(animes);
    setCurrentFilters({});
    setError("");
  };

  const getNoResultsMessage = () => {
    const hasSearchTerm = currentFilters.palavraChave?.trim();
    const hasOtherFilters =
      currentFilters.genero || currentFilters.classificacao || currentFilters.status;

    if (hasSearchTerm && hasOtherFilters) {
      return `Nenhum anime encontrado com o termo "${currentFilters.palavraChave}" e os filtros aplicados.`;
    } else if (hasSearchTerm) {
      return `Nenhum anime encontrado com o termo "${currentFilters.palavraChave}". Verifique se o nome está correto.`;
    } else if (hasOtherFilters) {
      return "Nenhum anime encontrado com os filtros aplicados.";
    }
    return "Nenhum anime encontrado.";
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
          {!loading && error && <p className="no-results">{error}</p>}

          <div className="banner-container">
            {filteredAnimes.map((anime) => (
              <div key={anime.id} className="banner">
                {/* IMAGEM + STATUS SOBREPOSTO */}
                <div className="cover">
                  <Link to={`/anime/${anime.id}`}>
                    <img
                      src={anime.imagens?.urlImagemMedia}
                      alt={anime.tituloPrincipal}
                      loading="lazy"
                    />
                  </Link>

                  <span className="status-badge">
                    {traduzirStatus(anime.status)}
                  </span>
                </div>

                {/* TÍTULO */}
                <h3>
                  <Link
                    to={`/anime/${anime.id}`}
                    style={{ textDecoration: "none", color: "inherit" }}
                  >
                    {anime.tituloPrincipal}
                  </Link>
                </h3>

                {/* INFO-BOX (gêneros + classificação) */}
                <div className="info-box">
                  <div className="genres">
                    {anime.generos?.slice(0, 3).map((g) => (
                      <span key={g} className="genre-tag">{traduzirGenero(g)}</span>
                    ))}
                  </div>

                  <span className="classification badge">
                    {traduzirClassificacao(anime.classificacao)}
                  </span>
                </div>
              </div>
            ))}
          </div>

          {!loading && !error && filteredAnimes.length === 0 && (
            <p className="no-results">{getNoResultsMessage()}</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Home;
