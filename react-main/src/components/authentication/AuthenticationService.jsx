import axios from 'axios'
import { LOGIN_API_URL } from '../../Constants'

//import jwt_decode from "jwt-decode";

//function RedirectLogout(props) {
//    const navigate = useNavigate();
//    console.log("redirect to login");
//    return navigate("/login/");
//}

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

    // get authenticated axios connection
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

        if (error.response.status === 401)
        {
            this.logout();
        }
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

    // use updated token as new Authorization for header if valid
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
    
    // remove auth and send to login page
    logout() {
        sessionStorage.removeItem('Authorization');
    }

    // check session for auth token
    isUserLoggedIn() {
        const token = sessionStorage.getItem('Authorization')
        if (token === null) return false
        return true
    }
}

export default new AuthenticationService();
