import moment from 'moment'
import React, { useEffect, useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from '../authentication/AuthProvider.tsx';
import { TODOS_API_URL } from '../../Constants';

export default function ListTodosComponent(...props)
{
    const navigate = useNavigate();
    const { isAuthenticated, getSession } = useAuth();
    const [state, setState] = useState({
      todos: [],
      message: "",
      loadedTodos: false,
    });

    const getTodos = useCallback(async () =>
    {
        let axiosSession = getSession();
        const data = await axiosSession.get(TODOS_API_URL)
        .then( response => 
        {
            return response.data;
        }
        )
        .catch(err => {
            console.log(err)
            return null
        })

        if (data !== null)
        {
            setState({todos: data});
        }
    }, [getSession]);

    useEffect(() => 
    {
        // check if auth
        if (!isAuthenticated())
        {
            navigate("/login");
        }
    }, [navigate, isAuthenticated]);

    // on page load
    useEffect(() => 
    {
        // empty todos
        if (state.loadedTodos === false)
        {
            console.log("useEffect(getTodos)")
            // async update todos list
            getTodos();
            setState({...state, loadedTodos: true});
        }

    }, [getTodos, getSession, state]);

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
                    No todos returned
                </div>
            </>
        )        
}
