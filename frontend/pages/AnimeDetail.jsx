import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { API_URL } from "../src/config/api";

function AnimeDetail() {
  const { id } = useParams();
  const [anime, setAnime] = useState(null);

  useEffect(() => {
    axios.get(`${API_URL}/api/animes/${id}`)
      .then(response => setAnime(response.data))
      .catch(error => console.error("Erro ao buscar anime:", error));
  }, [id]);

  if (!anime) {
    return <p>Carregando...</p>;
  }

  return (
    <div style={{ padding: "20px", maxWidth: "800px", margin: "0 auto" }}>
      <h1>{anime.tituloPrincipal}</h1>
    <div className="container-detalhes">
      <div>
      {anime.imagens && anime.imagens.urlImagemGrande && (
        <img 
          src={anime.imagens.urlImagemGrande} 
          alt={anime.tituloPrincipal} 
          style={{ width: "300px", borderRadius: "10px", marginBottom: "20px" }}
        />
      )}
      </div>
    <div>
        <p><strong>Status:</strong> {anime.status}</p>
        <p><strong>Classificação:</strong> {anime.classificacao}</p>
        <p><strong>Nota:</strong> {anime.nota}</p>
        <p><strong>Episódios:</strong> {anime.episodios}</p>
        <p><strong>Ano:</strong> {anime.ano}</p>
        <p><strong>Popularidade:</strong> {anime.popularidade}</p>

        <p><strong>Gêneros:</strong> {anime.generos.join(", ")}</p>
        <p><strong>Estúdios:</strong> {anime.estudios.join(", ")}</p>

        <p><strong>Sinopse:</strong></p>
        <p>{anime.sinopse}</p>

        {anime.periodoExibicao && (
          <p>
            <strong>Período de exibição:</strong> 
            {anime.periodoExibicao.dataInicio} até {anime.periodoExibicao.dataFim}
          </p>
        )}

        {anime.urlTrailer && (
          <p>
            <a href={anime.urlTrailer} target="_blank" rel="noopener noreferrer">
              Ver trailer
            </a>
          </p>
        )}
      </div>
    </div>
    </div>
  );
}

export default AnimeDetail;