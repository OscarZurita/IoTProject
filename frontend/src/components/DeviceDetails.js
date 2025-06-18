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
  Chip,
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
      <Typography variant="h5" gutterBottom align="center" sx={{ mb: 4 }}>
        Device {deviceId} Details
      </Typography>

      <Grid container spacing={3} sx={{ mb: 4, justifyContent: 'center' }}>
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent sx={{ py: 3 }}>
              <Typography color="textSecondary" gutterBottom align="center">
                Moisture
              </Typography>
              <Typography variant="h4" align="center">
                {deviceData.moisture.toFixed(1)}%
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent sx={{ py: 3 }}>
              <Typography color="textSecondary" gutterBottom align="center">
                Light
              </Typography>
              <Typography variant="h4" align="center">
                {deviceData.light.toFixed(1)} lux
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom align="center">
                Watering Decision
              </Typography>
              <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2, mb: 1 }}>
                <Chip
                  label={deviceData.decision ? "Do Water" : "Don't Water"}
                  color={deviceData.decision ? "primary" : "success"}
                  variant="outlined"
                  sx={{ fontSize: '1.1rem', padding: '6px 16px' }}
                />
              </Box>
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
              <TableCell align="center">Server Timestamp</TableCell>
              <TableCell align="center">Device Timestamp</TableCell>
              <TableCell align="center">Moisture (%)</TableCell>
              <TableCell align="center">Light (lux)</TableCell>
              <TableCell align="center">Decision</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {history.map((record) => (
              <TableRow key={record.id}>
                <TableCell align="center">
                  {new Date(record.timestamp).toLocaleString()}
                </TableCell>
                <TableCell align="center">
                  {record.deviceTimestamp ? 
                    new Date(record.deviceTimestamp).toLocaleString() : 
                    'N/A'
                  }
                </TableCell>
                <TableCell align="center">{record.moisture.toFixed(1)}</TableCell>
                <TableCell align="center">{record.light.toFixed(1)}</TableCell>
                <TableCell align="center">
                  <Chip
                    label={record.decision ? "Do Water" : "Don't Water"}
                    color={record.decision ? "primary" : "success"}
                    size="small"
                    variant="outlined"
                  />
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