import React from 'react';
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";

// Páginas da aplicação
import About from './page/About';
import Home from './page/Home';
import ImportacaoNovo from './page/importacao/ImportacaoNovo';
import ImportacaoListar from './page/importacao/ImportacaoListar';

// Definição das rotas
const router = createBrowserRouter([
  { path: '/'     , element: <Home />  },
  { path: '/about', element: <About /> },
  { path: '/importacao/novo', element: <ImportacaoNovo /> },
  { path: '/importacao/listar', element: <ImportacaoListar /> }
]);

function App() {
  return (
    <RouterProvider router={router} />
  );
}

export default App;
