import React, { useState, useEffect } from 'react';
import './Weather.css';
import {
    Button,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    IconButton,
    CircularProgress,
    Snackbar,
    Alert
} from '@mui/material';
import SettingsIcon from '@mui/icons-material/Settings';

const Weather = () => {
    const [weatherData, setWeatherData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [city, setCity] = useState(() => {
        return localStorage.getItem('selectedCity') || 'Emden';
    });
    const [dialogOpen, setDialogOpen] = useState(false);
    const [newCity, setNewCity] = useState('');
    const [saving, setSaving] = useState(false);
    const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

    useEffect(() => {
        const fetchWeather = async () => {
            try {
                setLoading(true);
                setError(null);
                const response = await fetch(`http://localhost:8080/api/weather?city=${encodeURIComponent(city)}`);
                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.message || 'Failed to fetch weather data, check city name');
                }
                const data = await response.json();
                setWeatherData(data);
            } catch (err) {
                setError(err.message);
                setWeatherData(null);
            } finally {
                setLoading(false);
            }
        };

        fetchWeather();
        const interval = setInterval(fetchWeather, 300000);
        return () => clearInterval(interval);
    }, [city]);

    const handleDialogOpen = () => {
        setNewCity('');
        setDialogOpen(true);
    };

    const handleDialogClose = () => {
        setDialogOpen(false);
    };

    const handleSnackbarClose = () => {
        setSnackbar({ ...snackbar, open: false });
    };

    const handleCityChange = (e) => {
        setNewCity(e.target.value);
    };

    const handleCitySave = async () => {
        if (!newCity.trim()) {
            setSnackbar({ open: true, message: 'City must not be empty', severity: 'error' });
            return;
        }
        setSaving(true);
        try {
            const response = await fetch('http://localhost:8080/api/weather/city', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newCity.trim())
            });
            const result = await response.json();
            if (!response.ok) {
                throw new Error(result.error || 'Failed to update city');
            }
            setCity(newCity.trim());
            localStorage.setItem('selectedCity', newCity.trim());
            setSnackbar({ open: true, message: 'City updated successfully!', severity: 'success' });
            setDialogOpen(false);
        } catch (err) {
            setSnackbar({ open: true, message: err.message, severity: 'error' });
        } finally {
            setSaving(false);
        }
    };

    if (loading) return <div className="weather-container">Loading weather data...</div>;
    if (error) return (
        <div className="weather-container error">
            <div className="error-message">Error: {error}</div>
            <Button variant="outlined" startIcon={<SettingsIcon />} onClick={handleDialogOpen}>
                Config
            </Button>
            <Dialog open={dialogOpen} onClose={handleDialogClose}>
                <DialogTitle>Change City</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="City"
                        type="text"
                        fullWidth
                        value={newCity}
                        onChange={handleCityChange}
                        disabled={saving}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDialogClose} disabled={saving}>Cancel</Button>
                    <Button onClick={handleCitySave} disabled={saving} variant="contained">Save</Button>
                </DialogActions>
            </Dialog>
            <Snackbar open={snackbar.open} autoHideDuration={4000} onClose={handleSnackbarClose}>
                <Alert onClose={handleSnackbarClose} severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </div>
    );
    if (!weatherData) return null;

    return (
        <div className="weather-container">
            <div className="city-selector" style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 16 }}>
                <Button variant="outlined" startIcon={<SettingsIcon />} onClick={handleDialogOpen}>
                    Config
                </Button>
            </div>
            <Dialog open={dialogOpen} onClose={handleDialogClose}>
                <DialogTitle>Change City</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="City"
                        type="text"
                        fullWidth
                        value={newCity}
                        onChange={handleCityChange}
                        disabled={saving}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDialogClose} disabled={saving}>Cancel</Button>
                    <Button onClick={handleCitySave} disabled={saving} variant="contained">Save</Button>
                </DialogActions>
            </Dialog>
            <Snackbar open={snackbar.open} autoHideDuration={4000} onClose={handleSnackbarClose}>
                <Alert onClose={handleSnackbarClose} severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
            <h2>Current Weather in {city}</h2>
            <div className="weather-info">
                <div className="temperature">
                    <span className="value">{weatherData.temperature}°C</span>
                </div>
                <div className="rain-status">
                    <span className={`status ${weatherData.is_raining ? 'raining' : 'not-raining'}`}>
                        {weatherData.is_raining ? 'Currently Raining' : 'Not Raining'}
                    </span>
                </div>
            </div>
        </div>
    );
};

export default Weather; 