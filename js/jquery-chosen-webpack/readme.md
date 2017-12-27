在webpack中引用jquery和jquery的插件

参考文章：https://segmentfault.com/a/1190000006887523

如果jquery插件中把jQuery作为全局变量引用，可以通过ProvidePlugin来解决
new webpack.ProvidePlugin(
{
    jQuery: 'jquery'
}
ProvidePlugin的机制：当webpack加载到某个js模块里，出现了未定义且名称符合（字符串完全匹配）配置中的key的变量时，会自动require配置中的value所指定的js模块

同理，如果引用的是$，则配置为
new webpack.ProvidePlugin(
{
    $: 'jquery'
}

或者，配置为
new webpack.ProvidePlugin(
{
    $: 'jquery',
    jQuery: 'jquery',
    'window.jQuery': 'jquery',
    'window.$': 'jquery'
}


************
详细步骤
************
1、安装插件
npm install jquery --save
npm install chosen-js --save

2、配置webpack
新建src和build目录
新建webpack.config.js文件

配置entry
entry: './src/index.js',

配置output
output: {
    path: path.resolve(__dirname, 'build'),
    filename: 'bundle.js',
},

配置HtmlWebpackPlugin
npm install --save-dev HtmlWebpackPlugin
指定模板
new HtmlWebpackPlugin({
    title: 'Output Management',
    template: 'src/index.html'
}),


配置CleanWebpackPlugin
npm install --save-dev CleanWebpackPlugin
指定要清理的文件夹
new CleanWebpackPlugin(['build']),

3、写html
在src目录下新建index.html
在html中加一个select，用做测试

4、写js
在src目录下新建index.js

在其中引用jquery和chosen-js
import $ from "jquery";
import "chosen-js";

使用jquery插件的api
$('.chosen-select' ).chosen();

---此时
用webpack命令打包
在浏览器中打开build/index.html，按F12会看到报错信息：jQuery找不到

原因：chosen-js插件中直接引用了jQuery变量，但是这个变量并不是全局的，所以它找不到


5、配置ProvidePlugin
new webpack.ProvidePlugin(
{
    jQuery: 'jquery'
}

ProvidePlugin的机制：当webpack加载到某个js模块里，出现了未定义且名称符合（字符串完全匹配）配置中的key的变量时，会自动require配置中的value所指定的js模块

---此时
在浏览器中可以看到chosen插件起作用了。到此已经知道了jquery插件的用法。
在这一步中，看到的样式是不对的，因为我们还没有引入chosen自带的css

6、引入css
安装css-loader
npm install --save-dev style-loader css-loader

配置css-loader
module: {
    rules: [
        {
            test: /\.css$/,
            use: [
                'style-loader',
                'css-loader'
            ]
        },

---此时
webpack打包报错，因为css中引用了图片

7、配置file-loader
安装file-loader
npm install --save-dev file-loader

在rules中配置file-loader
{
    test: /\.(png|svg|jpg|gif)$/,
    use: [
        'file-loader'
    ]
}

done

*********
如果某个jquery插件连npm都没上，只能通过script标签来引用它，并且它里面用了jQuery全局变量
==> 采用expose-loader插件，将指定js模块export的变量声明为全局变量
不研究了。。。

如果想采用script标签引用jquery
==> 采用externals配置，将某个全局变量伪装成某js模块的exports
    externals: {
      'jquery': 'window.jQuery',
    },
当某个js模块显式调用var $ = require('jquery')时，会把window.jQuery返回给它