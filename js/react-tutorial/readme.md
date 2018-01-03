练习react官网的React-Tutorial
1. 不采用create-react-app，而是从头搭建一个简单可运行的环境
2. 从零开始写代码，包括官网教程中提供的代码框架也自己写出来



# 详细步骤-搭环境

## 1、安装package  
`npm init -y`  
`npm install --save react react-dom`  
`npm install --save-dev babel-loader babel-core babel-preset-es2015  babel-preset-react webpack`  
`npm install --save-dev html-webpack-plugin clean-webpack-plugin style-loader css-loader webpack-dev-server`    

需了解下怎么配置babel  
https://doc.webpack-china.org/loaders/babel-loader/#-  
https://github.com/thejameskyle/babel-handbook/blob/master/translations/zh-Hans/user-handbook.md#toc-babel-core

没有采用spring教程中的配置，module.loaders语法是webpack2的，webpack3的新语法是module.rules  
https://spring.io/guides/tutorials/react-and-spring-data-rest/

## 2、目录结构
build  
src  
  index.html  
  index.js  

## 3、webpack.config.js
* entry
* output
* devServer
* plugins
* module:{rule:[{}]}

`{
   test: /\.js$/,
   exclude: /(node_modules)/,
   use: {
       loader: 'babel-loader',
       options: {
           presets: ['es2015', 'react']
       }
   }
},`

除babel的配置外，其余配置和jquery-chosen-webpack工程的一样。

## 4、index.html
写一个id=root的div

## 5、index.js
写业务代码
  
  
# 详细步骤-开发代码  

## 1、画静态页面
页面分块，设计好div  
样式调好  

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