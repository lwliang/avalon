/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.alias;

import java.util.HashMap;

/**
 * 别名字典
 */
public class AliasMap extends HashMap<IAliasRequire, String> {

    @Override
    public String put(IAliasRequire aliasSupport, String value) {
        return super.put(aliasSupport, value);
    }

    public String get(IAliasRequire aliasSupport) {
        return super.get(aliasSupport);
    }
}
