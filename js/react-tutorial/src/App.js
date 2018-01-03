import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import './App.css';

// 三子棋
// Game
// Board HistoryList GameInfo

class Game extends Component {
    constructor() {
        super();
        this.handleClickBoard = this.handleClickBoard.bind(this);
        this.handleClickHistory = this.handleClickHistory.bind(this);
        this.state = {
            hisotryList: [Array(9).fill(null)], // 初始状态，在历史栏上不展示
            currentStep: 1  // 当前要走的是第1步
        }
    }

    handleClickBoard(index) {
        const currentStep = this.state.currentStep;
        const historyList = this.state.hisotryList;
        const currentValues = this.state.hisotryList[currentStep - 1];

        // not empty
        if (currentValues[index] || findWinLine(currentValues)) {
            return;
        }

        // player
        const player = currentStep % 2 == 1 ? 'X' : 'O';

        const newValues = currentValues.slice();
        newValues[index] = player;
        historyList[currentStep] = newValues;

        this.setState(
            {
                hisotryList: historyList.splice(0, currentStep + 1),
                currentStep: currentStep + 1
            }
        );
    }

    handleClickHistory(step) {
        this.setState(
            {
                currentStep: step + 1 // 查看第1步，表示当前待走的是第2步
            }
        )
    }

    render() {
        const currentStep = this.state.currentStep;
        const currentValues = this.state.hisotryList[currentStep - 1];
        const winLine = findWinLine(currentValues);
        let info;
        if (winLine) {
            info = 'Winner: ' + currentValues[winLine[0]];
        } else {
            const nextPlayer = currentStep % 2 == 1 ? 'X' : 'O'; // X先走
            info = 'Next player: ' + nextPlayer;
        }
        const historyCount = this.state.hisotryList.length - 1;
        return (
            <div>
                <Board values={currentValues} onClickBoard={this.handleClickBoard} winLine={winLine} />
                <div id="info-area">
                    <GameInfo info={info} />
                    <HistoryList steps={historyCount} onClickHistory={this.handleClickHistory} />
                </div>
            </div>
        );
    }
}

function findWinLine(currentValues) {
    const winSeries = [[0, 1, 2], [3, 4, 5], [6, 7, 8], [0, 3, 6], [1, 4, 7], [3, 5, 8], [0, 4, 8], [2, 4, 6]];
    for (let i = 0; i < winSeries.length; i++) {
        let allEqual = true;
        let series = winSeries[i];
        for (let j = 1; j < series.length; j++) {
            if (!currentValues[series[j]] || currentValues[series[j]] != currentValues[series[j - 1]]) {
                allEqual = false;
                break;
            }
        }
        if (allEqual) {
            return winSeries[i];
        }
    }
}

class Board extends Component {
    render() {
        let squares = [];
        const winLine = this.props.winLine;
        for (let row = 0; row < 3; row++) {
            for (let col = 0; col < 3; col++) {
                let isWinLine = winLine && winLine.indexOf(row * 3 + col) > -1;
                squares[row * 3 + col] = <Square key={row * 3 + col} value={this.props.values[row * 3 + col]} onClick={() => this.props.onClickBoard(row * 3 + col)} isWinLine={isWinLine} />;
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
        // 采用style属性来设计样式
        // https://reactjs.org/docs/dom-elements.html
        let squareStyle;
        if (this.props.isWinLine) {
            squareStyle = {
                color: "red",
            };
        } else {
            squareStyle = {};
        }
        return (
            <div style={squareStyle} className="square" onClick={this.props.onClick}>{this.props.value}</div>
        );
    }
}

class HistoryList extends Component {

    render() {
        const steps = [];
        steps[0] = (<li >
            <button onClick={() => this.props.onClickHistory(0)}>
                Go to game start
            </button>
        </li>);
        for (let step = 0; step < this.props.steps; step++) {
            steps[step + 1] = (<li>
                <button key={step} onClick={() => this.props.onClickHistory(step + 1)}>
                    Go to move #{step + 1}
                </button>
            </li>);
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