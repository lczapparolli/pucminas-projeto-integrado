import React from 'react';
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";

// Páginas da aplicação
import About from './page/About';
import Home from './page/Home';

// Definição das rotas
const router = createBrowserRouter([
  { path: '/'     , element: <Home />  },
  { path: '/about', element: <About /> }
]);

function App() {
  return (
    <RouterProvider router={router} />
  );
}

export default App;