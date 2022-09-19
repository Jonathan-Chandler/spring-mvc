import React, { Component } from 'react';
//import FlashCardApp from './components/flashcard/FlashCardApp';
import TodoApp from './components/todo/TodoApp';
import './App.css';
import 'bootstrap/dist/css/bootstrap.css';

class App extends Component 
{
  render() 
  {
    return (
      <div className="App">
        {/*<div class="container-fluid">
          <div class="row flex-xl-nowrap">
            <div class="col-12 col-md-3 col-xl-2">
              <nav>
                <ul class="navbar-nav">
                  <li class="nav-item">
                    <a class="nav-link" href="index">Home</a>
                  </li>
                  <li class="nav-item">
                    <a class="" href="index">Navbar</a>
                  </li>
                  <li class="nav-item">
                    <a class="" href="index">Navbar</a>
                  </li>
                  <li class="nav-item">
                    <a class="" href="index">Navbar</a>
                  </li>
                  <li class="nav-item">
                    <a class="" href="index">Navbar</a>
                  </li>
                </ul>
              </nav>
            </div>

            <div class="col-12 col-md-9 col-xl-8 py-md-3 pl-md-5">
              <h1 className="header">Application</h1>
            </div>
          </div>
        </div>

        {/*<CalculatorComponent/>*/}
        {/*<FlashCardApp/>*/}
        <TodoApp/>
      </div>
    );
  }
}
export default App;

