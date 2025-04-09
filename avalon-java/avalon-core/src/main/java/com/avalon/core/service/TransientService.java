package com.avalon.core.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.RecordRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 不会生成数据库表，用于处理一些临时性的任务 比如配置文件
 *
 * @author lwlianghehe@gmail.com
 * @date 2025/01/08 18:42
 */
@Slf4j
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
@Lazy
public abstract class TransientService extends AbstractService {
    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        return PrimaryKey.build(0);
    }

    @Override
    public Integer update(RecordRow recordRow) throws AvalonException {
        return 0;
    }

    @Override
    public Integer delete(RecordRow row) throws AvalonException {
        return 0;
    }
}
