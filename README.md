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
- 验证码
- 通用
- 异常
- 国际化
- 接口日志
- 分页
- 限流
- 接口响应
- 参数校验

### 验证码
主要实现了验证码的整合以及通用的校验逻辑实现
验证码整合主要是整合了多种验证码方便开发使用
通用的校验逻辑则是通过AOP+注解实现指定字段的验证码校验
#### 验证码整合

#### 通用验证码校验
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

### 国际化
封装i18n工具类，直接注入Spring容器中，可以通过依赖注入直接使用


### 限流
通过Interceptor实现接口限流
已经内置了几种限流器
> 目前有的限流器：
> 
> 固定窗口计数限流器

默认`不启用`

#### 如何使用
