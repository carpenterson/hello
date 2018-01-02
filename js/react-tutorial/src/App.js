import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import './App.css';

// 三子棋
// Game
// Board HistoryList GameInfo

class Game extends Component {

    constructor() {
        super();
        this.state = {
            hisotryList: [[null, null, null, 'X', null, null, null, null, null]],
            currentStep: 1
        }
    }

    render() {
        const currentStep = this.state.currentStep;
        const currentValues = this.state.hisotryList[currentStep - 1];
        const nextPlayer = currentStep % 2 == 0 ? 'X' : 'O'; // X先走
        const info = 'Next player: ' + nextPlayer;
        const historyCount = this.state.hisotryList.length;
        return (
            <div>
                <Board values={currentValues} />
                <div id="info-area">
                    <GameInfo info={info} />
                    <HistoryList steps={historyCount}/>
                </div>
            </div>
        );
    }
}

class Board extends Component {
    render() {
        let squares = []
        for (let row = 0; row < 3; row++) {
            for (let col = 0; col < 3; col++) {
                squares[row * 3 + col] = <Square key={row * 3 + col} value={this.props.values[row * 3 + col]} />;
            }
        }

        return (
            <div id="board">
                {squares}
            </div>
        );
    }
}

class Square extends Component {
    render() {
        return (
            <div className="square">{this.props.value}</div>
        );
    }
}

class HistoryList extends Component {
    
    render() {
       const steps = [];
       for (let step =0 ; step < this.props.steps; step++){
           steps[step] = <li key={step}>step {step+1}</li>;
       }

        return (
            <div id="hisotry">
                <ol>
                    {steps}
                </ol>
            </div>
        );
    }
}

class GameInfo extends Component {
    render() {
        return (
            <div id="game-info">{this.props.info}</div>
        );
    }
}

class App extends React.Component {
    render() {
        return (
            <Game />
        );
    }
}


export { App };