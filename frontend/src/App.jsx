import './global.css';
import {Home, AnimeDetail} from '../pages/index';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

export function App() {

  return (
    <div>
      <main>
        <Router>
          <main>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/anime/:id" element={<AnimeDetail />} />
            </Routes>
          </main>
        </Router>
      </main>
    </div>
  )
}