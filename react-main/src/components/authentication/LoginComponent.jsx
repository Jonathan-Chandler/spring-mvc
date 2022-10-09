import React, {useState, useEffect} from 'react'
import { useNavigate } from "react-router-dom";
import useAuth from "./AuthProvider.tsx";

export default function LoginComponent(...props) {
    const { username, token, apiSession, loading, error, login, signUp, logout } = useAuth();
    const navigate = useNavigate();

    // username/password form data
    const [data, setData] = useState(
    {
      username: "test_user1",
      password: "password3",
    });

    // set to true if user entered wrong username/password
    const [loginAttempted, setLoginAttempted] = useState(false);
    const [loginFailed, setLoginFailed] = useState(false);

    // attempt login on submit
    const handleSubmit = async () => 
    {
        login(data.username, data.password);
        setLoginAttempted(true)
    }
    
    // do redirect and reset login failed status if login is valid
    useEffect(() => 
    {
        // already authenticated
        if (!loading && token && token !== "")
        {
            //console.log("token: " + token)
            navigate("/welcome");
        }

        // tried login and no longer loading
        if (loginAttempted && !loading && (!token || token !== ""))
        {
            // set login
            setLoginFailed(true);
        }
    }, [data.username, navigate, loading, token, loginAttempted]);

    // handle login form data update
    const handleLoginDataChange = (event) => 
    {
        const value = event.target.value
        setData({
            ...data,
            [event.target.name]: value
        });
    }

    return (
        <div>
            <h1>Login</h1>
            <div className="container">
                {loginFailed && <div className="alert alert-warning">Invalid Credentials</div>}
                User Name: <input type="text" name="username" value={data.username} onChange={handleLoginDataChange} />
                Password: <input type="password" name="password" value={data.password} onChange={handleLoginDataChange} />
                <button className="btn btn-success" onClick={handleSubmit}>Login</button>
            </div>
        </div>
    )
}

