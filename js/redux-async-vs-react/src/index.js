import React from 'react'
import ReactDOM from 'react-dom'
import App from './components/AsyncApp'

const reactEl = document.getElementById('root');
const render = () => {
    ReactDOM.render(
        <App />,
        reactEl);
}
render();