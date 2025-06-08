import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Typography,
  Container,
  CssBaseline,
  ThemeProvider,
  createTheme,
} from '@mui/material';
import DeviceList from './components/DeviceList';
import DeviceDetails from './components/DeviceDetails';
import Weather from './components/Weather';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

function App() {
  const [selectedDevice, setSelectedDevice] = useState(null);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <AppBar position="static">
          <Toolbar>
            <Typography variant="h6" component={Link} to="/" sx={{ textDecoration: 'none', color: 'white' }}>
              Plant Monitoring
            </Typography>
          </Toolbar>
        </AppBar>
        <Container maxWidth="xl">
          <Weather />
          <Routes>
            <Route path="/" element={<DeviceList />} />
            <Route path="/device/:deviceId" element={<DeviceDetails />} />
          </Routes>
        </Container>
      </Router>
    </ThemeProvider>
  );
}

export default App;
