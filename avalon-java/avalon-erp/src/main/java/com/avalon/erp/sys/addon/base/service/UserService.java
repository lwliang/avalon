/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.condition.EqualCondition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.permission.ElevatePermissionEnum;
import com.avalon.core.permission.TemporaryElevate;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.IUserService;
import com.avalon.core.util.ImageUtils;
import com.avalon.core.util.ObjectUtils;
import com.avalon.erp.remote.IAvalonFileClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@Primary
public class UserService extends AbstractService implements IUserService {
    @Autowired
    private IAvalonFileClient avalonFileClient;

    @Override
    public String getServiceName() {
        return "base.user";
    }

    @Override
    public String getLabel() {
        return "登录账户";
    }

    protected final Field password = Fields.createPasswordField("密码");

    protected final Field account = Fields.createString("账号", true);
    public final Field avatar = Fields.createImage("头像");
    public final Field debug = Fields.createBoolean("开发者模式");

    @Override
    protected Field createNameField() {
        return Fields.createString("昵称", 100);
    }

    @TemporaryElevate({ElevatePermissionEnum.permission, ElevatePermissionEnum.recordRule})
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

    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        if (recordRow.containsKey(account) && !recordRow.containsKey(avatar)) {
            byte[] bytes = ImageUtils.generateAvatar(recordRow.get(account).toString());
            MultipartFile file = new MockMultipartFile("avatar", "avatar.png", "image/png", bytes);
            RecordRow avatar = avalonFileClient.uploadFile(file);
            if (ObjectUtils.isNotNull(avatar)) {
                recordRow.put(this.avatar, avatar.getString("url"));
            }
        }
        PrimaryKey insert = super.insert(recordRow);
        Object groupId = invokeMethod("base.service.data", "refId", "base", "base_group"); // 获取权限id
        if (ObjectUtils.isNotNull(groupId)) {
            invokeMethod("base.group", "addGroupUser", groupId, insert.getInteger());
        }

        return insert;
    }
}
