练习react官网的React-Tutorial
1. 不采用create-react-app，而是从头搭建一个简单可运行的环境
2. 从零开始写代码，包括官网教程中提供的代码框架也自己写出来



# 搭建工程骨架

## 1、安装package  
```
npm init -y
npm install --save-dev webpack webpack-dev-server html-webpack-plugin clean-webpack-plugin  style-loader css-loader
npm install --save react react-dom
npm install --save-dev babel-loader babel-core babel-preset-es2015  babel-preset-react
```
1. 没有使用less-loader
2. 没有使用babel-preset-stage-0，所以代码中不能写`f=()=>{}`这种格式的代码

## 2、创建目录结构
```bat
mkdir src
mkdir build
type nul>webpack.config.js
type nul>readme.md
cd src
type nul>index.html
type nul>index.js
```
build  
src  
--index.html  
--index.js  
webpack.config.js

## 3、webpack.config.js
* entry
* output
* devServer
* plugins
* module:{rule:[{}]}

## 4、index.html
写一个id=root的div

## 5、index.js
写业务代码


# 开发业务代码  

## 1、画静态页面
+ 页面分块，设计好div
+ 设计好样式  

参考  
http://zh.learnlayout.com/ 

## 2、设计对应的react component

## 3、把要修改的数据作为state

## 4、加上事件处理


# 对比官网教程中的代码
https://codepen.io/gaearon/pen/gWWZgR?editors=0010

1. 标题叫做Tic Tac Toe
2. 页面布局用了flex
3. 历史记录列表，在li中套了button，更好看 
4. 新建一个有初始值的空数组：Array(9).fill(null)

# TODO
1. flex布局