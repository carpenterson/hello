import * as fetch from 'isomorphic-fetch'

export enum ActionType {
    SELECT_SUBREDDIT = 'SELECT_SUBREDDIT', // 选择子版块
    REQUEST_POSTS = 'REQUEST_POSTS', // 发送请求
    RECEIVE_POSTS= 'RECEIVE_POSTS' // 请求结束
}

export interface Action{
    type: ActionType,
    subreddit: string,
    posts?:Array<any>
}

export const selectSubreddit = (subreddit:string):Action =>({
    type:ActionType.SELECT_SUBREDDIT,
    subreddit
})

// 请求开始时发出的action
const requestPosts = (subreddit:string):Action => ({
    type: ActionType.REQUEST_POSTS,
    subreddit
})

// 请求结束时发出的action
const reveivePosts = (subreddit:string, json:any):Action => {
    return {
        type: ActionType.RECEIVE_POSTS,
        subreddit,
        // 请求回来的数据做转换
        posts: json.data.children.map((child:any) => child.data),
    };
}

export const fetchPosts = (subreddit:string) => (dispatch:any) => {
    dispatch(requestPosts(subreddit));
    return fetch(`https://www.reddit.com/r/${subreddit}.json`)
        .then(response => response.json())
        .then(json => dispatch(reveivePosts(subreddit, json)))
}

export const fetchPostsIfNeeded = (subreddit:string) => (dispatch:Function, getState:Function) => {
    const subredditInfo = getState()['postsBySubreddit'][subreddit];
    if(subredditInfo == undefined ){
        dispatch(fetchPosts(subreddit));
    }
}
 