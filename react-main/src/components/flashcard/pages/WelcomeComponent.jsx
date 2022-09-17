import React, { Component } from 'react';
import HelloWorldService from '../../../api/todo/HelloWorldService.js'

class WelcomeComponent extends Component
{
  constructor(props) {
    super(props);

    this.retrieveWelcomeMessage = this.retrieveWelcomeMessage.bind(this);
    this.handleSuccessfulResponse = this.handleSuccessfulResponse.bind(this);
    this.convertEmployeeResponse = this.convertEmployeeResponse.bind(this);

    this.state = {
      employeeMessage : ''
    }
  }

  render() {
    return (
      <>
        <h1>welcome header</h1>
        <div className="container">
          Welcome {this.props.params.username}
          <button className="btn" onClick={this.retrieveWelcomeMessage}>Service</button>
        </div>
        <div className="container">
          <div>
            <h1>List Employees</h1>
            <div className="container">
              <table className="table">
                <thead>
                  <tr>
                    <th scope="col">id</th>
                    <th scope="col">firstName</th>
                    <th scope="col">lastName</th>
                    <th scope="col">email</th>
                  </tr>
                </thead>
                <tbody>
                  {
                    this.state.employeeMessage
                  }
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </>
    );
  }

  retrieveWelcomeMessage() {
    HelloWorldService.executeHelloWorldService()
      .then( response => this.handleSuccessfulResponse(response))
      //.then( response => console.log(response) )
  }

  handleSuccessfulResponse(response) {
    //this.setState({employeeMessage : response.data.firstName})
    //this.setState({employeeMessage : response.data[1].firstName})
    this.setState({employeeMessage : this.convertEmployeeResponse(response.data)})
  }

  convertEmployeeResponse(responseData){
    return (responseData.map (
      entry => 
        <tr>
          <th scope="row">{entry.id}</th>
          <td>{entry.firstName}</td>
          <td>{entry.lastName}</td>
          <td>{entry.email}</td>
        </tr>
    )
    );
  }
}

export default WelcomeComponent;
