import React from 'react';
import {Box, List, ListItemButton, ListItemIcon, ListItemText, Drawer, Toolbar, Typography} from '@mui/material';
import { Home, AccountBalanceWallet, TrendingUp, Settings } from '@mui/icons-material';
import {useNavigate} from "react-router-dom";

const drawerWidth = 240;

const Sidebar = () => {
    const navigate = useNavigate();

    return (
        <Drawer
            variant="permanent"
            sx={{
                width: drawerWidth,
                height: '100vh',
                flexShrink: 0,
                [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box', height: '100vh' },
            }}
        >
            <Toolbar
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                }}
            >
                <Typography variant="h6" noWrap>
                    MoneyMindful
                </Typography>
            </Toolbar>
            <Box sx={{ overflow: 'auto' }}>
                <List>
                    <ListItemButton onClick={() => navigate('/')} key="Home">
                        <ListItemIcon>
                            <Home />
                        </ListItemIcon>
                        <ListItemText primary="Overview" />
                    </ListItemButton>
                    <ListItemButton onClick={() => navigate('/budgets')} key="Budgets">
                        <ListItemIcon>
                            <AccountBalanceWallet />
                        </ListItemIcon>
                        <ListItemText primary="Budgets" />
                    </ListItemButton>
                    <ListItemButton onClick={() => navigate('/expenses')} key="Expenses">
                        <ListItemIcon>
                            <TrendingUp />
                        </ListItemIcon>
                        <ListItemText primary="Expenses" />
                    </ListItemButton>
                    <ListItemButton onClick={() => navigate('/savingsgoals')} key="SavingsGoals">
                        <ListItemIcon>
                            <TrendingUp />
                        </ListItemIcon>
                        <ListItemText primary="Savings Goals" />
                    </ListItemButton>
                    <ListItemButton key="Settings">
                        <ListItemIcon>
                            <Settings />
                        </ListItemIcon>
                        <ListItemText primary="Settings" />
                    </ListItemButton>
                </List>
            </Box>
        </Drawer>
    );
};

export default Sidebar;
