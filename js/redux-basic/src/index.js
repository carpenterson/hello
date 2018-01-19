import React from 'react'
import ReactDOM from 'react-dom'
import App from './App'
import { createStore } from './redux'
import reducer from './reducers'

const reactEl = document.getElementById('root');

const store = createStore(reducer);

const render = () => {
    ReactDOM.render(
        <App value={store.getState()}
            addRed={() => store.dispatch({ type: 'ADD_RED' })}
            subRed={() => store.dispatch({ type: 'SUB_RED' })}
            addBlue={() => store.dispatch({ type: 'ADD_BLUE' })}
            subBlue={() => store.dispatch({ type: 'SUB_BLUE' })}
        />,
        reactEl);
}

render();
store.subscribe(render);