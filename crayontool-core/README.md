`HttpServletRequest`

uri路径相关各个方法的区别

`HttpServletRequest.getRequestURL().toString()`

URL（统一资源定位器），能够唯一标识一个资源

`http://127.0.0.1:8080/crayontool-test/hello/param0`

`HttpServletRequest.getContextPath()`

Servlet上下文路径，打包在Tomcat哪个路径下

可以通过yaml配置

`/crayontool-test`

`HttpServletRequest.getServletPath()`

只显示Servlet路径

`/hello/param0`

`HttpServletRequest.getRequestURI()`

URI（统一资源标识符），同一站点下标识一个资源

`/crayontool-test/hello/param0`