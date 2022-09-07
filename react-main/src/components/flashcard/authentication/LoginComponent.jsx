import React, { Component } from 'react';
import AuthenticationService from '../decorators/AuthenticationService.jsx'

class LoginComponent extends Component
{
  constructor(props) {
    super(props);
    this.state = {
      username : 'username',
      password : '',
      hasLoginFailed : false,
    }

    this.handleChange = this.handleChange.bind(this)
    this.loginClicked = this.loginClicked.bind(this)
  }

  handleChange(event) {
    //console.log(event.target.value)
    //console.log(this.state)
    this.setState(
      {
        [event.target.name] : event.target.value
      }
    )
  }

  loginClicked() {
    //console.log(this.state)
    if (this.state.username==='username' && this.state.password==='a')
    {
      AuthenticationService.registerSuccessfulLogin(this.state.username, this.state.password)

      //console.log("login")
      this.props.navigate(`/welcome/${this.state.username}`)
    }
    else
    {
      this.setState({hasLoginFailed : true})
    }
  }

  render() {
    return (
      <div>
        <h1>Login</h1>
        User Name: <input type="text" name="username" value={this.state.username} onChange={this.handleChange}/>
        Password: <input type="password" name="password" value={this.state.password} onChange={this.handleChange}/>
        <button className="btn" onClick={this.loginClicked}>Login</button>
        {this.state.hasLoginFailed && <div className="alert alert-warning">Invalid Login</div>}
      </div>
    )
  }
}

export default LoginComponent;

