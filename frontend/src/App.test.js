jest.mock('axios');

import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import App from './App';
import { BrowserRouter as Router } from 'react-router-dom';

test('renders Layout and main routes', () => {
  render(
      <Router>
        <App />
      </Router>
  );
  // Check for a specific element that is always rendered by the Layout component
  const overviewElement = screen.getByText(/overview/i);
  expect(overviewElement).toBeInTheDocument();

});
