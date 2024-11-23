/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * 选择字段的值 Object是数据库保存的值，String是用于web前端显示的值
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SelectionHashMap extends HashMap<Object, String> {

    private Class<? extends Enum> type;
}
