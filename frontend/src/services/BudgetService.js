import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/user/budgets';

const getTokenHeader = () => {
    const token = localStorage.getItem('token');
    return { 'Authorization': `Bearer ${token}` };
};

export const fetchBudgets = async () => {
    try {
        const response = await axios.get(API_BASE_URL, { headers: getTokenHeader() });
        return response.data;
    } catch (error) {
        console.error('There was an error fetching the budgets:', error.response);
        throw error;
    }
};

export const createBudget = async (data) => {
    try {
        const response = await axios.post(API_BASE_URL, data, { headers: getTokenHeader() });
        return response.data;
    } catch (error) {
        console.error('Error creating budget:', error.response);
        throw error;
    }
};

