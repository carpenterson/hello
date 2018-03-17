1. 学习了[阮一峰的Redux教程2][]。
2. [redux官方教程][]没有学，看了大纲，阮一峰的教程与之差不多。
3. [github上的例子][]中用了react-redux的API，且代码略多。这个只实现了一个功能来了解异步：在componentDidMount中发一个请求，请求过程中显示Loading...。

## 中间件
* 中间件的目的：对redux加一些功能，比如打日志。
* 中间件的实现手段：在发送action的点切入，对store.dispatch进行增强。

## 异步操作
异步操作要发出两个action： 操作开始时发一个action，结束时发一个action（成功|失败）。如何自动发出第二个action？
* 办法1：拿到原始的dispatch，自己调用两次。
* 办法2：写一个`asyncDispatchAction`，然后把它像普通action一样传给dispatch。这样就需要对dispatch用中间件加强，因为原始dispatch只接收对象（action）。
``` js
actionCreator(args)
{
    const asyncDispatchAction = function(dispatch, getState){
        dispatch(beginAction);
        doSomething(args).then(()=>{dispatch(endAction)});
    }
    return asyncDispatchAction;
}
enhancedDispatch(asyncDispatchAction){
    asyncDispatchAction(dispatch, getState);
}
```

## 如果不用redux，react怎么实现异步
在componentDisMount中实现异步，把dispatch改成setState
``` js
doSomething().then(()=>{setState()})
```

## todo
* 把例子完善。componentDidMount是什么时候会触发，只是页面加载的时候触发一次吗，还是后面调用了render方法之后还会再触发？什么是mount？
* 如果不用redux，只用react怎么实现一样的功能

[阮一峰的Redux教程2]:(http://www.ruanyifeng.com/blog/2016/09/redux_tutorial_part_two_async_operations.html)
[redux官方教程]:(https://redux.js.org/advanced/async-actions)
[github上的例子]:(https://github.com/reactjs/redux/tree/master/examples/async)