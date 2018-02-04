import React, { Component } from 'react'
import SelectList from 'select-list'

export default class App extends Component {
    options = [];
    constructor(props){
        super(props);
        this.state = {selectValue: 'red'};
        this.initOptions();
    }

    initOptions(){
        this.options.push({ value: 'red', label: 'Red' });
        this.options.push({ value: 'blue', label: 'Blue' });
    }

    handleChange = (value) =>{
        this.setState({selectValue: value});
    }

    render() {
        return (
            <div>
                <h1>Hello</h1>
                <SelectList options={this.options} value={this.state.selectValue} onChange={this.handleChange}/>
            </div>
        );
    }
}