# 简单的联系表单

## 页面内容

fieldset元素类似于div充当容器的作用。使用它可以对表单进行分组，一个表单可以有多个`<fieldset></fieldset>`对，每一对为一组，每组的内容描述可以使用`<legend></legend>`说明。

使用`<br />`元素分行，这样在没有CSS的情况下也能显示正常，因为label、input、textarea元素各自占一行，就不会显示在一行内。

## 页面布局

最外层的容器ID为root，宽度设置为500px，上下外边距各50px，左右居中。

将label、input、textarea元素以块元素显示，这样可以对他们设置外边距。

IE和firefox盒子模型的宽度计算方法不一样，对fieldset设置内边距时，通常的计算方法是width+padding，而IE直接将width作为盒子的宽度。

元素`<br />`默认有一定的行间距，所以不用再对label元素设置下边距。

对input和textarea元素设置上边距来控制文本输入框与上行文本间的距离。另外，给这两元素同一个类名:text，用来设置文本输入框的样式。