import React, { useReducer } from "react";
import axios from 'axios'
import { TODOS_API_URL } from '../../../Constants'
//import {useAuth, useAuthDispatch} from '../../authentication/AuthContext.jsx'
import UseAuth from '../../authentication/AuthContext.jsx'

//export const RetrieveAllTodos = () => {
//export default function RetrieveAllTodos()
export const RetrieveAllTodos = () => 
{
    //const {auth, dispatch} = [useAuth(), useAuthDispatch()];
    const {auth} = UseAuth();
    console.log("auth: " + auth);
    //const [auth, dispatch] = useReducer(AuthReducer, initialState);
    

    return auth.apiSession.get(TODOS_API_URL);
}
        //return axios.get(`${TODOS_API_URL}`);
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

//export default new TodoDataService()

