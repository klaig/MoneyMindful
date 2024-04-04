import React, { useState, useEffect } from 'react';
import { getExpenses } from '../services/expenseService';
import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import { format } from 'date-fns';

const Expenses = () => {
    const [expenses, setExpenses] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await getExpenses();
                setExpenses(data);
            } catch (error) {
                setError('Error fetching expenses');
            }
        };
        fetchData();
    }, []);

    const formatDate = (dateString) => {
        return format(new Date(dateString), 'dd/MM/yyyy');
    };

    return (
        <Box sx={{ width: '100%', marginTop: 8 }}>
            <Typography variant="h4" gutterBottom>
                Expenses
            </Typography>
            {error && <Typography color="error">{error}</Typography>}
            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="expenses table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Category</TableCell>
                            <TableCell align="right">Amount (€)</TableCell>
                            <TableCell>Notes</TableCell>
                            <TableCell>Date</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {expenses.map((expense) => (
                            <TableRow key={expense.id}>
                                <TableCell>{expense.category}</TableCell>
                                <TableCell align="right">{expense.amount.toFixed(2)}</TableCell>
                                <TableCell>{expense.notes}</TableCell>
                                <TableCell>{formatDate(expense.dateTime)}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Box>
    );
};

export default Expenses;
