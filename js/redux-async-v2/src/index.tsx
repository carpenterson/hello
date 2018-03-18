import * as React from 'react'
import * as ReactDOM from 'react-dom'
import { App, AppProps } from './components/App'
import { createStore, applyMiddleware } from 'redux'
import { reducer, State } from './reducers'
import { createLogger } from 'redux-logger'
import thunk from 'redux-thunk'

const logger = createLogger();

const store = createStore(reducer, applyMiddleware(thunk, logger));

const reactEl = document.getElementById('root');
const render = () => {
    ReactDOM.render(
        <App {...mapStateToProps(store.getState(), store.dispatch) } />,
        reactEl);
}

const mapStateToProps = (state: any, dispatch: Function): AppProps => {
    const { selectedSubreddit, postsBySubreddit } = state;
    const { isFetching, posts:items, lastUpdateTime } = postsBySubreddit[selectedSubreddit]
        || {
            isFetching: true,
            posts: [],
            lastUpdateTime: null
        };
    return {
        selectedSubreddit,
        dispatch,
        isFetching,
        items,
        lastUpdateTime
    };
}

store.subscribe(render);
render();
