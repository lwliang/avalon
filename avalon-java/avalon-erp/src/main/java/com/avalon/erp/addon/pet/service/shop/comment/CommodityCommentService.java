/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.shop.comment;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommodityCommentService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.commodity.comment";
    }

    @Override
    public String getLabel() {
        return "商品评论";
    }

    protected final Field comment = Fields.createString("评论");
    protected final Field commodityId = Fields.createMany2one("商品", "pet.commodity");
    protected final Field userId = Fields.createMany2one("用户", "pet.user");
    protected final Field userName = Fields.createString("用户名"); // 匿名，则显示为匿名
    protected final Field orderId = Fields.createMany2one("订单", "pet.order");
    public final Field rate = Fields.createInteger("星级");
    public final Field anonymous = Fields.createBoolean("匿名");

    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        if (recordRow.containsKey(commodityId) &&
                recordRow.containsKey(userId) &&
                recordRow.containsKey(orderId)) {
            Condition condition = commodityId.eq(recordRow.getRawValue(commodityId))
                    .andCondition(userId.eq(recordRow.get(userId)))
                    .andCondition(orderId.eq(recordRow.get(orderId)));
            Record select = select(condition, getPrimaryKeyName());
            if (!select.isEmpty()) {
                return PrimaryKey.build(getPrimaryKeyValue(select.get(0)));
            }
        }
        return super.insert(recordRow);
    }
}
