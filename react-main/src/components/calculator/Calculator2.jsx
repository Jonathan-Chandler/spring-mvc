import React, { Component } from 'react';
import PropTypes from 'prop-types'
import './Calculator.css'

export class CalculatorComponent2 extends Component 
{
  constructor()
  {
    super();
    this.state = {
      counter : 0,
      secondCounter : 100
    }

    this.increment = this.increment.bind(this);
    this.decrement = this.decrement.bind(this);
    this.reset = this.reset.bind(this);
  }

  increment(amount)
  {
    console.log('increment');
    this.setState(
      (prevState) => {
        return {counter: prevState.counter + amount}
      }
    );
  }

  decrement(amount)
  {
    console.log('decrement');
    this.setState(
      (prevState) => {
        return {counter: prevState.counter - amount}
      }
    );
  }

  reset()
  {
    console.log('reset');
    this.setState({counter: 0});
  }

  render() 
  {
    return (
      <div className="calculatorComponent2">
        <div className="container">
          <div className="col">
            <div role="group" className="border btn-group">
              <button onClick={() => this.props.increment} incAmount={1} incMethod={this.increment}>+1</button>
            </div>
          </div>
          <span className="calc-total">{this.state.counter}</span>
        </div>
      </div>
    )
  }
}

export default CalculatorComponent2;

