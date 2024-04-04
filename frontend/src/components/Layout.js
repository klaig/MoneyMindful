import React from 'react';
import {Box, CssBaseline, Toolbar} from '@mui/material';
import TopBar from './TopBar';
import Sidebar from './Sidebar';

const drawerWidth = 240;

const Layout = ({ children }) => {
    return (
        <Box sx={{ display: 'flex' }}>
            <CssBaseline />
            <TopBar />
            <Sidebar />
            <Box component="main" sx={{ flexGrow: 1, p: 3, marginLeft: `${drawerWidth}px`, width: `calc(100% - ${drawerWidth}px)` }}>
                <Toolbar />
                {children}
            </Box>
        </Box>
    );
};

export default Layout;
