import React, { Component } from 'react';

export default class SelectList extends Component {

    handleChange = (e)=>{this.props.onChange(e.target.value)};

    render() {
        const {options, value} = this.props;
        let opts = [];
        for(let i in options){
            opts.push(<option key={options[i].value} value={options[i].value}>{options[i].label}</option>)
        }
        return (
            <div>
                <select value={value} onChange={this.handleChange}>
                     {opts}
                </select>
            </div>
        );
    }
}

