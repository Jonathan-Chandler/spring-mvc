import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
//import 'react-bootstrap/Navbar';
//import navbar from 'react-bootstrap/Navbar';

class App extends Component {
  render() {
    return (
      <div className="App">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
          <a class="navbar-brand" href="index">Navbar</a>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>

          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
              <li class="nav-item active">
                <a class="nav-link" href="index">Home <span class="sr-only">(current)</span></a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="index">Link</a>
              </li>
              <li class="nav-item">
                <a class="nav-link disabled" href="index">Disabled</a>
              </li>
            </ul>
          </div>
        </nav>
        <div class="container-fluid">
          <div class="row flex-xl-nowrap">
            <div class="col-12 col-md-3 col-xl-2">
              <nav>
                <ul class="navbar-nav">
                  <li class="nav-item">
                    <a class="nav-link" href="#">Home</a>
                  </li>
                  <li class="nav-item">
                    <a class="" href="#">Navbar</a>
                  </li>
                  <li class="nav-item">
                    <a class="" href="#">Navbar</a>
                  </li>
                  <li class="nav-item">
                    <a class="" href="#">Navbar</a>
                  </li>
                  <li class="nav-item">
                    <a class="" href="#">Navbar</a>
                  </li>
                </ul>
              </nav>
            </div>

            <div class="col-12 col-md-9 col-xl-8 py-md-3 pl-md-5">
            <h1 className="header">Application</h1>
              <p>Test</p>
            </div>
          </div>
        </div>

      </div>
    );
  }
}
export default App;

