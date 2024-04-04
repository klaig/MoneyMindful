import React from 'react';
import { AppBar, Toolbar, Typography } from '@mui/material';

const drawerWidth = 240;

const TopBar = () => {
    return (
        <AppBar position="fixed" sx={{ width: `calc(100% - ${drawerWidth}px)`, ml: `${drawerWidth}px` }}>
            <Toolbar>
                <Typography variant="h6" noWrap>
                    MoneyMindful
                </Typography>
            </Toolbar>
        </AppBar>
    );
};

export default TopBar;
