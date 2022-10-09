import moment from 'moment'
import React, { useEffect, useReducer, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from '../authentication/AuthProvider.tsx';
import { TODOS_API_URL } from '../../Constants';

export default function ListTodosComponent(...props)
{
    const navigate = useNavigate();
    const { username, token, apiSession, loading, error, login, signUp, logout, getSession } = useAuth();
    const [state, setState] = useState({
      todos: [],
      message: "",
    });


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
    useEffect(() => 
    {
        if (!token || token === "")
        {
            logout();
            navigate("/login");

        }

        getTodos();
    }, []);

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
}
