package com.llf.basemodel.commonactivity;

import android.support.annotation.Nullable;

/**
 * Created by wangjiawei on 2017-9-28.
 */

public class RxDataTool {

    /**
     * 判断字符串是否为空 为空即true
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNullString(@Nullable String str) {
        return str == null || str.length() == 0 || "".equals(str) || "null".equals(str);
    }
}
