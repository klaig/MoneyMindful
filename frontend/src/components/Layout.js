import React from 'react';
import {Box, CssBaseline, Toolbar} from '@mui/material';
import TopBar from './TopBar';
import Sidebar from './Sidebar';

const drawerWidth = 40;

const Layout = ({ children }) => {
    return (
        <Box sx={{ display: 'flex' }}>
            <CssBaseline />
            <TopBar />
            <Sidebar />
            <Box component="main" sx={{ flexGrow: 1, p: 3, ml: `${drawerWidth}px`, mr: `${drawerWidth}px`, width: `calc(100% - ${drawerWidth * 2}px)` }}>
                <Toolbar />
                {children}
            </Box>
        </Box>
    );
};

export default Layout;
