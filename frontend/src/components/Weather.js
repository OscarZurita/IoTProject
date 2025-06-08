import React, { useState, useEffect } from 'react';
import './Weather.css';

const Weather = () => {
    const [weatherData, setWeatherData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [city, setCity] = useState(() => {
        // Initialize from localStorage or default to 'Emden'
        return localStorage.getItem('selectedCity') || 'Emden';
    });
    const [customCity, setCustomCity] = useState('');

    useEffect(() => {
        const fetchWeather = async () => {
            try {
                setLoading(true);
                const response = await fetch(`http://localhost:8080/api/weather?city=${encodeURIComponent(city)}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch weather data, check city name');
                }
                const data = await response.json();
                setWeatherData(data);
                setLoading(false);
            } catch (err) {
                setError(err.message);
                setLoading(false);
            }
        };

        fetchWeather();
        // Refresh weather data every 5 minutes
        const interval = setInterval(fetchWeather, 300000);
        return () => clearInterval(interval);
    }, [city]);

    const handleCustomCitySubmit = (event) => {
        event.preventDefault();
        if (customCity.trim()) {
            const newCity = customCity.trim();
            setCity(newCity);
            localStorage.setItem('selectedCity', newCity);
            setCustomCity('');
        }
    };

    if (loading) return <div className="weather-container">Loading weather data...</div>;
    if (error) return <div className="weather-container error">Error: {error}</div>;
    if (!weatherData) return null;

    return (
        <div className="weather-container">
            <div className="city-selector">
                <form onSubmit={handleCustomCitySubmit} className="custom-city-form">
                    <input
                        type="text"
                        value={customCity}
                        onChange={(e) => setCustomCity(e.target.value)}
                        placeholder="Enter city name"
                        className="custom-city-input"
                    />
                    <button type="submit" className="custom-city-button">
                        Search
                    </button>
                </form>
            </div>
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