import React, {useState, useEffect} from 'react'
//import AuthenticationService from '../authentication/AuthenticationService.jsx'
import { useNavigate } from "react-router-dom";
//import UseAuth, {useAuthDispatch} from './AuthContext.jsx'
//import {AUTH_ACTIONS} from './AuthReducer.jsx'
import useAuth from "./AuthProvider.tsx";

export default function LoginComponent(...props) {
    //const auth = UseAuth();
    //const authDispatch = useAuthDispatch();
    const { username, token, apiSession, loading, error, login, signUp, logout } = useAuth();
    const navigate = useNavigate();

    // username/password form data
    const [data, setData] = useState({
      username: "test_user1",
      password: "password3",
    });

    // set to true if user entered wrong username/password
    const [loginFailed, setLoginFailed] = useState(0);

    // set to true if user entered correct login, causes redirect
    const [loginSuccess, setLoginSuccess] = useState(false);

    // attempt login on submit
    const handleSubmit = async () => {

        //login({username: data.username, password: data.password});
        login(data.username, data.password);
        // must force synchronous login to check if login was successful
        //await AuthenticationService.login(data.username, data.password);

        //authDispatch({
        //    type: AUTH_ACTIONS.REQUEST_LOGIN, 
        //    payload:{username: data.username, password: data.password}
        //})

        // trigger redirect if auth was successful
        //setLoginSuccess(AuthenticationService.isUserLoggedIn())

        // display failed message if login not successful
        //setLoginFailed(loginSuccess ? false : true)
        //console.log("handleSubmit: auth.token = " + auth.token)
    }
    
    // on page load: reset failed status and redirect if already authenticated
    useEffect(() => {
        // remove failed login warning
        setLoginFailed(false);

        // update login successful state and redirect if already logged in
        //setLoginSuccess(AuthenticationService.isUserLoggedIn())
    }, []);

    // do redirect and reset login failed if login is valid
    //useEffect(() => {
    //    if (loginSuccess)
    //    {
    //        setLoginFailed(false);
    //        navigate("/welcome/" + data.username);
    //    }
    //}, [data.username, navigate, loginSuccess]);

    useEffect(() => {
        if (!loading && token && token !== "")
        {
            console.log("token = " + token)
            setLoginFailed(false);
            navigate("/welcome/" + data.username);
        }
    }, [data.username, navigate, loading, token]);

    // do redirect and reset login failed if login is valid
    //useEffect(() => {
    //    if (loginSuccess)
    //    {
    //        setLoginFailed(false);
    //        navigate("/welcome/" + data.username);
    //    }
    //}, [data.username, navigate, loginSuccess]);

    //useEffect(() => {
    //    //console.log("auth.token = " + auth.token)
    //    //if (!auth.loading)
    //    //{
    //    //    if (auth.token && auth.token !== "")
    //    //    {
    //    //        console.log("notnull auth.token = " + auth.token)
    //    //        setLoginFailed(false);
    //    //        navigate("/welcome/" + auth.username);
    //    //    }
    //    //}
    //}, [navigate]);
    //}, [auth.loading, auth.username, auth.token, navigate]);

    // handle login form data update
    const handleLoginDataChange = (event) => {
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

