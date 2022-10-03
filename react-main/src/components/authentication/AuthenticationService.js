import axios from 'axios'
import { API_URL, LOGIN_API_URL } from '../../Constants'

export const USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUser'

class AuthenticationService {

    getJwtLoginPromise(username, password) {
        console.log("request sent to: " + LOGIN_API_URL)
        console.log("getjwt username: " + username)
        console.log("getjwt password: " + password)
        const credentials = {
            "username": username,
            "password": password
        }
        console.log("json stringify: " + JSON.stringify(credentials))
        console.log("credentials-obj: " + JSON.stringify(credentials))
        //return axios({
        //    url: LOGIN_API_URL,
        //    method: 'post',
        //    timeout: 8000,
        //    headers: {
        //        'Content-Type': 'application/json'
        //    },
        //    body: {
        //        credentials
        //    }
        //})
        return axios.post(LOGIN_API_URL, credentials)
        .then(response => {
                if (response.headers.Authorization) {
                    this.registerSuccessfulLoginForJwt(response.headers.Authorization)
                    return true
                }
                return false
            }
        )
        .catch(err => {
            console.error(err)
            return false
        })
    }

    executeJwtAuthenticationService(username, password) {
        return axios.post(`${LOGIN_API_URL}`, {
            "username":username,
            "password":password
        })
    }

    createBasicAuthToken(username, password) {
        return 'Basic ' + window.btoa(username + ":" + password)
    }

    registerSuccessfulLoginForJwt(token) {
        if (token) {
            localStorage.setItem('Authorization', token)
            this.setupAxiosInterceptors(token)
        }
    }

    createJWTToken(token) {
        return 'Bearer ' + token
    }

    logout() {
        localStorage.removeItem(USER_NAME_SESSION_ATTRIBUTE_NAME);
    }

    isUserLoggedIn() {
        let token = localStorage.getItem('Authorization')
        if (token === null) return false
        return true
    }

    setupAxiosInterceptors(token) {

        axios.interceptors.request.use(
            (config) => {
                if (this.isUserLoggedIn()) {
                    config.headers.authorization = token
                    axios.defaults.headers.common['Authorization'] = localStorage.getItem('Authorization');
                }
                return config
            }
        )
    }
}

export default new AuthenticationService()
