import React, { Component } from 'react';
import AppMain from './components/app/AppMain';
import './App.css';
import 'bootstrap/dist/css/bootstrap.css';
import { AuthProvider } from './components/authentication/AuthProvider.tsx'

class App extends Component 
{
  render() 
  {
    return (
      <div className="App">
        <AuthProvider>
          <AppMain/>
        </AuthProvider>
      </div>
    );
  }
}
export default App;

