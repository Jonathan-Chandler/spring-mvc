import axios from 'axios'
import { LOGIN_API_URL } from '../../Constants'
//import jwt_decode from "jwt-decode";

class AuthenticationService {

    constructor()
    {
        // use service as a wrapper for axios to handle auth
        let service = axios.create();

        // update token from response headers with updated auth tokens and handle failed authentication
        service.interceptors.response.use(this.handleResponseHeaders, this.handleResponseAuthFailure);

        // add auth token to all headers
        service.defaults.headers.common['Authorization'] = sessionStorage.getItem('Authorization');

        this.service = service;
    }

    getAuth() {
        return this.service
    }

    // update token from response headers with updated auth tokens
    handleResponseHeaders = (response) => {
        if (response.headers.authorization)
        {
            const token = response.headers.authorization;
            console.log("has auth: " + token);
            this.updateAuthorizationToken(token);
        }
        return response;
    }

    // handle failed authentication
    handleResponseAuthFailure = (error) => {
        console.log("handleResponseFailure: " + error.response);
        //if (error.response.status === 401)
        //{
        //    this.logout();
        //}
        return Promise.reject(error);
    }

    // send synchronous login request
    async login(username, password) {
        const userData = {
            username: username,
            password: password
        };
        await this.service
            .post(`${LOGIN_API_URL}`, userData)
            .then((response) => {
                // if returned valid auth token then use for all future headers
                if (response.data.Authorization)
                {
                    const token = response.data.Authorization;
                    this.updateAuthorizationToken(token);
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

    updateAuthorizationToken(token)
    {
        if (token && token.startsWith('Bearer '))
        { 
            const currentToken = sessionStorage.getItem('Authorization')

            if (token !== currentToken)
            {
                // store auth token
                sessionStorage.setItem('Authorization', token);

                // add auth token to all headers
                this.service.defaults.headers.common['Authorization'] = token;
            }
        }
        else
        {
            // assume auth expired/failed
            this.logout();
        }
    }
    
    logout() {
        sessionStorage.removeItem('Authorization');
    }

    isUserLoggedIn() {
        const token = sessionStorage.getItem('Authorization')
        if (token === null) return false
        return true
    }


    //setupInterceptors()
    //{
    //    // add auth token to all headers
    //    axios.defaults.headers.common['Authorization'] = sessionStorage.getItem('Authorization');

    //    //// update token from response headers with updated auth tokens and handle failed authentication
    //    //axios.interceptors.response.use(function (response) {
    //    //    if (response.headers.authorization)
    //    //    {
    //    //        const token = response.headers.authorization;
    //    //        console.log("has auth: " + token);
    //    //        this.updateAuthorizationToken(token);
    //    //    }
    //    //    return response;
    //    //}, function (error) {
    //    //    if (error.response.status === 401)
    //    //    {
    //    //        this.logout();
    //    //    }
    //    //    return Promise.reject(error);
    //    //});
    //}

    //handleAuthenticationHeader(response) {
    //    console.log("Handling header for response: " + response);
    //    this.validateHeader(response);
    //    return response;
    //}

    //handleAuthenticationFailure(error) {
    //    console.log("Handling error: " + error);
    //    // force logout on auth failures
    //    if (error.response.status === 401)
    //    {
    //        this.logout();
    //    }
    //    return Promise.reject(error);
    //}


    // update token from header or logout if invalid
    //validateHeader(response) {
    //    try {
    //        if (response.headers)
    //        {
    //            const token = response.headers.authorization;
    //            if (token)
    //                this.updateAuthorizationToken(token)
    //        }
    //    }
    //    catch(error) {
    //        console.log("Error converting response: " + error);
    //        this.logout();
    //    }
    //}

    //get(path, callback) {
    //    return this.service.get(path).then(
    //        (response) => callback(response.status, response.data)
    //    );
    //}

    //post(path, payload, callback) {
    //    return this.service.request({
    //        method: 'POST',
    //        url: path,
    //        responseType: 'json',
    //        data: payload
    //    }).then(
    //        (response) => callback(response.status, response.data)
    //    );
    //}

    //isValidToken(token)
    //{
    //    try {
    //        const decodedToken = jwt_decode(token)
    //        const currentDate = new Date()
    //
    //        // expires in 5 minutes
    //        if (decodedToken.exp < currentDate.getTime() + (5*60*1000))
    //        {
    //        }
    //    }
    //    catch (error)
    //    {
    //    }
    //    return true;
    //}

}

export default new AuthenticationService();
