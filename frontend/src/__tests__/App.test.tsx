import React from 'react';
import { render,  } from '@testing-library/react';
import App from '../App';

test('renderiza a aplicação sem quebrar', () => {
  render(<App />);
});