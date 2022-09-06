import React, { Component } from 'react';
import PropTypes from 'prop-types'
import './Calculator.css'

export class CalculatorComponent extends Component 
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

  // auto bind
  // increment = () => {

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
      <div className="calculatorComponent">
        <div className="container">
          <div className="col">
            <div role="group" className="border btn-group">
              <IncrementButton incAmount={1} incMethod={this.increment}/>
              <IncrementButton incAmount={5} incMethod={this.increment}/>
              <IncrementButton incAmount={10} incMethod={this.increment}/>
            </div>
            <div role="group" className="border btn-group">
              <DecrementButton decAmount={1} decMethod={this.decrement}/>
              <DecrementButton decAmount={5} decMethod={this.decrement}/>
              <DecrementButton decAmount={10} decMethod={this.decrement}/>
            </div>
            <div className="row">
            <div role="group" className="border btn-group">
              <ResetButton resetMethod={this.reset}/>
            </div>
            </div>
          </div>
          <div className="row">
            <span className="calc-total">{this.state.counter}</span>
          </div>
        </div>
      </div>
    );
  }
}

export class IncrementButton extends Component 
{
  constructor()
  {
    super();
    this.state = {
      counter : 0
    }

    this.increment = this.increment.bind(this);
  }

  // auto bind
  // increment = () => {

  increment()
  {
    this.props.incMethod(this.props.incAmount)

    //this.setstate(
    //  (prevState) => {
    //    return {counter: prevState.counter + this.props.incAmount}
    //  }
    //);
    //this.setstate({
    //    counter: this.state.counter + this.props.incAmount}
    //  
    //);
  }

  render() 
  {
    return (
      <div className="incrementButton">
              <button 
                onClick={this.increment}
                type="button" 
                className="btn btn-secondary btn-lg calc-button"
              >
                +{this.props.incAmount}
              </button>
      </div>
    );
  }
}

export class DecrementButton extends Component 
{
  constructor()
  {
    super();
    this.state = {
      counter : 0
    }

    this.decrement = this.decrement.bind(this);
  }

  decrement()
  {
    this.props.decMethod(this.props.decAmount)
  }

  render()
  {
    return (
      <div className="decrementButton">
        <button
          onClick={this.decrement}
          type="button"
          className="btn btn-secondary btn-lg calc-button"
        >
          -{this.props.decAmount}
        </button>
      </div>
    );
  }
}

export class ResetButton extends Component 
{
  constructor()
  {
    super();
    this.state = {
      counter : 0
    }

    this.reset = this.reset.bind(this);
  }

  reset()
  {
    this.props.resetMethod()
  }

  render()
  {
    return (
      <div className="decrementButton">
        <button
          onClick={this.reset}
          type="button"
          className="btn btn-secondary btn-lg calc-button"
        >
          Reset
        </button>
      </div>
    );
  }
}

function doNothing(val)
{
  console.log('doNothing');
}

IncrementButton.defaultProps = {
  incAmount : 1,
  incMethod : doNothing
};

IncrementButton.propTypes = {
  incAmount : PropTypes.number
};

DecrementButton.defaultProps = {
  decAmount : 1,
  decMethod : doNothing
};

IncrementButton.propTypes = {
  decAmount : PropTypes.number
};

export default CalculatorComponent;
