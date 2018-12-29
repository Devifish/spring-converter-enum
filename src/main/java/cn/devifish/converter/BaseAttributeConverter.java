package cn.devifish.converter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用于转换 JPA Enum 类型数据
 * 使用枚举类型需要实现 Converter Enum 接口
 *
 * @param <E> 对象泛型Class
 * @param <V> 对应值类型 （数据存储类型）
 * @see javax.persistence.AttributeConverter
 * @author Devifish
 */
@SuppressWarnings("unchecked")
public abstract class BaseAttributeConverter<E extends ConvertibleEnum<V>, V extends Serializable> implements AttributeConverter<E, V> {

    private static final Log logger = LogFactory.getLog(BaseAttributeConverter.class);
    private final Map<V, E> enumMap;

    public BaseAttributeConverter() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<E> actualType = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
        this.enumMap = Stream.of(actualType.getEnumConstants()).collect(Collectors.toMap(ConvertibleEnum::param, e -> e));

        if (logger.isInfoEnabled())
            logger.info(String.format("Load '%s.class' Enum Converter", actualType.getSimpleName()));
    }

    /**
     * 转换枚举为param
     * @param e enum
     * @return ConverterEnum.param()
     */
    @Override
    public V convertToDatabaseColumn(E e) {
        if (e == null) return null;
        return e.param();
    }

    /**
     * param转换为枚举
     * 取值使用 HashMap Key存在 时间复杂度为 0(1)
     * @param v ConverterEnum.param()
     * @return enum
     */
    @Override
    public E convertToEntityAttribute(V v) {
        if (v == null) return null;
        return this.enumMap.get(v);
    }

}
