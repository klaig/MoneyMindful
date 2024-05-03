import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, TextField, DialogActions, Button } from '@mui/material';
import { DesktopDatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { createExpense } from '../services/expenseService';

const AddExpenseModal = ({ open, onClose }) => {
    const [newExpense, setNewExpense] = useState({
        categoryId: 1, // Default category ID for demonstration
        amount: '',
        dateTime: new Date(),
        notes: ''
    });

    const handleChange = (field, value) => {
        setNewExpense({ ...newExpense, [field]: value });
    };

    const handleSave = async () => {
        if (newExpense.amount && newExpense.dateTime) {
            try {
                await createExpense(newExpense);
                onClose(true); // Refresh the list on successful add
            } catch (error) {
                console.error('Error creating new expense:', error);
            }
        } else {
            alert('Please fill in all fields');
        }
    };

    return (
        <Dialog open={open} onClose={() => onClose(false)}>
            <DialogTitle>Add New Expense</DialogTitle>
            <DialogContent>
                <TextField
                    autoFocus
                    margin="dense"
                    label="Amount"
                    type="number"
                    fullWidth
                    variant="outlined"
                    value={newExpense.amount}
                    onChange={(e) => handleChange('amount', e.target.value)}
                />
                <TextField
                    margin="dense"
                    label="Notes"
                    fullWidth
                    variant="outlined"
                    multiline
                    rows={4}
                    value={newExpense.notes}
                    onChange={(e) => handleChange('notes', e.target.value)}
                />
                <LocalizationProvider dateAdapter={AdapterDateFns}>
                    <DesktopDatePicker
                        label="Date"
                        inputFormat="MM/dd/yyyy"
                        value={newExpense.dateTime}
                        onChange={(date) => handleChange('dateTime', date)}
                        renderInput={(params) => <TextField {...params} />}
                    />
                </LocalizationProvider>
            </DialogContent>
            <DialogActions>
                <Button onClick={() => onClose(false)}>Cancel</Button>
                <Button onClick={handleSave} color="primary">Save</Button>
            </DialogActions>
        </Dialog>
    );
};

export default AddExpenseModal;
