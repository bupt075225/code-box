# CSS3 滑动菜单

## 页面内容

使用无序列表来组织导航菜单，列表的每一列中是菜单的超级链接。列中嵌套包含下一级导航菜单。

## 页面布局

对导航条块和列表做全局设置，去除列表样式，内外边距设置为0.

    #nav,#nav ul
    {
    list-style: none;
    	padding: 0px;
    	margin: 0px;
    }

所有列向左浮动，使用相对定位。定义列宽和列高以及列的背景色。

    #nav li
    {
    float: left;
    	position: relative;
    	line-height: 40px;
    	width: 140px;
    	background-color: #5a8078;
    }

定位子菜单列表，使用绝对定位。与上级菜单列表外边距设置为0，左对齐。使用display属性将子菜单隐藏。

    #nav li ul
    {
        position: absolute;
    	margin-top: 0;
    	margin-left: 0;
    	display: none;
    }

设置所有列中超级链接的样式。超链接定义为块元素，左右侧内边距15px，上下侧内边距为0；颜色为#fff，去掉默认的链接下划线。

    #nav li a
    {
        display: block;
    	padding: 0 15px;
    	color: #fff;
    	text-decoration: none;
    }

显示子菜单的方法如下：

    #nav li:hover ul
    {
        display: block;
    }

IE6不支持:hover，需要用js来处理一下。

    <!--[if lte IE 6]>
    		<script type="text/javascript">
    			startList = function()
    				{
    				if(document.all && document.getElementById)
    					{
    					var navRoot = document.getElementById("nav");
    						for(i=0; i<navRoot.childNodes.length; i++)
    						{
    						var node = navRoot.childNodes[i];
    							if(node.nodeName == "LI")
    							{
    							node.onmouseover=function()
    								{
    								this.className+="over";
    								}
    								node.onmouseout=function()
    								{
    								this.className=this.className.replace("over","");
    								}
    							}
    						}
    					}
    				}
    				window.onload=startList;
    			</script>
    <![endif]-->

使用CSS3的渐变动画可以让下拉子菜单显示效果更炫一些。

## 参考资料

1.[http://www.newmediacampaigns.com/blog/nicer-navigation-with-css-transitions](http://www.newmediacampaigns.com/blog/nicer-navigation-with-css-transitions)

2.[http://www.quirksmode.org/css/condcom.html](http://www.quirksmode.org/css/condcom.html)

3.[http://files.www.gethifi.com/images/nicer-navigation-with-css-transitions/demo.html](http://files.www.gethifi.com/images/nicer-navigation-with-css-transitions/demo.html)