import { useEffect, useState } from "react";
import axios from "axios";

const REVIEWS_STEP = 3; // quantidade que aparece por vez

export function ReviewsSection({ animeId }) {
  const [reviews, setReviews] = useState([]);
  const [visibleCount, setVisibleCount] = useState(REVIEWS_STEP);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function fetchReviews() {
      try {
        setLoading(true);
        setError("");

        const response = await axios.get(
          `https://animatch-backend.onrender.com/api/animes/${animeId}/reviews`
        );

        setReviews(response.data || []);
      } catch (err) {
        console.error(err);
        setError("Não foi possível carregar as reviews.");
      } finally {
        setLoading(false);
      }
    }

    if (animeId) {
      fetchReviews();
    }
  }, [animeId]);

  if (loading) {
    return (
      <section>
        <h2>Reviews</h2>
        <p>Carregando reviews...</p>
      </section>
    );
  }

  if (error) {
    return (
      <section>
        <h2>Reviews</h2>
        <p>{error}</p>
      </section>
    );
  }

  if (!reviews.length) {
    return (
      <section>
        <h2>Reviews</h2>
        <p>Ainda não existem avaliações para este anime.</p>
      </section>
    );
  }

  const visibleReviews = reviews.slice(0, visibleCount);

  return (
    <section className="reviews-section">
      <h2>Reviews</h2>

      <ul className="reviews-list">
        {visibleReviews.map((review) => (
          <li key={review.id} className="review-item">
            <strong className="review-author">
              {/* Ajuste se o back mandar outro formato, ex: review.user.username */}
              {review.author}
            </strong>
            <p className="review-comment">{review.comment}</p>
          </li>
        ))}
      </ul>

      {visibleCount < reviews.length && (
        <button
          className="btn-show-more"
          onClick={() => setVisibleCount((prev) => prev + REVIEWS_STEP)}
        >
          Ver mais reviews
        </button>
      )}
    </section>
  );
}