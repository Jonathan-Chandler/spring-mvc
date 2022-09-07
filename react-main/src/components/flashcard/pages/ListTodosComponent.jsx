import React, { Component } from 'react';

class ListTodosComponent extends Component
{
  constructor(props){
    super(props)
    this.state = {
      todo : 
      [
        {id: 1, description : 'Todo description', done:false, targetDate: new Date()},
        {id: 2, description : 'Todo description2', done:false, targetDate: new Date()},
        {id: 3, description : 'Todo description3', done:false, targetDate: new Date()}
      ]
    }
  }

  render() {
    //this.state.todo.forEach (entry => console.log(entry.description) );

    return (
      <div>
        <h1>List Todos</h1>
        <div className="container">
          <table className="table">
            <thead>
              <tr>
                <th scope="col">id</th>
                <th scope="col">description</th>
                <th scope="col">done</th>
                <th scope="col">targetDate</th>
              </tr>
            </thead>
            <tbody>
              {
                this.state.todo.map (
                  entry => 
                    <tr>
                      <th scope="row">{entry.id}</th>
                      <td>{entry.description}</td>
                      <td>{entry.done.toString()}</td>
                      <td>{entry.targetDate.toString()}</td>
                    </tr>
                )
              }
            </tbody>
          </table>
        </div>
      </div>
    )
  }
}

export default ListTodosComponent;
