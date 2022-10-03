import axios from 'axios'
import { TODOS_API_URL } from '../../Constants'

class TodoDataService {

    retrieveAllTodos(name) {
        return axios.get(TODOS_API_URL);
////        .then(response => {
////            if (response.headers.Authorization) {
////                this.registerSuccessfulLoginForJwt(response.headers.Authorization)
////            }
////            return response
//            if (response.body) {
//                return response.body
//            } else {
//                return {}
//            }
////        })
//        .catch(err => {
//            console.error(err)
//            return {}
//        })
    }

    //retrieveTodo(name, id) {
    //    //console.log('executed service')
    //    return axios.get(`${API_URL}/users/${name}/todos/${id}`);
    //}

    //deleteTodo(name, id) {
    //    //console.log('executed service')
    //    return axios.delete(`${API_URL}/users/${name}/todos/${id}`);
    //}

    //updateTodo(name, id, todo) {
    //    //console.log('executed service')
    //    return axios.put(`${API_URL}/users/${name}/todos/${id}`, todo);
    //}

    //createTodo(name, todo) {
    //    //console.log('executed service')
    //    return axios.post(`${API_URL}/users/${name}/todos/`, todo);
    //}
}

export default new TodoDataService()

