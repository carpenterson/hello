
完善异步请求数据的例子。参照[例子](https://github.com/reactjs/redux/tree/master/examples/async)
* 可以选择不同的主题
* 一个主题的内容加载后就缓存起来，下次再切到这个主题不再重新加载
* 可以刷新内容

1. 画静态页面，定义好props：
* selectedSubreddit, isFetching, items, lastUpdateTime

2. 定义好state的数据结构，在reducers中，每个属性对应一个方法: 
* selectedSubreddit, SubredditInfo{isFetching, posts, lastUpdateTime}

3. 定义好Action，与用户交互动作对应: 
* 初始化后加载数据 fetchPostsIfNeeded。异步操作包含：一个函数，两个Action对象。
* 选择子版块。selectSubreddit。选完之后要加载数据，复用fetchPostsIfNeeded。为什么要在componentWillReceiveProps中？
* 刷新数据。异步操作：直接调用fetchPosts

4. 在App中加上onChange等事件

5. 在reducers中根据Action来修改state

6. 把state映射到props上

----
没有使用didInvalidate。觉得没必要：
1. fetchPostsIfNeeded的判断逻辑中只需根据当前state是否包含对应信息就行，包含就不fetch，不包含就fetch；
2. refresh直接调用fetch就行了，“先置数据失效再fetchPostsIfNeeded”的办法好像太绕了。
