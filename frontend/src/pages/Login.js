import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

const Login = () => {
    return (
        <form>
            <TextField label="Username" variant="outlined" />
            <TextField label="Password" type="password" variant="outlined" />
            <Button type="submit" variant="contained" color="primary">
                Login
            </Button>
        </form>
    );
};

export default Login;
