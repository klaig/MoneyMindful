import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Login from './pages/Login';
import Expenses from "./pages/Expenses";
import Dashboard from "./pages/Dashboard";

function App() {
    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/expenses" element={<Expenses />} />
                    <Route path="/dashboard" element={<Dashboard />} />
                </Routes>
            </Layout>
        </Router>
    );
}

export default App;
