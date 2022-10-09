//import React, { useReducer } from "react";
//import axios from 'axios'
//import { LOGIN_API_URL } from '../../Constants'
//import UseAuth, {useAuthDispatch} from './AuthContext.jsx'
// 
//let sessionUsername = sessionStorage.getItem("username");
//let sessionToken = sessionStorage.getItem("Authorization");
// 
//export const AUTH_ACTIONS = {
//    REQUEST_LOGIN: "REQUEST_LOGIN",
//    LOGIN_SUCCESS: "LOGIN_SUCCESS",
//    REQUEST_REGISTER: "REQUEST_REGISTER",
//    REGISTER_SUCCESS: "REGISTER_SUCCESS",
//    UPDATE_JWT: "UPDATE_JWT",
//    LOGOUT: "LOGOUT",
//    AUTH_ERROR: "AUTH_ERROR",
//};
//
//export const initialState = {
//  username: "" || sessionUsername,
//  token: "" || sessionToken,
//  loading: false,
//  errorMessage: null,
//  apiSession: null
//};
//export const DoLogout = (state) => {
//    sessionStorage.setItem("username", "");
//    sessionStorage.setItem("Authorization", "");
//    state.username = "";
//    state.token = "";
//    state.apiSession = null;
//    state.loading = false;
//    state.error = "";
//    return state
//}
//
//// register new user
////export const DoRegister = async (username, password) => {
////    const [state, dispatch] = useReducer(AuthReducer, initialState);
////    const userData = {
////        username: username,
////        password: password
////    };
////    await axios
////        .post(`${LOGIN_API_URL}`, userData)
////        .then((response) => {
////            // if returned valid auth token then use for all future headers
////            if (response.data.Authorization)
////            {
////                const token = response.data.Authorization;
////                this.updateAuthorizationToken(token);
////                dispatch({
////                    type: AUTH_ACTIONS.REGISTER_SUCCESS, 
////                    payload: {username: username, token: token}
////                });
////            }
////        })
////        .catch((error) => {
////            if (error.response) {
////                console.log(error.response);
////            } else if (error.request) {
////                console.log("network error");
////            } else {
////                console.log(error);
////            }
////        });
////}
//
////class AuthenticationService {
////
////    constructor()
////    {
////        // use service as a wrapper for axios to handle auth
////        let service = axios.create();
////
////        // update token from response headers with updated auth tokens and handle failed authentication
////        service.interceptors.response.use(this.handleResponseHeaders, this.handleResponseAuthFailure);
////
////        // add auth token to all headers
////        service.defaults.headers.common['Authorization'] = sessionStorage.getItem('Authorization');
////
////        this.service = service;
////    }
////
////    // get authenticated axios connection
////    getAuth() {
////        return this.service
////    }
////
////    // update token from response headers with new auth tokens
////    handleResponseHeaders = (response) => {
////        if (response.headers.authorization)
////        {
////            const token = response.headers.authorization;
////            console.log("has auth: " + token);
////            this.updateAuthorizationToken(token);
////        }
////        return response;
////    }
////
////    // handle failed authentication
////    handleResponseAuthFailure = (error) => {
////        console.log("handleResponseFailure: " + error.response);
////
////        if (error.response.status === 401)
////        {
////            this.logout();
////        }
////        return Promise.reject(error);
////    }
////
////
////    // use updated token as new Authorization for header if valid
////    updateAuthorizationToken(token)
////    {
////        if (token && token.startsWith('Bearer '))
////        { 
////            const currentToken = sessionStorage.getItem('Authorization')
////
////            if (token !== currentToken)
////            {
////                // store auth token
////                sessionStorage.setItem('Authorization', token);
////
////                // add auth token to all headers
////                this.service.defaults.headers.common['Authorization'] = token;
////            }
////        }
////        else
////        {
////            // assume auth expired/failed
////            this.logout();
////        }
////    }
////    
////    // remove auth and send to login page
////    logout() {
////        sessionStorage.removeItem('Authorization');
////    }
////
////    // check session for auth token
////    isUserLoggedIn() {
////        const token = sessionStorage.getItem('Authorization')
////        if (token === null) return false
////        return true
////    }
////}
//
//
//export const AuthReducer = (state, action) => {
//    // init session for axios api access
//    if (state.apiSession === null)
//    {
//        // use service as a wrapper for axios to handle auth
//        state.apiSession = axios.create();
//
//        // update token from response headers with updated auth tokens and handle failed authentication
//        state.apiSession.interceptors.response.use(HandleResponseHeaders, HandleResponseAuthFailure);
//
//        //// use auth token if state/session already has it
//        //if (state.token !== "")
//        //{
//        //    // add auth token to all axios headers
//        //    state.apiSession.defaults.headers.common['Authorization'] = this.token;
//        //}
//    }
//
//    switch (action.type) {
//        case "REQUEST_REGISTER":
//            const reqRegUsername = action.payload.username;
//            const reqRegPassword = action.payload.password;
//            console.log("auth register request username: " + reqRegUsername + ", password: " + reqRegPassword);
//            return {
//                ...state,
//                loading: true
//            };
//
//        case "REGISTER_SUCCESS":
//            const regUsername = action.payload.username;
//            console.log("auth registered new login username: " + regUsername);
//            return {
//                ...state,
//                loading: false
//            };
//
//        case "REQUEST_LOGIN":
//            console.log("auth req login username: " + action.payload.username + ", password: " + action.payload.token);
//            DoLogin(action.payload.username, action.payload.token)
//            return {
//                ...state,
//                loading: true
//            };
//
//        case "LOGIN_SUCCESS":
//            //// username, token, 
//            //const reqUsername = action.payload.username;
//            //const reqPassword = action.payload.password;
//            //// update session username only
//            //const newUsername = action.payload.username;
//            console.log("auth success username: " + action.payload.username);
//
//            sessionStorage.setItem("username", action.payload.username);
//            sessionStorage.setItem("Authorization", action.payload.token);
//
//            return {
//                ...state,
//                username: action.payload.username, 
//                token: action.payload.token, 
//                apiSession: action.payload.apiSession,
//                loading: false
//            };
//
//        case "UPDATE_JWT":
//            // update session token only
//            const newToken = action.payload.token;
//            console.log("Update jwt: " + newToken);
//
//            // add auth token to all headers and store to session
//            state.apiSession.defaults.headers.common['Authorization'] = newToken;
//            sessionStorage.setItem("Authorization", newToken);
//            return {
//                ...state,
//                token: newToken,
//            };
//
//        case "LOGOUT":
//            // remove token and username from session storage
//            console.log("auth logout");
//            //const logoutState = DoLogout(state);
//            DoLogout(state);
//            //sessionStorage.setItem("username", "");
//            //sessionStorage.setItem("Authorization", "");
//            return {
//                ...state,
//                loading: false
//            };
//
//        case "AUTH_ERROR":
//            console.log("AUTH_ERROR: " + action.payload.error)
//            return {
//                ...state,
//                username: "",
//                sessionToken: "",
//                apiSession: null,
//                loading: false,
//                errorMessage: action.payload.error
//            };
//
//        default:
//            throw new Error(`Unhandled action type: ${action.type}`);
//    }
//};
//
