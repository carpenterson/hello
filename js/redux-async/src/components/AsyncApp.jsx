import React, {
    Component
} from 'react'

import { fetchPosts } from '../actions'

export default class AsyncApp extends Component {

    // react组件刚加载完的时候
    componentDidMount() {
        const dispatch = this.props.dispatch;
        dispatch(fetchPosts('reactjs'))
    }

    render() {
        const { posts, isFetching } = this.props;
        const isEmpty = posts.length === 0;
        return (
            <div>
                <h1>{'reactjs'}</h1>
                {isEmpty
                    ? (isFetching ? <h2>Loading...</h2> : <h2>Empty.</h2>)
                    : <div>
                        {posts.map((post, i) =>
                            <li key={i}>{post.title}</li>
                        )}
                    </div>
                }
            </div>
        )
    }
}

