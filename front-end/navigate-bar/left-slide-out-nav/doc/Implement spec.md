# 左侧垂直导航菜单的实现

## 概述

使用简单的列表布局和jquery动画效果实现一个滑动导航条。

## 页面基本布局
导航条的大部分被隐藏在页面左侧，用户将鼠标移到左侧没被隐藏的部分时，导航菜单会滑出。

使用列表来组织导航条的内容。列表的列是块元素，在文档流中沿垂直方向排列。定位方式使用固定。导航条要显示在页面上所有元素的最上层，所以将z-index设置为一个很大的值。

列表的列宽为100px，通过设置链接元素a的左边距隐藏85px的宽度到页面左侧，所以实际是将链接元素隐藏到了页面左侧。如下图所示：

![](http://i.imgur.com/P1OxUtI.png)

## JS实现滑动效果

定义一个JS函数来实现导航菜单的滑出效果。因为隐藏的是链接元素，所以JS函数选择列表的列宽元素hover，当鼠标移动到列上时，对这列上的链接调用jquery动画效果函数，实现链接的左边距从-85变为-2px的滑动效果，滑动变化时间为200毫秒；当鼠标移走后左边距再从-2变为-85px。

为了在页面加载的时候向用户显示导航条，接着就让它向左滑动隐藏起来。我们将布局中链接元素的左边距设置为2px，在JS函数中加下如下这行代码来实现这个效果。

> $('#nav a').stop().animate({'marginLeft':'-85px'},1000);

## 水平导航条

也可以将导航条做成水平排列的方式。效果可以参考[http://tympanus.net/Tutorials/FixedNavigationTutorial2/](http://tympanus.net/Tutorials/FixedNavigationTutorial2/)

## 参考资料

1.[http://tympanus.net/codrops/2009/11/30/beautiful-slide-out-navigation-a-css-and-jquery-tutorial/](http://tympanus.net/codrops/2009/11/30/beautiful-slide-out-navigation-a-css-and-jquery-tutorial/)

2.[http://tympanus.net/codrops/2009/12/08/beautiful-slide-out-navigation-revised/](http://tympanus.net/codrops/2009/12/08/beautiful-slide-out-navigation-revised/)