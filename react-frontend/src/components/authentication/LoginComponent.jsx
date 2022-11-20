import React, {useState, useEffect} from 'react'
import { useNavigate } from "react-router-dom";
import useAuth from "./AuthProvider.tsx";

export default function LoginComponent(...props) {
    const { isAuthenticated, loading, login } = useAuth();
    const navigate = useNavigate();

    // username/password form data
    const [data, setData] = useState(
    {
      username: "test_user123",
      password: "password123",
    });

    // set to true if user entered wrong username/password
    const [loginAttempted, setLoginAttempted] = useState(false);
    const [loginFailed, setLoginFailed] = useState(false);

    // do redirect and reset login failed status if login is valid
    useEffect(() => 
    {
		// auth is done loading
        if (!loading)
		{
			// already authenticated
			if (isAuthenticated())
			{
				navigate("/welcome");
			}
			else if (loginAttempted)
			{
				// set login
				setLoginFailed(true);
			}
		}
    }, [data.username, navigate, loading, loginAttempted, isAuthenticated]);

    // attempt login on submit
	const handleLogin = async (e) => 
    {
		e.preventDefault();
        login(data.username, data.password);
        setLoginAttempted(true)
    }
    
    // handle login form data update
    const handleLoginDataChange = (e) => 
    {
		e.preventDefault();

        const value = e.target.value
        setData({
            ...data,
            [e.target.name]: value
        });
    }

	return (
		<div>
			<h1>Login</h1>
			<div className="container">
				<form>
					User Name: <input type="text" name="username" value={data.username} onChange={handleLoginDataChange} />
					Password: <input type="password" name="password" value={data.password} onChange={handleLoginDataChange} />
					<button className="btn btn-success" onClick={handleLogin}>Login</button>
					{!loading && loginFailed && <div className="alert alert-warning">Invalid Credentials</div>}
					{loading && <div className="alert alert-warning">Loading...</div>}
				</form>
			</div>
		</div>
	)
}

