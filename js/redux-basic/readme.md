
学习[阮一峰的Redux教程1](http://www.ruanyifeng.com/blog/2016/09/redux_tutorial_part_one_basic_usages.html)

# Redux的关键概念
* state 数据
* view 视图，可以用React来实现视图。state没有变，生成的view就不会变。
* action 用户与视图交互后，产生的消息。
* reducer 接收action，更新state。负责业务逻辑的处理。
* store 把上面这些东西糅合起来的框架，它持有state，把action分发给reducer处理，并通知view更新。

# Redux的实现
## createStore
{getState, dispatch, subscribe}

## combineReducers
一个子reducer只更新一个state属性

# 与React的区别？
react：state由组件持有，如果多个组件要共享state，就把state交给父组件持有。调用组件的setState来更新视图。
redux：state由store持有，一个应用只有一个store。只把react作为显示层，发送action事件来更新视图。
