import * as React from 'react'

import { selectSubreddit, fetchPostsIfNeeded, fetchPosts } from '../actions'

export interface AppProps {
    dispatch: Function,
    selectedSubreddit: string,
    isFetching: boolean,
    lastUpdateTime?: Date,
    items: Array<any>
}

export class App extends React.Component<AppProps> {

    componentDidMount() {
        const { selectedSubreddit, dispatch } = this.props;
        dispatch(fetchPostsIfNeeded(selectedSubreddit));
    }

    selectSubreddit = (subreddit: string) => {
        const { dispatch } = this.props;
        // 连续两次改变store.state。同步改变。
        dispatch(selectSubreddit(subreddit)); // 改变selectedSubreddit
        dispatch(fetchPostsIfNeeded(subreddit)); // 改变isFetching
    }

    handleRefreshClick = () => {
        const {selectedSubreddit, dispatch} = this.props;
        dispatch(fetchPosts(selectedSubreddit));
    }

    render() {
        const { items, isFetching, selectedSubreddit, lastUpdateTime } = this.props;
        const isEmpty = items.length === 0;
        const options = ['reactjs', 'frontend'];
        return (
            <div>
                <h1>{selectedSubreddit}</h1>
                <select onChange={(e) => this.selectSubreddit(e.target.value)}
                    value={selectedSubreddit}>
                    {options.map((option) => (
                        <option key={option} value={option}>{option}</option>
                    ))}
                </select>
                <p>
                    {lastUpdateTime &&
                        <span>
                            Last updated at {lastUpdateTime.toLocaleTimeString()}{' '}
                        </span>
                    }
                    {!isFetching &&
                        <button onClick={this.handleRefreshClick}>
                            Refresh
                        </button>
                    }
                </p>
                {isEmpty
                    ? (isFetching ? <h2>Loading...</h2> : <h2>Empty.</h2>)
                    : <div  style={{ opacity: isFetching ? 0.5 : 1} }>
                        {items.map((item, i) =>
                            <li key={i}>{item.title}</li>
                        )}
                    </div>
                }
            </div>
        )
    }
}

