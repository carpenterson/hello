在webpack中引用jquery和jquery的插件

参考文章：https://segmentfault.com/a/1190000006887523

## 详细步骤

##### 在js代码中引用jquery和chosen-js
```
import $ from "jquery";
import "chosen-js";
```

##### 使用jquery插件的api
```
$('.chosen-select' ).chosen();
```

* 此时打包后在浏览器中看到报错：**jQuery找不到**  
原因：chosen-js插件中直接引用了jQuery变量，但是这个变量并不是全局的，所以它找不到  


##### 配置ProvidePlugin
```
new webpack.ProvidePlugin(
{
    jQuery: 'jquery'
}
```

##### ProvidePlugin的机制
当webpack加载到某个js模块里，出现了未定义且名称符合（字符串完全匹配）配置中的变量（jQuery）时，会自动require所指定的js模块(jquery)  

同理，如果引用的是$，则配置为
```
new webpack.ProvidePlugin(
{
    $: 'jquery'
}
```

万全的配置为
```
new webpack.ProvidePlugin(
{
    $: 'jquery',
    jQuery: 'jquery',
    'window.jQuery': 'jquery',
    'window.$': 'jquery'
}
```


##### 样式  
需要引入chosen自带的css，才能实现最后的效果
```
import '../node_modules/chosen-js/chosen.css';
```


## 其他方案
##### 如果某个jquery插件连npm都没上，只能通过script标签来引用它，并且它里面用了jQuery全局变量
==> 采用expose-loader插件，将指定js模块export的变量声明为全局变量
不研究了。。。

##### 如果想采用script标签引用jquery  
==> 采用externals配置，将某个全局变量伪装成某js模块的exports。打包出的bundle中不会包含jquery
```
    externals: {
      'jquery': 'window.jQuery',
    },
```
当某个js模块显式调用var $ = require('jquery')时，会把window.jQuery返回给它