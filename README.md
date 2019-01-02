# spring-converter-enum
> 为 Spring MVC 框架提供对 Enum 类型数据 param 到 enum object 转换<br/>
> 同样也可用于 Spring Boot （包含 Spring MVC）<br/>
> 基于 Java 8 开发，所以运行环境必须 java 8 或 以上

## 安装
下载 releases 里已经构建过的 jar 附件，项目导入该 jar 包<br/>
也可 clone 代码直接 copy 至项目内使用<br/>

JAR：[**下载**](https://gitee.com/Devifish/spring-converter-enum/releases) 

## 使用

#### 配置方法
##### XML 方法
```xml
<mvc:annotation-driven conversion-service="conversionServiceFactoryBean" />

<bean id="conversionServiceFactoryBean" class="org.springframework.context.support.ConversionServiceFactoryBean">
    <property name="converters">
        <set>
            <bean class="cn.devifish.spring.EnumConverterFactory" />
        </set>
    </property>
</bean>
```

#### Annotation 方法 (适用于Spring Boot)
```java
@Configuration
public class WebContextConfiguration {
    
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void addConversionConfig() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) requestMappingHandlerAdapter.getWebBindingInitializer();
        GenericConversionService genericConversionService = (GenericConversionService)initializer.getConversionService();
        genericConversionService.addConverterFactory(new EnumConverterFactory());
    }
}
```

#### 使用方法
项目内需要转换的 Enum类 需要实现 ConvertibleEnum接口<br/>
Spring MVC 即可实现 param 到 enum object 转换
```java
public enum LeaveType implements ConverterEnum<Integer> {

    病假(1), 事假(2), 婚假(3), 丧假(4), 其他(9);

    private final int param;

    LeaveType(int param) {
        this.param = param;
    }

    @Override
    public Integer param() {
        return param;
    }
}
```

如果使用了 Spring Data Jpa 的，想使数据库保存 param 值 而非 Enum index 的话<br/>
需要创建一个 jpa转换代理类
```java
public class LeaveTypeConverter extends BaseAttributeConverter<LeaveType, Integer> {}
```
实体类成员或方法上添加
```java
@Convert(converter = LeaveTypeConverter.class)
private LeaveType leaveType;
```
或
```java
@Convert(converter = LeaveTypeConverter.class)
public LeaveType getLeaveType() {
    return leaveType;
}
```