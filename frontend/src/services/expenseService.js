import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/user/expenses';
const headers = () => ({
    Authorization: `Bearer ${localStorage.getItem('token')}`
});

export const getExpenses = async () => {
    try {
        const response = await axios.get(API_BASE_URL, { headers: headers() });
        return response.data;
    } catch (error) {
        console.error('There was an error fetching the expenses:', error.response);
        throw error;
    }
};

export const createExpense = async (expenseData) => {
    try {
        const response = await axios.post(API_BASE_URL, expenseData, { headers: headers() });
        return response.data;
    } catch (error) {
        console.error('Error creating expense:', error.response);
        throw error;
    }
};

export const updateExpense = async (id, expenseData) => {
    try {
        const response = await axios.put(`${API_BASE_URL}/${id}`, expenseData, { headers: headers() });
        return response.data;
    } catch (error) {
        console.error('Error updating expense:', error.response);
        throw error;
    }
};

export const deleteExpense = async (id) => {
    try {
        const response = await axios.delete(`${API_BASE_URL}/${id}`, { headers: headers() });
        return response.data;
    } catch (error) {
        console.error('Error deleting expense:', error.response);
        throw error;
    }
};
