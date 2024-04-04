import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/user/expenses';

export const getExpenses = async () => {
    try {
        const response = await axios.get(API_BASE_URL, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        return response.data;
    } catch (error) {
        console.error('There was an error fetching the expenses:', error.response);
        throw error;
    }
};
