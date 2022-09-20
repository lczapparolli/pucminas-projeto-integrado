import React from 'react';

import { Link } from 'react-router-dom';

import Nav from 'react-bootstrap/Nav';
import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';

function Layout(props: React.PropsWithChildren): any {
  return (
    <Container>
      <Navbar>
        <Container>
          <Navbar.Brand as={Link} to="/">Início</Navbar.Brand>
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