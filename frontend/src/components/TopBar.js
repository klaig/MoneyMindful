import React from 'react';
import { AppBar, Toolbar, Typography, Box } from '@mui/material';
import ProfileMenu from './ProfileMenu';

const drawerWidth = 240;

const TopBar = () => {
    return (
        <AppBar position="fixed" sx={{ width: `calc(100% - ${drawerWidth}px)`, ml: `${drawerWidth}px` }}>
            <Toolbar>
                <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1 }}>
                    MoneyMindful
                </Typography>
                <Box sx={{ display: 'flex' }}>
                    <ProfileMenu />
                </Box>
            </Toolbar>
        </AppBar>
    );
};

export default TopBar;
