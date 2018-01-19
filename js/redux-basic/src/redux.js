
function createStore(reducer, initState) {
    let state;
    let listeners = [];

    const getState = () => { return state };

    const dispatch = (action) => {
        // reducer根据当前state和action消息，计算出下一个state
        state = reducer(state, action);
        listeners.forEach((listener) => listener());
    }

    const subscribe = (listener) => {
        listeners.push(listener);
        // 返回一个unsubscribe
        return () => {
            listeners = listeners.filter(l => l !== listener)
        }
    }

    if (initState) {
        state = initState;
    } else {
        dispatch({});
    }

    return { getState, dispatch, subscribe }
}

export { createStore };