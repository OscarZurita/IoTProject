import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import {
  Box,
  Paper,
  Typography,
  Grid,
  Card,
  CardContent,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  CircularProgress,
} from '@mui/material';
import axios from 'axios';

const DeviceDetails = () => {
  const { deviceId } = useParams();
  const [deviceData, setDeviceData] = useState(null);
  const [history, setHistory] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [totalItems, setTotalItems] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchDeviceData = async () => {
    try {
      setLoading(true);
      setError(null);
      const [latestResponse, historyResponse] = await Promise.all([
        axios.get(`http://localhost:8080/api/sensor-data/device/${deviceId}/latest`),
        axios.get(`http://localhost:8080/api/sensor-data/device/${deviceId}?page=${page}&size=${rowsPerPage}`)
      ]);
      setDeviceData(latestResponse.data);
      setHistory(historyResponse.data.content);
      setTotalItems(historyResponse.data.totalElements);
    } catch (error) {
      console.error('Error fetching device data:', error);
      setError('Failed to load device data. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (deviceId) {
      fetchDeviceData();
    }
  }, [deviceId, page, rowsPerPage]);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography variant="h6" color="error">
          {error}
        </Typography>
      </Box>
    );
  }

  if (!deviceData) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography variant="h6" color="error">
          Device not found
        </Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h5" gutterBottom>
        Device {deviceId} Details
      </Typography>

      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
                Temperature
              </Typography>
              <Typography variant="h4">
                {deviceData.temperature.toFixed(1)}°C
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
                Moisture
              </Typography>
              <Typography variant="h4">
                {deviceData.moisture.toFixed(1)}%
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
                Light
              </Typography>
              <Typography variant="h4">
                {deviceData.light.toFixed(1)} lux
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Typography variant="h6" gutterBottom>
        History
      </Typography>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Timestamp</TableCell>
              <TableCell>Temperature (°C)</TableCell>
              <TableCell>Moisture (%)</TableCell>
              <TableCell>Light (lux)</TableCell>
              <TableCell>Watering Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {history.map((record) => (
              <TableRow key={record.id}>
                <TableCell>
                  {new Date(record.timestamp).toLocaleString()}
                </TableCell>
                <TableCell>{record.temperature.toFixed(1)}</TableCell>
                <TableCell>{record.moisture.toFixed(1)}</TableCell>
                <TableCell>{record.light.toFixed(1)}</TableCell>
                <TableCell>
                  {record.wateringStatus ? 'ON' : 'OFF'}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25]}
          component="div"
          count={totalItems}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>
    </Box>
  );
};

export default DeviceDetails; 