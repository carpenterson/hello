import React, {
    Component
} from 'react'

export default class AsyncApp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedSubreddit: 'reactjs',
            postsBySubreddit: {}
        };
    }

    // react组件刚加载完的时候
    componentDidMount() {
        this.fetchPosts(this.state.selectedSubreddit);
    }

    selectSubreddit = (subreddit) => {
        this.setState({ selectedSubreddit: subreddit }); // 先把这个值选上再说
        // setState是异步的，注意后面的代码不能通过this.state.selectedSubreddit来取值
        if (!this.state.postsBySubreddit[subreddit]) {
            this.fetchPosts(subreddit);
        } 
    }
    
    fetchPosts = (subreddit) => {
        let newPosts = {
            ...this.state.postsBySubreddit,
            [subreddit]: { isFetching: true, posts: [] }
        };
        this.setState({
            postsBySubreddit: newPosts
        });
        fetch(`https://www.reddit.com/r/${subreddit}.json`)
            .then(response => response.json())
            .then((json) => {
                let newPosts = {
                    ...this.state.postsBySubreddit,
                    [subreddit]: {
                        isFetching: false,
                        posts: json.data.children.map(child => child.data),
                        lastUpdateTime: new Date(),
                    }
                };
                this.setState({
                    postsBySubreddit: newPosts
                })
            })
    }

    handleRefreshClick = () => {
       this.fetchPosts(this.state.selectedSubreddit);
    }

    render() {
        const { postsBySubreddit, selectedSubreddit } = this.state;
        let defaultInfo = { isFetching: true, posts: [], lastUpdateTime: null }; // 为null但是又选择了这个选项，说明马上就要loading了
        let subredditInfo = postsBySubreddit[selectedSubreddit] ? postsBySubreddit[selectedSubreddit] : defaultInfo;
        const isFetching = subredditInfo.isFetching;
        const posts = subredditInfo.posts;
        const lastUpdateTime = subredditInfo.lastUpdateTime;
        const isEmpty = posts.length === 0;
        const options = ['reactjs', 'frontend'];
        return (
            <div>
                <h1>{selectedSubreddit}</h1>
                <select onChange={(e) => this.selectSubreddit(e.target.value)} value={selectedSubreddit}>
                    {options.map((option) => (
                        <option key={option} value={option}>{option}</option>
                    ))}
                </select>
                <p>
                    {lastUpdateTime &&
                        <span>
                            Last updated at {new Date(lastUpdateTime).toLocaleTimeString()}{' '}
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

