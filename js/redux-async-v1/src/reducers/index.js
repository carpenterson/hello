import {
    combineReducers
} from 'redux';
import {
    REQUEST_POSTS,
    RECEIVE_POSTS
} from '../actions'


const posts = (state = [], action) => {
    switch (action.type) {
        case REQUEST_POSTS:
            return [];
        case RECEIVE_POSTS:
            return action.posts;
        default:
            return state;
    }
}

const isFetching = (state, action) => {
    switch (action.type) {
        case REQUEST_POSTS:
            return true;
        default:
            return false;
    }
}

export default combineReducers({
    posts,
    isFetching
})