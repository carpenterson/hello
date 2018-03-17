学习前端工程的配置，采用TypeScript和React，用webpack打包  
1. TypeScript配置
2. Babel配置
3. 配置loader

对官网例子做了一点改动，用了babel。对.tsx文件，先用awesome-typescript-loader翻译为es6，再用babel-loader翻译为es5。TypeScript和Babel的配置是前后衔接的。


# 官网的例子
https://www.typescriptlang.org/docs/handbook/react-&-webpack.html

## 1.未使用babel
typescript本身就可以翻译JSX

## 2.通过script标签引入react
1. 在html中写script标签
2. 在webpack.config.js的externals中配置`"react": "React"`,  
效果是：代码中的`import xx from 'react'`会翻译成对全局变量React的引用  

# 参考链接
TypeScript的JSX配置  
http://www.typescriptlang.org/docs/handbook/jsx.html  

# 详细步骤

## 1.安装package
```
npm init -y 
npm install --save react react-dom @types/react @types/react-dom
npm install --save-dev typescript awesome-typescript-loader source-map-loader
npm install --save-dev babel-loader babel-core babel-preset-es2015  babel-preset-react
npm install --save-dev webpack html-webpack-plugin clean-webpack-plugin style-loader css-loader webpack-dev-server
``` 

## 2.建目录结构

## 3.配置tsconfig.json

## 4.配置.babelrc

## 5.配置webpack.config.js
* entry
* output
* resolve
* loader
* plugins
* devtool

# TODO
1. 阅读TypeScript文档  
https://www.typescriptlang.org/docs/handbook/basic-types.html
