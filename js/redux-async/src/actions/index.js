import fetch from 'isomorphic-fetch'

export const REQUEST_POSTS = 'REQUEST_POSTS';
export const RECEIVE_POSTS = 'RECEIVE_POSTS';

// 请求开始时发出的action
const requestPosts = subreddit => ({
    type: REQUEST_POSTS,
    subreddit
})

// 请求结束时发出的action
const reveivePosts = (subreddit, json) => {
    // debugger;
    return {
        type: RECEIVE_POSTS,
        subreddit,
        // 请求回来的数据做转换
        posts: json.data.children.map(child => child.data),
    };
}

// Action Creator
// 异步操作要发出两个action： 开始时发一个action，结束时发一个action（成功|失败）。
// 如何自动发出第二个action？
// 办法1：拿到原生的dispatch，自己调用两次。
// 办法2：用中间件改造dispatch，原始dispatch只接收对象（action），增强后的dispatch可接收一个函数（(dispatch)=>{...}），并且执行这个函数。

// 写法1
// export const fetchPosts = subreddit => (dispatch) => {
//     dispatch(requestPosts(subreddit));
//     return fetch(`https://www.reddit.com/r/${subreddit}.json`)
//         .then(response => response.json())
//         .then(json => dispatch(reveivePosts(subreddit, json)))
// }

// 写法2（等价于写法1）
const fetchPosts = function (subreddit) {
    const asyncAction = function (dispatch) {
        dispatch(requestPosts(subreddit));
        return fetch(`https://www.reddit.com/r/${subreddit}.json`)
            .then(response => response.json())
            .then(json => dispatch(reveivePosts(subreddit, json)))
    }
    return asyncAction;
}
export {
    fetchPosts
}