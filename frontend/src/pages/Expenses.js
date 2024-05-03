import React, { useState, useEffect } from 'react';
import { getExpenses, updateExpense, deleteExpense } from '../services/expenseService';
import { Box, Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, IconButton, Menu, MenuItem, TextField } from '@mui/material';
import { format, parseISO } from 'date-fns';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import AddExpenseModal from '../components/AddExpenseModal';

const Expenses = () => {
    const [expenses, setExpenses] = useState([]);
    const [error, setError] = useState('');
    const [anchorEl, setAnchorEl] = useState(null);
    const [editableExpenseId, setEditableExpenseId] = useState(null);
    const [tempExpense, setTempExpense] = useState({});
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const data = await getExpenses();
            setExpenses(data);
        } catch (error) {
            console.error('Failed to fetch expenses:', error);
            setError('Failed to fetch expenses');
        }
    };

    const handleOpenAddModal = () => {
        setIsAddModalOpen(true);
    };

    const handleCloseAddModal = (shouldRefresh) => {
        setIsAddModalOpen(false);
        if (shouldRefresh) {
            fetchData();
        }
    };

    const handleMenuClick = (event, expense) => {
        setAnchorEl(event.currentTarget);
        setTempExpense(expense);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    const handleDelete = async () => {
        if (window.confirm("Are you sure you want to delete this entry?")) {
            await deleteExpense(tempExpense.id);
            fetchData();
        }
        handleMenuClose();
    };

    const handleEdit = () => {
        setEditableExpenseId(tempExpense.id);
        setTempExpense({
            ...tempExpense,
            dateTime: tempExpense.dateTime ? parseISO(tempExpense.dateTime) : new Date(), // Ensure it's a Date object
            categoryId: 1 // Hardcoding categoryId temporarily
        });
        handleMenuClose();
    };

    const handleUpdate = async () => {
        const updateData = {
            ...tempExpense,
            categoryId: 1, // Hardcoding categoryId until dynamic category selection is implemented
        };
        await updateExpense(updateData.id, updateData);
        setEditableExpenseId(null);
        fetchData();
    };

    const handleChange = (prop, value) => {
        setTempExpense({ ...tempExpense, [prop]: value });
    };

    const renderMenu = (
        <Menu
            id="simple-menu"
            anchorEl={anchorEl}
            keepMounted
            open={Boolean(anchorEl)}
            onClose={handleMenuClose}
        >
            <MenuItem onClick={handleEdit}>Update</MenuItem>
            <MenuItem onClick={handleDelete}>Delete</MenuItem>
        </Menu>
    );

    return (
        <LocalizationProvider dateAdapter={AdapterDateFns}>
            <Box sx={{ width: '100%', marginTop: 8 }}>
                <Typography variant="h4" gutterBottom>
                    Expenses
                </Typography>
                <Button variant="contained" color="primary" onClick={handleOpenAddModal}>
                    Add Expense
                </Button>
                <AddExpenseModal open={isAddModalOpen} onClose={handleCloseAddModal} />
                {error && <Typography color="error">{error}</Typography>}
                <TableContainer component={Paper}>
                    <Table sx={{ minWidth: 650 }} aria-label="expenses table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Category</TableCell>
                                <TableCell align="right">Amount (€)</TableCell>
                                <TableCell>Notes</TableCell>
                                <TableCell>Date</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {expenses.map((expense) => (
                                <TableRow key={expense.id}>
                                    {editableExpenseId === expense.id ? (
                                        <>
                                            <TableCell>
                                                <TextField
                                                    value={tempExpense.categoryName}
                                                    onChange={(e) => handleChange('categoryName', e.target.value)}
                                                />
                                            </TableCell>
                                            <TableCell align="right">
                                                <TextField
                                                    value={tempExpense.amount}
                                                    onChange={(e) => handleChange('amount', parseFloat(e.target.value))}
                                                    type="number"
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    value={tempExpense.notes}
                                                    onChange={(e) => handleChange('notes', e.target.value)}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <DatePicker
                                                    label="Select date"
                                                    value={tempExpense.dateTime}
                                                    onChange={(newDate) => handleChange('dateTime', newDate)}
                                                    components={{
                                                        OpenPickerIcon: MoreVertIcon,
                                                    }}
                                                    componentsProps={{
                                                        textField: {
                                                            variant: 'outlined',
                                                            fullWidth: true,
                                                        },
                                                    }}
                                                    renderInput={(params) => <TextField {...params} />}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <Button onClick={handleUpdate}>Confirm</Button>
                                            </TableCell>
                                        </>
                                    ) : (
                                        <>
                                            <TableCell>{expense.categoryName}</TableCell>
                                            <TableCell align="right">{expense.amount.toFixed(2)}</TableCell>
                                            <TableCell>{expense.notes}</TableCell>
                                            <TableCell>{format(parseISO(expense.dateTime), 'dd/MM/yyyy')}</TableCell>
                                            <TableCell>
                                                <IconButton onClick={(e) => handleMenuClick(e, expense)}>
                                                    <MoreVertIcon />
                                                </IconButton>
                                            </TableCell>
                                        </>
                                    )}
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
                {renderMenu}
            </Box>
        </LocalizationProvider>
    );
};

export default Expenses;
