import React, { Component } from 'react';

class WelcomeComponent extends Component
{
  render() {
    return (
      <>
        <h1>welcome header</h1>
        <div className="container">
          Welcome {this.props.params.username}
        </div>
      </>
    );
  }
}

export default WelcomeComponent;
