//import React, { Component } from 'react'
//import TodoDataService from './TodoDataService.js'
//import AuthenticationService from '../../authentication/AuthenticationService.jsx'
import moment from 'moment'
import React, { useEffect, useReducer, useState } from "react";
import { useNavigate } from "react-router-dom";
//import {AuthProvider} from '../../authentication/AuthProvider.jsx'
import useAuth from '../../authentication/AuthProvider.tsx';
import { TODOS_API_URL } from '../../../Constants';
//
//import RetrieveAllTodos from './TodoDataService.js'

//class ListTodosComponent extends Component {
export default function ListTodosComponent(...props)
{
    const navigate = useNavigate();
    const { username, token, apiSession, loading, error, login, signUp, logout, getSession } = useAuth();
    const [state, setState] = useState({
      todos: [],
      message: "",
    });

    if (!token || token === "")
    {
        logout();
        navigate("/login");

    }


    async function getTodos()
    {
        let axiosSession = getSession()

        if (axiosSession)
        {
            console.log("have apiSession");
            await axiosSession.get(TODOS_API_URL).then(
                    response => {
                        console.log("TODOS:")
                        console.log(response.data)
                        setState({todos: response.data});
                    }
                )
                .catch(err => {
                    console.log(err)
                })

        }
        else
        {
            console.log("no apiSession");
        }
    }

    // on page load: reset failed status and redirect if already authenticated
    useEffect(() => {
        getTodos();
    }, []);

    //useEffect(() => {
    //    // update login successful state and redirect if already logged in
    //    //a
    //    if (apiSession)
    //    {
    //        console.log("have apiSession2");
    //        let axiosSession = getSession()
    //        axiosSession.get(TODOS_API_URL).then(
    //                response => {
    //                    //console.log("response:");
    //                    //console.log(response.headers);
    //                    //console.log("<end response");
    //                    //console.log("headers:");
    //                    //console.log(response.headers.authorization);
    //                    //console.log("end headers:");
    //                    //AuthenticationService.validateHeader(response);
    //                    //setState({ todos: response.data })
    //                    console.log(response.data)
    //                }
    //            )
    //            .catch(err => {
    //                console.log(err)
    //            })

    //    }
    //    else
    //    {
    //        console.log("no apiSession2");
    //    }
    //}, [apiSession]);

    if (state.todos)
    {
        return (
            <div>
                <h1>List Todos</h1>
                {state.message && <div class="alert alert-success">{state.message}</div>}
                <div className="container">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Description</th>
                                <th>Target Date</th>
                                <th>IsCompleted?</th>
                                <th>Update</th>
                                <th>Delete</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                state.todos.map(
                                    todo =>
                                        <tr key={todo.id}>
                                            <td>{todo.description}</td>
                                            <td>{moment(todo.targetDate).format('YYYY-MM-DD')}</td>
                                            <td>{todo.done.toString()}</td>
                                            <td><button className="btn btn-success" onClick={() => this.updateTodoClicked(todo.id)}>Update</button></td>
                                            <td><button className="btn btn-warning" onClick={() => this.deleteTodoClicked(todo.id)}>Delete</button></td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
    else
        return (
            <>
                <div className="container">
                    No todos returned {username}
                </div>
            </>
        )        
////////    }
}
//////export default function ListTodosComponent() {
//////    //const { username, token, apiSession, loading, error, login, signUp, logout } = useAuth();
//////    //const [auth, dispatch] = useReducer(AuthReducer, initialState);
//////    //const [auth] = useReducer(AuthReducer);
////////    const {auth} = AuthProvider.useAuth()
//////    //const auth = UseAuth();
//////    //const [state, setState] = useState({todos:[], message:null});
//////    //const [state, setState] = useState({todos:[], message:null});
//////    //const [state, setState] = useState({
//////    //  todos: [],
//////    //  message: "",
//////    //});
//////    //const [todo, setTodo] = useState([]);
//////    //const [message, setMessage] = useState(null);
//////    ////console.log("ListTodosComponent.auth: " + auth)
//////    ////console.log("ListTodosComponent.auth.apiSession: " + auth.apiSession)
//////    //        message: null
//////
//////    ////useEffect(() => {
//////    ////    //<tr key={todo.id}>
//////    ////    //    <td>{todo.description}</td>
//////    ////    //    <td>{moment(todo.targetDate).format('YYYY-MM-DD')}</td>
//////    ////    //    <td>{todo.done.toString()}</td>
//////    ////    // remove failed login warning
//////    ////    setTodo([])
//////    ////    setState({todos: [], message: ""})
//////
//////    ////    // update login successful state and redirect if already logged in
//////    ////    //setLoginSuccess(AuthenticationService.isUserLoggedIn())
//////    ////}, []);
//////    //constructor(props) {
//////    //    console.log('constructor')
//////    //    super(props)
//////    //    this.state = {
//////    //        todos: [],
//////    //        message: null
//////    //    }
//////    //    //this.deleteTodoClicked = this.deleteTodoClicked.bind(this)
//////    //    this.updateTodoClicked = this.updateTodoClicked.bind(this)
//////    //    //this.addTodoClicked = this.addTodoClicked.bind(this)
//////    //    this.refreshTodos = this.refreshTodos.bind(this)
//////    //}
//////
//////    //componentWillUnmount() {
//////    //    console.log('componentWillUnmount')
//////    //}
//////
//////    //shouldComponentUpdate(nextProps, nextState) {
//////    //    console.log('shouldComponentUpdate')
//////    //    console.log(nextProps)
//////    //    console.log(nextState)
//////    //    return true
//////    //}
//////
//////    //componentDidMount() {
//////    //    console.log('componentDidMount')
//////    //    this.refreshTodos();
//////    //    console.log(this.state)
//////    //}
//////
//////    ////const refreshTodos = () => {
//////    ////    apiSession.get(TODOS_API_URL)
//////    ////        .then(
//////    ////            response => {
//////    ////                //console.log("response:");
//////    ////                //console.log(response.headers);
//////    ////                //console.log("<end response");
//////    ////                //console.log("headers:");
//////    ////                //console.log(response.headers.authorization);
//////    ////                //console.log("end headers:");
//////    ////                //AuthenticationService.validateHeader(response);
//////    ////                setState({ todos: response.data })
//////    ////            }
//////    ////        )
//////    ////        .catch(err => {
//////    ////            console.error(err)
//////    ////        })
//////    ////}
//////    ////refreshTodos();
//////
//////    //addTodoClicked() {
//////    //    this.props.history.push(`/todos/-1`)
//////    //}
//////
//////    //updateTodoClicked() {
//////    //    console.log('update ')
//////    //    this.props.navigate(`/todos`)//REACT-6
//////    //}
//////
//////    return (<div> nothing </div);
//////}
//////
////////        console.log('render')
