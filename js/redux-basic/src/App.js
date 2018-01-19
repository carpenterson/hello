import React, { Component } from 'react'

export default class App extends Component {
    render() {
        return (
            <div>
                <p>Red: {this.props.value.red}</p>
                <p>Blue: {this.props.value.blue}</p>
                <button onClick={()=>this.props.addRed()}>Red+</button>
                <button onClick={()=>this.props.subRed()}>Red-</button>
                <button onClick={()=>this.props.addBlue()}>Blue+</button>
                <button onClick={()=>this.props.subBlue()}>Blue-</button>
            </div>
        );
    }
}