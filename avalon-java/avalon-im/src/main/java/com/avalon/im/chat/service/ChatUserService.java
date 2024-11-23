/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.service;


import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.IUserService;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatUserService extends AbstractService implements IUserService {
    @Override
    public String getServiceName() {
        return "chat.user";
    }

    @Override
    public String getLabel() {
        return "聊天用户， company app, thirdUserId三者共同确认唯一用户id";
    }

    public final Field company = Fields.createString("公司");
    public final Field app = Fields.createString("应用");
    public final Field thirdUserId = Fields.createString("第三方用户ID");

    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }

    public Integer register(String company, String app, String thirdUserId) {
        Condition condition = Condition.equalCondition("company", company);
        condition = condition.andEqualCondition("app", app);
        condition = condition.andEqualCondition("thirdUserId", thirdUserId);
        Record select = select(condition, "id");
        RecordRow recordRow = null;
        if (!select.isEmpty()) {
            throw new AvalonException("错误");
            // return recordRow.getInteger("id");
        }
        RecordRow row = RecordRow.build();
        row.put("company", company);
        row.put("app", app);
        row.put("thirdUserId", thirdUserId);
        return insert(row).getInteger();
    }
}
