import React, { Component } from 'react';
import PropTypes from 'prop-types'
import './Increment.css'

export class Increment extends Component 
{
  constructor()
  {
    super();
    this.state = {
      counter : 0
    }

    this.adder = this.adder.bind(this);
  }

  // auto bind
  // increment = () => {

  adder()
  {
    console.log('increment adder');
    this.setState({
      counter: this.state.counter + this.props.incAmount
    });
  }

  render() 
  {
    return (
      <div className="increment">
        <div className="container">
          <div className="row">
            <div role="group" className="border btn-group">
              <button 
                onClick={this.adder} 
                type="button" 
                className="btn btn-secondary btn-lg inc-button"
              >
                +{this.props.incAmount}
              </button>
            </div>
          </div>
            <span className="calc-total">{this.state.counter}</span>
        </div>
      </div>
    );
  }
}

Increment.defaultProps = {
  incAmount : 1
};

Increment.propTypes = {
  incAmount : PropTypes.number
};

export default Increment;
