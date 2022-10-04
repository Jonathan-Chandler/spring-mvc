import React, { Component } from 'react'
import TodoDataService from './TodoDataService.js'
import AuthenticationService from '../../authentication/AuthenticationService.jsx'
import moment from 'moment'

class ListTodosComponent extends Component {
    constructor(props) {
        console.log('constructor')
        super(props)
        this.state = {
            todos: [],
            message: null
        }
        //this.deleteTodoClicked = this.deleteTodoClicked.bind(this)
        this.updateTodoClicked = this.updateTodoClicked.bind(this)
        //this.addTodoClicked = this.addTodoClicked.bind(this)
        this.refreshTodos = this.refreshTodos.bind(this)
    }

    componentWillUnmount() {
        console.log('componentWillUnmount')
    }

    shouldComponentUpdate(nextProps, nextState) {
        console.log('shouldComponentUpdate')
        console.log(nextProps)
        console.log(nextState)
        return true
    }

    componentDidMount() {
        console.log('componentDidMount')
        this.refreshTodos();
        console.log(this.state)
    }

    refreshTodos() {
        TodoDataService.retrieveAllTodos()
            .then(
                response => {
                    //console.log("response:");
                    //console.log(response.headers);
                    //console.log("<end response");
                    //console.log("headers:");
                    //console.log(response.headers.authorization);
                    //console.log("end headers:");
                    //AuthenticationService.validateHeader(response);
                    this.setState({ todos: response.data })
                }
            )
            .catch(err => {
                console.error(err)
            })
    }

    addTodoClicked() {
        this.props.history.push(`/todos/-1`)
    }

    updateTodoClicked() {
        console.log('update ')
        this.props.navigate(`/todos`)//REACT-6
    }

    render() {
        console.log('render')
        return (
            <div>
                <h1>List Todos</h1>
                {this.state.message && <div class="alert alert-success">{this.state.message}</div>}
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
                                this.state.todos.map(
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
                    <div className="row">
                        <button className="btn btn-success" onClick={this.addTodoClicked}>Add</button>
                    </div>
                </div>
            </div>
        )
    }
}

export default ListTodosComponent
