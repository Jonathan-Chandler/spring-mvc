import React, {useState, useEffect} from 'react'
import { useNavigate } from "react-router-dom";
import useAuth from "./AuthProvider.tsx";

export default function RegisterComponent(...props) {
	const { isAuthenticated, loading, register, error } = useAuth();
	const navigate = useNavigate();

	// username/password form data
	const [data, setData] = useState(
		{
			username: "test_user123",
			email: "test_user1@email.com",
			password: "password123",
		});
	const [registerAttempted, setRegisterAttempted] = useState(false);
	const [registerFailed, setRegisterFailed] = useState(false);

	// user clicked register button
	const handleRegister = async (e) => 
	{
		e.preventDefault();
        console.log("handle register submit");
		register(data.username, data.email, data.password);
		setRegisterAttempted(true);
	}

	// navigate to welcome if already authorized
	useEffect(() => 
	{
        if (isAuthenticated())
        {
            navigate("/welcome");
        }
	}, [isAuthenticated, navigate]);

	// display message if attempted to register
	useEffect(() => 
	{
		// failed to register if error was set and done loading
		if (!loading && registerAttempted)
		{
			if (error !== null && error !== "")
			{
				// show failed to register message
				setRegisterFailed(true);
			}
			else
			{
				// show register success message
				setRegisterFailed(false);
			}
		}
	}, [data.username, navigate, loading, registerAttempted, registerFailed, error]);

	// handle register form data update
	const handleDataChange = (e) => 
	{
		e.preventDefault();
		const value = e.target.value

		setData({
			...data,
			[e.target.name]: value
		});
	}

	if (!loading && registerAttempted && !registerFailed)
	{
		// new user registration was accepted
		return (
			<div>
				<h1>Register</h1>
				<div className="container">
					<p />Successfully registered new user {data.username}<p />Please go to the <a href="/login">login</a> page
				</div>
			</div>
		)
	}
	else
	{
		// display registration form and failure message if attempted to register and done loading
		return (
			<div>
				<h1>Register</h1>
				<div className="container">
					User Name: <input type="text" name="username" value={data.username} onChange={handleDataChange} />
					Email: <input type="text" name="email" value={data.email} onChange={handleDataChange} />
					Password: <input type="password" name="password" value={data.password} onChange={handleDataChange} />
					<button className="btn btn-success" onClick={handleRegister}>Register</button>
					{!loading && registerFailed && <div className="alert alert-warning">Registration failed</div>}
					{loading && <div className="alert alert-warning">Loading...</div>}
				</div>
			</div>
		)
	}
}


