# crayontool

## 简介
封装开发中常用的工具，包括常用实体类、增强注解、拦截器等

整合spring-boot-starter实现自动配置

目录说明
```bash
crayontool-core                       - 核心包
crayontool-spring-boot-autoconfigure  - 自动配置
crayontool-spring-boot-starter        - starter
crayontool-test                       - 测试包（可运行）

mvn-install.cmd                       - starter打包脚本
```

## 功能模块
- ✔[验证码](#验证码)
- 通用
- ✔异常
- ✔国际化
- ✔接口日志
- ✔分页
- 限流
- ✔统一接口响应
- ✔参数校验
- 脱敏
- 敏感词过滤
- XSS过滤
- ZooKeeper-Curator整合
- Model-Cache（数据库访问层缓存）

### 验证码
主要实现了验证码的整合以及通用的校验逻辑实现
验证码整合主要是整合了多种验证码方便开发使用
通用的校验逻辑则是通过AOP+注解实现指定字段的验证码校验

#### 验证码整合
- 文本验证码
  - 普通文本
  - 算数文本
- 旋转验证码
- 拼图验证码

#### 通用验证码校验
通过AOP的方式使用通用的校验逻辑，标注待校验的方法以及用来校验的字段（即用户输入的验证码字段）
##### 注解说明
`@CaptchaField`
表示验证码字段，通过AOP会去获取该字段的值进行验证码校验
`@CheckCaptcha`
标识AOP增强的方法，只有被该注解标识的方法才会执行验证码校验逻辑

##### 如何使用
1. 使用`@CheckCaptcha`标识需要校验的接口方法
2. 使用`@CaptchaField`标识验证码字段
3. 继承`CheckCaptchaAspect`，实现`check`方法（该方法即为验证码的校验逻辑）
4. 使用`@Component`注解将切面注入Spring容器

### 异常
封装了自定义异常、参数校验异常的全局异常捕获器
默认`不开启`
#### 如何使用
```yaml
person:
  crayon:
    tool:
      exception:
        handler:
          constraint:
            # 开启参数校验异常捕获器
            enable: true
            # 指定响应的状态码（指的json响应中的code字段）
            code: 400
            # 指定响应的消息
            message: Bad Request
          custom:
            # 开启自定义异常捕获器
            enable: true
```

### 接口日志
通过Interceptor封装了接口日志打印功能
默认开启
#### 如何使用
```yaml
person:
  crayon:
    tool:
      log:
        # 默认开启
        enable: true
        interceptor:
          # 打印模板
          # 0：HTTP请求时间
          # 1：请求方法
          # 2：uri
          # 3：耗时（毫秒）
          format-template: "{0} | {1} | {2} | {3}ms"
          # 接口拦截路径
          add-path-patterns: /**
```

### 脱敏
结合注解和AOP实现自动脱敏功能
脱敏示例
```json
{
"code": "200",
"message": "请求成功",
"timestamp": 1664102170480,
"data": {
"email": "6********@qq.com",
"inner": {
"mobilePhone": "138****1234",
"userID": "0"
},
"userInfoInnerList": [
{
"mobilePhone": "135****5678",
"userID": "0"
}
],
"addressList": [
"神奈川********"
],
"addressMap": {
"湘北": "神奈川********",
"山王工业": "秋田县县立********"
},
"userInfoInnerMap": {
"one": {
"mobilePhone": "138****5678",
"userID": ""
},
"two": {
"mobilePhone": "138****4321",
"userID": ""
}
}
}
}
```
#### 注解说明
`@DesensitizeField`
标注需要脱敏的字段
其中有type属性用于指定脱敏的类型，具体类型参照hutool中的脱敏类型

`@Desensitize`
标注需要脱敏的方法

#### 如何使用
在application.yaml中添加如下配置开启自动脱敏，默认`不开启`
```yaml
person:
  crayon:
    tool:
      desensitization:
        enable: true
```



### 国际化
封装i18n工具类，直接注入Spring容器中，可以通过依赖注入直接使用


### 限流
通过Interceptor实现接口限流，我认为限流根据业务可以分为两种

-   针对用户的限流

    每个用户的请求频率是独立的，一个正在执行接口爆破的用户会被系统限流，而正常的用户应该不受影响

-   针对接口的限流

    这是对接口可用性的最后保障，以接口为中心的限流

已经内置了几种限流器

> 目前有的限流器：
>
> 固定窗口计数限流器
>

默认`不启用`

#### 如何使用

### Zookeeper-Curator

### Model-Cache
数据库访问层缓存，将数据库查询的结果缓存起来，用来提高服务的查询性能
使用AOP的方式实现缓存自动管理（解决缓存击穿、穿透问题等）