import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/user/savingsgoals';

const getTokenHeader = () => {
    const token = localStorage.getItem('token');
    return { 'Authorization': `Bearer ${token}` };
};

export const fetchSavingsGoals = async () => {
    try {
        const response = await axios.get(API_BASE_URL, { headers: getTokenHeader() });
        return response.data;
    } catch (error) {
        console.error('There was an error fetching the savings goals:', error.response);
        throw error;
    }
};

export const createSavingsGoal = async (data) => {
    try {
        const response = await axios.post(API_BASE_URL, data, { headers: getTokenHeader() });
        return response.data;
    } catch (error) {
        console.error('Error creating savings goal:', error.response);
        throw error;
    }
};

