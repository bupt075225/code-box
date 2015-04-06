# Boilerplate

这是一个前端开发模板，在各个项目中可以复用，加快开发进度。

## 基本结构

初始文件结构如下所示:

    .
    |—— CHANGELOG.md
    |—— css
    |   |——normalize.css
    |—— doc
    |—— humans.txt
    |—— images
    |—— index.html
    |—— js
    |   |—— main.js
    |   |—— vendor
    |—— README.md

**css目录**

存放项目所有的css文件，放置的初始模板css包括：

* normalize.css是支持跨浏览器的CSS重置，消除不同浏览器支持的CSS差异。
* 通用HTML元素样式，没有任何依赖，可直接使用或定制。
* 响应式设计的media queries。

**doc目录**

存放项目文档

**js目录**

存放项目所有的js文件：

* vendor目录存放第三方库例如jQuery就放在这。
* main.js存放项目的javascript代码。

**index.html**

网站所有页面的基本框架

## 排版

样式中样式使用的单位采用rem，rem是CSS3新增的一个相对单位。相对的是HTML根元素设置的字体大小，这样只通过修改HTML根元素就成比例的调整所有字体大小。IE8及更老的浏览器不支持rem，会忽略用rem设定的字体大小，解决办法是要多写一个用px为单位设置的大小。

浏览器的默认字体高是16px(1em=16px)，为简化字体大小换算，在html根元素中声明了font-size=62.5%，这样就使得16px * 62.5%=10px，12px=1.2em，10px=1em。

## 按钮

有两种按钮样式，分别应用button和button-primary这两个类到元素可以得到两种样式。
