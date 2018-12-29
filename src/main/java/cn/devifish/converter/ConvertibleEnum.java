package cn.devifish.converter;

import java.io.Serializable;

/**
 * 用于实现 Spring ConverterFactory 与 JPA AttributeConverter接口
 * 通过 param 实现到 Enum 的转换
 *
 * @param <V> param 类型
 * @author Devifish
 */
public interface ConvertibleEnum<V extends Serializable> {

    V param();

}
