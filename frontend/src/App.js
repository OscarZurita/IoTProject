import React, { useEffect, useState } from 'react';
import './App.css';

function App() {
  const [log, setLog] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch('/api/sensor-data/latest')
      .then((res) => {
        if (!res.ok) throw new Error('No log found');
        return res.json();
      })
      .then((data) => {
        setLog(data);
        setLoading(false);
      })
      .catch((err) => {
        setError('No log data available.');
        setLoading(false);
      });
  }, []);

  return (
    <div className="container">
      <h1>Latest Microcontroller Log</h1>
      <div className="log">
        {loading && 'Loading...'}
        {error && error}
        {log && !error && (
          <>
            <div><span className="label">Timestamp:</span> {log.timestamp || 'N/A'}</div>
            <div><span className="label">Temperature:</span> {log.temperature} °C</div>
            <div><span className="label">Moisture:</span> {log.moisture} %</div>
            <div><span className="label">Light:</span> {log.light} lux</div>
            <div><span className="label">Watering Status:</span> {log.wateringStatus ? 'ON' : 'OFF'}</div>
          </>
        )}
      </div>
    </div>
  );
}

export default App;
