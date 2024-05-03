import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Login from './pages/Login';
import Expenses from "./pages/Expenses";
import Dashboard from "./pages/Dashboard";
import Budgets from "./pages/Budgets";
import SavingsGoals from "./pages/SavingsGoals";

function App() {
    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/expenses" element={<Expenses />} />
                    <Route path="/" element={<Dashboard />} />
                    <Route path="/budgets" element={<Budgets />} />
                    <Route path="/savingsgoals" element={<SavingsGoals />} />
                </Routes>
            </Layout>
        </Router>
    );
}

export default App;
