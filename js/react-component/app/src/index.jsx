import React from 'react'; // 即使只用了ReactDOM，也不能在这去掉这行
import ReactDOM from 'react-dom';
import App from './App';

let renderEl = document.getElementById('root');
ReactDOM.render(<App />, renderEl);