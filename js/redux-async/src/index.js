import React from 'react'
import ReactDOM from 'react-dom'
import App from './components/AsyncApp'
import { createStore, applyMiddleware } from 'redux'
import reducer from './reducers'
import { createLogger } from 'redux-logger'
import thunk from 'redux-thunk'

const logger = createLogger();

const store = createStore(reducer, applyMiddleware(thunk, logger));

// // 模拟log中间件
// const store = createStore(reducer);
// let next = store.dispatch;
// store.dispatch = function dispatchAndLog(action){
//     console.log('dispatching',action);
//     next(action);
//     console.log("next state", store.getState());
// }

const reactEl = document.getElementById('root');
const render = () => {
    ReactDOM.render(
        <App {...mapStateToProps(store.getState())}
            dispatch = {store.dispatch}
        />,
        reactEl);
}

const mapStateToProps = (state)=>{
    return {
        isFetching: state.isFetching,
        posts: state.posts
    }
}

store.subscribe(render);
render();

// 异步操作要发出两个action： 开始时发一个action，结束时发一个action（成功/失败）
// 如何自动发出第二个action?