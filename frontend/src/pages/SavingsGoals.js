import React, { useState, useEffect } from 'react';
import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import { format } from 'date-fns';
import { fetchSavingsGoals } from '../services/SavingsGoalService';

const SavingsGoals = () => {
    const [savingsGoals, setSavingsGoals] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await fetchSavingsGoals();
                setSavingsGoals(data);
            } catch (error) {
                console.error('Failed to fetch savings goals:', error);
                setError('Failed to fetch savings goals');
            }
        };
        fetchData();
    }, []);

    return (
        <Box sx={{ width: '100%', marginTop: 8 }}>
            <Typography variant="h4" gutterBottom>
                Savings Goals
            </Typography>
            {error && <Typography color="error">{error}</Typography>}
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="savings goals table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell align="right">Target Amount</TableCell>
                            <TableCell align="right">Current Amount</TableCell>
                            <TableCell>Target Date</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {savingsGoals.map((goal) => (
                            <TableRow key={goal.id}>
                                <TableCell>{goal.name}</TableCell>
                                <TableCell align="right">{goal.targetAmount.toFixed(2)}</TableCell>
                                <TableCell align="right">{goal.currentAmount.toFixed(2)}</TableCell>
                                <TableCell>{format(new Date(goal.targetDate), 'dd/MM/yyyy')}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
};

export default SavingsGoals;
