官方教程的例子。通过CommonsChunkPlugin把代码分块打包。

https://webpack.js.org/guides/code-splitting/

1. 配置多个entry
2. 通过CommonsChunkPlugin来把公共的部分抽出来。这部分公共的xx.bundle.js，HtmlWebpackPlugin会把它对应的sript标签放在前面