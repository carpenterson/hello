针对redux实现异步的helloworld程序，不用redux，只用react实现一样的功能。对比一下。

----
页面刚加载时请求数据
* redux: 在componentDidMount的时间点dispatch，给promise的回调中再dispatch
* react: 在componentDidMount的时间点setState，给promise的回调中再setState

----
交互过程中请求数据
* redux：在onChange事件中调用一次dispatch，在componentWillReceiveProps中再调用一次dispatch，给promise的回调中再dispatch
* react：在onChange事件中同步调用两次setState，给promise的回调中再setState

+ setState是异步的：`this.setState({s:s})`-->`doSomeThing(this.state.s)`可能不符合预期
+ dispatch是同步的：`dispatch(action1)`-->`reducer{s}`-->`dispatch(action2)`-->`reducer{getState().s}`

----
promise是一个代表异步操作最终完成或失败的对象。比如，函数f返回promise对象，咱不需要把回调函数传给函数f，而是绑到函数f返回的promise对象上。这样写代码很方便。

----
react生命周期
![react生命周期图片-csdn](https://images2017.cnblogs.com/blog/1106982/201708/1106982-20170811224737742-1564011484.jpg)