import React, { useState, useEffect } from 'react';
import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import { fetchBudgets } from '../services/BudgetService';

const Budgets = () => {
    const [budgets, setBudgets] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await fetchBudgets();
                setBudgets(data);
            } catch (error) {
                console.error('Failed to fetch budgets:', error);
                setError('Failed to fetch budgets');
            }
        };
        fetchData();
    }, []);

    return (
        <Box sx={{ width: '100%', marginTop: 8 }}>
            <Typography variant="h4" gutterBottom>
                Budgets
            </Typography>
            {error && <Typography color="error">{error}</Typography>}
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="budgets table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Budget</TableCell>
                            <TableCell align="right">Total Limit</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {budgets.map((budget) => (
                            <TableRow key={budget.id}>
                                <TableCell>{budget.name}</TableCell>
                                <TableCell align="right">{budget.budgetLimits.reduce((sum, limit) => sum + parseFloat(limit.limitAmount), 0).toFixed(2)}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
};

export default Budgets;
