# CSS

## 多级导航菜单页面

两级导航菜单很容易实现，但三级菜单会遇到第三级菜单不能显示的问题。页面中INVESTING和PROPERTY SEARCH之下还有两级菜单。使用列表来组织导航菜单内容。

## 页面布局和样式

移除第一级和第二级菜单的列表样式和内外边距。

    #nav, #nav ul
    {
    	list-style: none;
    	padding: 0;
    	margin: 0;
    }

使用相对定位方式定位第一级列表元素，向左浮动让所有的列从默认文本流中浮动起来沿水平方向(x轴方向)排列。固定列宽和列高。

    #nav li
    {
    	float: left;
    	position: relative;
    	line-height: 3.5em;
    	width: 14em;
    }

使用绝对定位方式来定位第二级导航列表，第一级列表ID是nav，使用*#nav li ul*来定义第二级列表样式(也可以用*ul#nav li ul*)。有二级落单的一级导航菜单，在鼠标移到它这上时才会显示出二级菜单，其它情况是不会显示二级菜单，所以用*display:none*将二有菜单隐藏掉，这样三级菜单更没有显示机会了。

    #nav li ul
    {
    	position: absolute;
    	margin-top: 0em;
    	margin-left: 0em;
    	display: none;
    }
这里用到一点技巧，定位第二级列表时可以用top和left与确定它的位置，但在IE中可能会存在一些问题，IE在定位z轴(z-index)位置时有问题。这里改用margin-top和margin-left来确定位置，从z轴方向看，二级菜单在一级菜单之上，三级菜单在二级菜单之，CSS代码如下：

    #nav ul li ul
    {
    	margin-top: -3em;
    	margin-left: 7em;
    }

超链接被定义为块元素，这样整个菜单按钮区都变成了一个超链接。使用*#nav a {...}*可以定义超链接样式。使用*border-right*属性来分隔一级菜单中的按钮，没有使用*margin-right:1px*的方法来分隔，原因是在子菜单样式中要去掉这个右边框时更简单，否则使用右边距会出现去掉后有空隙的情况，边框本身就是元素的一部分，所以去掉后没有这样的问题。

    #nav a
    {
    	display: block;
    	text-decoration: none;
    	color: white;
    	border-right: 1px solid;
    }

定义顶层和子层菜单的hover颜色和链接颜色。

    #nav li a:hover
    {
    	background-color: #b5091f;
    	color: white;
    }

    #nav li ul a:hover
    {
        background-color: #343434;
    	color: white;
    }

定义子菜单按钮的背景色，去掉之前定义的链接元素右边框。

    #nav ul a
    {
        border-right: none;
        background-color: #111111;
    }

二级菜单默认情况下不显示，但在鼠标移到一级菜单按钮上时要显示。如果使用*#nav li:hover ul { display:block; }*的方法来显示二级菜单，三级菜单也会一起显示出来，因为三级菜单继承了这个属性，因此先把三级菜单隐藏掉。

    #nav li:hover ul ul, #nav li.over ul ul
    {
        display: none;
    }
    
    #nav ul li:hover ul, #nav ul li.over ul
    {
        display: block;
    }

前面提到IE在定位z-index时存在问题，具体来说是在父box使用相对定位(一级菜单)，子box使用绝对定位(二、三级菜单)时显示出现问题，因为相对定位会从文档流中脱离，这种场景就要借助三维平面，增加z轴方向来定位。

    #nav 
    {
        z-index: 1;
    }
    
    #nav ul
    {
        z-index: 2;
    }
    
    #nav ul ul
    {
        z-index: 3;
    }

这种方法还没在旧IE上测试过，另外一种解决方法请参见原文介绍。

## 参考资料

1.[http://www.emanuelblagonic.com/create-your-own-drop-down-menu-with-nested-submenus-using-css-and-a-little-javascript/2006/10/11/create-your-own-drop-down-menu-with-nested-submenus-using-css-and-a-little-javascript/](http://www.emanuelblagonic.com/create-your-own-drop-down-menu-with-nested-submenus-using-css-and-a-little-javascript/2006/10/11/create-your-own-drop-down-menu-with-nested-submenus-using-css-and-a-little-javascript/)

2.[http://alistapart.com/article/dropdowns](http://alistapart.com/article/dropdowns)

3.[http://alistapart.com/article/taminglists](http://alistapart.com/article/taminglists)