/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.exception;


public class RedisLockException extends AvalonException {

    public Integer getCode() {
        return 1000;
    }

    public RedisLockException() {
        super("系统繁忙,请重新操作");
    }

    public RedisLockException(String mark) {
        super(mark);
    }

}
