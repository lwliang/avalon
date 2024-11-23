/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.condition.EqualCondition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService extends AbstractService implements IUserService {
    @Override
    public String getServiceName() {
        return "base.user";
    }

    protected final Field password = Fields.createString("密码");

    protected final Field account = Fields.createString("账号", true);

    @Override
    protected Field createNameField() {
        return Fields.createString("昵称", 100);
    }

    public Record login(String account, String password) {
        Condition condition = new EqualCondition("account", account);
        condition = condition.andCondition(new EqualCondition("password", password));

        return select(condition, "id");
    }

    public void register(String name, String account, String password) {
        RecordRow row = new RecordRow();
        row.put("name", name);
        row.put(this.account, account);
        row.put(this.password, password);
        insert(row);
    }
}
