import React, { useState } from 'react';
import { IconButton, Menu, MenuItem, Avatar, Typography, Divider } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const ProfileMenu = () => {
    const [anchorEl, setAnchorEl] = useState(null);
    const navigate = useNavigate();

    const handleMenuOpen = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    const handleLogout = () => {
        // Perform logout operations
        handleMenuClose();
        navigate('/login');
    };

    return (
        <div>
            <IconButton onClick={handleMenuOpen} size="large" sx={{ ml: 2 }}>
                <Avatar alt="User" src="/static/images/avatar/1.jpg" />
            </IconButton>
            <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
            >
                <MenuItem disabled>
                    <Typography variant="subtitle1">Test User</Typography>
                </MenuItem>
                <MenuItem disabled>
                    <Typography variant="body2" color="textSecondary">
                        test@email.com
                    </Typography>
                </MenuItem>
                <Divider />
                <MenuItem onClick={handleMenuClose}>Home</MenuItem>
                <MenuItem onClick={handleLogout}>Login</MenuItem>
            </Menu>
        </div>
    );
};

export default ProfileMenu;
