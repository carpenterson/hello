针对redux实现异步的helloworld程序，不用redux，只用react实现一样的功能。对比一下。

----
页面刚加载时请求数据
* redux: 在componentDidMount的时间点dispatch，给promise的回调中再dispatch
* react: 在componentDidMount的时间点setState，给promise的回调中再setState

----
交互过程中请求数据
* redux：在onChange事件中调用一次dispatch，在componentWillReceiveProps中再调用一次dispatch，给promise的回调中再dispatch
* react：在onChange事件中同步调用两次setState，给promise的回调中再setState

+ setState两次: setState(1) -- setState(2) -- 可能触发两次render，也可能只有一次？
+ dispatch两次：`dispatch(1)` --> ReactDOM.render(){componentWillReceiveProps{`dispatch(2)`}} --> ReactDOM.render(){componentWillReceiveProps{不满足dispatch条件}}

----
promise是一个代表异步操作最终完成或失败的对象。比如，函数f返回promise对象，咱不需要把回调函数传给函数f，而是绑到函数f返回的promise对象上。这样写代码很方便。