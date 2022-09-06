import React, { Component } from 'react';
import PropTypes from 'prop-types'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import withNavigation from './WithNavigation.jsx'

export class FlashCardApp extends Component 
{
  render() 
  {
    const LoginComponentWithNavigation = withNavigation(LoginComponent);
    return (
      <div className="flashCardApp">
        <h1>Flash Card Application</h1>
        <Router>
          <Routes>
            <Route path="/" element={<LoginComponentWithNavigation />} />
            <Route path="/login" element={<LoginComponentWithNavigation />} />
            <Route path="/welcome" element={<WelcomeComponent />} />
          </Routes>
        </Router>
      </div>
    );
  }
}

class WelcomeComponent extends Component
{
  render() {
    return (
      <div>
        Welcome
      </div>
    );
  }
}

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
      console.log("login")
      this.props.navigate(`welcome`)
    }
    else
    {
      this.setState({hasLoginFailed : true})
    }
  }

  render() {
    return (
      <div>
        User Name: <input type="text" name="username" value={this.state.username} onChange={this.handleChange}/>
        Password: <input type="password" name="password" value={this.state.password} onChange={this.handleChange}/>
        <button onClick={this.loginClicked}>Login</button>
        {this.state.hasLoginFailed && <div>Invalid Login</div>}
      </div>
    )
  }
}


//function ShowLoginSuccessful(props){
//  if (props.showLoginSuccess){
//    return <div>Login Successful</div>
//  }
//}
//
//function ShowInvalidCredentials(props){
//  if (props.hasLoginFailed){
//    return <div>Invalid Login</div>
//  }
//}

export default FlashCardApp;
