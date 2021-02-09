package com.bdsoft.crawler.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;

@Slf4j
public class CopyUtils {

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 对象拷贝
     *
     * @param from 原对象
     * @param to   转换对象类型
     * @return 转换后对象
     */
    public static <F, T> T copy(F from, Class<T> to) {
        T t = null;
        try {
            t = to.newInstance();
        } catch (Exception e) {
            log.error("实例化出错：{}", to.getName());
            return t;
        }
        BeanUtils.copyProperties(from, t);
        return t;
    }

    /**
     * 批量对象拷贝
     *
     * @param from 原对象列表
     * @param to   转换对象类型
     * @return 转换后对象列表
     */
    public static <F, T> List<T> copy(List<F> from, Class<T> to) {
        if (CollectionUtils.isEmpty(from)) {
            return Collections.emptyList();
        }
        try {
            to.newInstance();
        } catch (Exception e) {
            log.error("实例化出错：{}", to.getName());
            return Collections.emptyList();
        }

        List<T> list = new ArrayList<>(from.size());
        for (F f : from) {
            list.add(copy(f, to));
        }
        return list;
    }
}