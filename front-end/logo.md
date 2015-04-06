# 网站logo的两种实现

在开发网站时，logo是用`<img />`还是`<h1>`一直存在着争论。两种方法的实现分别如下：

使用logo图片：

    <a href="/" title="Return to the homepage" id="logo">
        <img src="/images/logo.gif" alt="mycompany.inc logo"/>
    </a>

使用`<h1>`需要借助CSS来实现

    <h1><a href="/" title="Return to the homepage">mycompany</a></h1>

    h1 a
    {
        width: ;
        height: ;
        display: block;
        text-indent: -8888px;
        background: url(/images/logo.gif);
    }

使用图片的原因是认为logo是网站的内容，而不是背景和样式；

## 参考资料

1. [http://csswizardry.com/2010/10/your-logo-is-an-image-not-a-h1/](http://csswizardry.com/2010/10/your-logo-is-an-image-not-a-h1/)