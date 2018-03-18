import {
    combineReducers
} from 'redux';
import {
    ActionType,
    Action
} from '../actions'

export interface State{
    selectedSubreddit: string,
    postsBySubreddit: any
}

const selectedSubreddit = (state: string = 'reactjs', action: Action) => {
    switch (action.type) {
        case ActionType.SELECT_SUBREDDIT:
            return action.subreddit;
        default:
            return state;
    }
}

interface SubRedditInfo {
    isFetching: boolean,
    posts: Array<any>,
    lastUpdateTime?: Date
}

const posts = (state: SubRedditInfo = { isFetching: true, posts: [] }, action: Action) => {
    switch (action.type) {
        case ActionType.REQUEST_POSTS:
            return {
                ...state,
                // posts:[], // 在发出请求的时候，先把当前的数据清空
                isFetching: true
            }
        case ActionType.RECEIVE_POSTS: {
            return {
                ...state,
                isFetching: false,
                posts: action.posts,
                lastUpdateTime: new Date()
            }
        }
        default:
            return state;
    }
}

const postsBySubreddit = (state: any = {}, action: Action) => {
    switch (action.type) {
        case ActionType.REQUEST_POSTS:
        case ActionType.RECEIVE_POSTS:
            return {
                ...state,
                [action.subreddit]: posts(state[action.subreddit], action)
            }
        default:
            return state;
    }
}

export const reducer = combineReducers({
    selectedSubreddit,
    postsBySubreddit
})