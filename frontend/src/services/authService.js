import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/auth";

export const login = async (username, password) => {
    try {
        const response = await axios.post(API_BASE_URL + '/login', { username, password });
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
        }
        return response.data;
    } catch (error) {
        console.error('Login error', error.response);
        throw error;
    }
};