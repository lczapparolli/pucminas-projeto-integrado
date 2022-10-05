import React from 'react';

import { Link } from 'react-router-dom';

import Nav from 'react-bootstrap/Nav';
import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import Dropdown from 'react-bootstrap/Dropdown';
import NavItem from 'react-bootstrap/NavItem';
import NavLink from 'react-bootstrap/NavLink';

function Layout(props: React.PropsWithChildren): any {
  return (
    <Container>
      <Navbar>
        <Container>
          <Navbar.Brand as={Link} to="/">Início</Navbar.Brand>
          <Container>
            <Dropdown as={NavItem}>
              <Dropdown.Toggle as={NavLink}>Importações</Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item as={Link} to="/importacao/listar">Importações em progresso</Dropdown.Item>
                <Dropdown.Item as={Link} to="/importacao/novo">Nova importação</Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          </Container>
          <Nav>
            <Nav.Link as={Link} to="/about">Sobre</Nav.Link>
          </Nav>
        </Container>
      </Navbar>
      <Container>
        {props.children}
      </Container>
    </Container>
  );
}

export default Layout;