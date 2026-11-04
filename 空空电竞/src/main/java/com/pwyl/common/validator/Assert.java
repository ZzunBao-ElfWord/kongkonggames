package com.pwyl.common.validator;

import com.pwyl.common.exception.PwylException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 *
 */
 public class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new PwylException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new PwylException(message);
        }
    }
}
