import axios from 'axios'
import { LOGIN_API_URL, JPA_API_URL } from '../../Constants'

class AuthorizationService 
{
    login(username, password) {
        //console.log('executed service')
        return axios.post(`${LOGIN_API_URL}`, {
            "username": username,
            "password": password
        });
    }

    registerSuccessfulLoginForJwt(token) {
        sessionStorage.setItem("jwt_response", token)
        this.setupAxiosInterceptors(this.createJWTToken(token))
    }
}

export default new AuthorizationService()

