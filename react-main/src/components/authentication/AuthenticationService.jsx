import axios from 'axios'
import { API_URL, LOGIN_API_URL } from '../../Constants'
import React, {Component, useState, useEffect} from 'react'

//export const USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUser'


//class AuthenticationService extends React.Component {
//class AuthenticationService extends React.Component {
class AuthenticationService {

    // send synchronous login request
    async login(username, password) {
        const userData = {
            username: username,
            password: password
        };
        await axios
            .post(`${LOGIN_API_URL}`, userData)
            .then((response) => {
                if (response.data.Authorization)
                {
                    // add auth token to all headers
                    axios.defaults.headers.common['Authorization'] = response.data.Authorization;

                    // store auth token
                    sessionStorage.setItem('Authorization', response.data.Authorization);
                }
            })
            .catch((error) => {
                if (error.response) {
                    console.log(error.response);
                    console.log("server responded");
                } else if (error.request) {
                    console.log("network error");
                } else {
                    console.log(error);
                }
            });
    }

//    login(username, password) {
//        const userData = {
//            username: username,
//            password: password
//        };
//        axios
//            .post(`${LOGIN_API_URL}`, userData)
//            .then((response) => {
//                console.log(response);
//                if (response.data.Authorization)
//                {
//                    //console.log(response);
//                    console.log("Set login Authorization: " + response.data.Authorization)
//                    //props.authorization = response.data.Authorization
//                    axios.defaults.headers.common['Authorization'] = response.data.Authorization;
//                    sessionStorage.setItem('Authorization', response.data.Authorization);
//                    //props.navigate(`/welcome/${this.state.username}`)
//                    //navigation.navigate('/welcome/${username}')
//                    return true;
//                }
//                //this.props.history.push("/welcome/${data.username}")
//                //withRouter.push("/welcome/${data.username}")
//                //useHistory().push("/welcome")
//                //navigate("/welcome/${data.username}", {replace: true});
//                //navigate("/welcome/name", {replace: true});
//                //navigate("/welcome/name");
//                //const ShowTheLocationWithRouter = withRouter(LoginComponent);
//            })
//            .catch((error) => {
//                //setDoRedirect(false);
//                if (error.response) {
//                    console.log(error.response);
//                    console.log("server responded");
//                } else if (error.request) {
//                    console.log("network error");
//                } else {
//                    console.log(error);
//                }
//                return false;
//            });
//        return false;
//    }

    logout() {
        sessionStorage.removeItem('Authorization');
    }

    isUserLoggedIn() {
        //let token = localStorage.getItem('Authorization')
        let token = sessionStorage.getItem('Authorization')
        //console.log("isUserLoggedIn token: " + token);
        if (token === null) return false
        console.log("isUserLoggedIn: true");
        return true
    }

//        localStorage.removeItem('Authorization');
}

export default new AuthenticationService();

////export default class AuthenticationService {
//function AuthenticationService
//{
////export default class AuthenticationService extends Component {
//
//    //executeJwtAuthenticationService(username, password) {
//    //    return axios.post(`${LOGIN_API_URL}`, {
//    //        "username":username,
//    //        "password":password
//    //    })
//    //}
//
//    logout() {
//        localStorage.removeItem('Authorization');
//    }
//
//    isUserLoggedIn() {
//        let token = localStorage.getItem('Authorization')
//        if (token === null) return false
//        return true
//    }
//}
//
//export default AuthenticationService;
////export default new AuthenticationService()
//
