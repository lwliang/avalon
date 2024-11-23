/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.alias;

import com.avalon.core.util.ObjectUtils;

public class DefaultAliasSupport {
    private AliasMap aliasMap;

    /**
     * 获取别名，同一个containsKey,获取的别名一致
     *
     * @param aliasSupport 同一个实例
     * @return 别名
     */
    public String getAlias(IAliasRequire aliasSupport) {
        if (ObjectUtils.isNull(aliasMap)) {
            aliasMap = new AliasMap();
        }
        if (aliasMap.containsKey(aliasSupport)) {
            return aliasMap.get(aliasSupport);
        }

        String alias = getNextAlias();
        aliasMap.put(aliasSupport, alias);
        return alias;
    }

    /**
     * 判断别名是否存在
     *
     * @param alias 别名
     * @return 是否存在
     */
    public Boolean containAlias(String alias) {
        return aliasMap.containsValue(alias);
    }

    /**
     * 获取下一个别名
     *
     * @return 别名
     */
    public String getNextAlias() {
        char alias = 'a';
        int a = (int) alias + aliasMap.size();
        return String.valueOf((char) a);
    }
}
