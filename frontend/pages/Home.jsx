import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

const Home = () => {
  const [animes, setAnimes] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/api/animes")
      .then(response => {
        setAnimes(response.data);
      })
      .catch(error => {
        console.error("Erro ao carregar animes:", error);
      });
  }, []);

  return (
    <div>
        <div className="banner-container">
            {animes.map(anime => (
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
                </div>
            ))}
        </div>
    </div>
  );
}

export default Home;