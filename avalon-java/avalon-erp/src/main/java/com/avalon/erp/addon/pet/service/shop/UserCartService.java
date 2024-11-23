/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.shop;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;
import com.avalon.erp.addon.pet.service.shop.commodity.PetCommodityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class UserCartService extends AbstractService {
    public UserCartService(PetCommodityService petCommodityService) {
        this.petCommodityService = petCommodityService;
    }

    @Override
    public String getServiceName() {
        return "pet.user.cart";
    }

    @Override
    public String getLabel() {
        return "用户购物车";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field userId = Fields.createMany2one("用户", "pet.user");
    public final Field commodityId = Fields.createMany2one("商品", "pet.commodity");
    public final Field count = Fields.createInteger("数量");
    @Deprecated()
    public final Field price = Fields.createBigDecimal("价格"); // 弃用

    /**
     * 添加商品到购物车
     *
     * @param productId 商品id
     * @param userId    用户id
     */
    public void addCartProduct(Integer productId, Integer userId, Integer count) {
        Condition condition = this.userId.eq(userId)
                .andCondition(this.commodityId.eq(productId));
        Record search = search(condition);
        RecordRow recordRow = search.get(0);
        if (ObjectUtils.isNull(recordRow) || recordRow.isEmpty()) {
            recordRow = RecordRow.build();
            recordRow.put("userId", userId);
            recordRow.put("commodityId", productId);
            recordRow.put("count", 1);
            insert(recordRow);
            return;
        }
        RecordRow updateRecordRow = RecordRow.build();
        updateRecordRow.put("count", count);
        updateNumberValue(updateRecordRow,
                getPrimaryKeyField().eq(recordRow.getRawValue(getPrimaryKeyField())));
    }

    private final PetCommodityService petCommodityService;

    /**
     * 获取用户购物车的商品
     *
     * @param userId 用户id
     * @return 产品
     */
    public Record getCartProductByUserId(Integer userId) {
        Record result = select(this.userId.eq(userId),
                "id", "count", "commodityId.id", "commodityId.name", "commodityId.firstUrl");
        for (RecordRow recordRow : result) {
            if (!recordRow.containsKey("commodityId")) {
                continue;
            }
            FieldValue fieldValue = petCommodityService.getFieldValue("price",
                    Condition.equalCondition(petCommodityService.getPrimaryKeyField(),
                            recordRow.getRecordRow("commodityId").getInteger("id")));
            recordRow.put("price", fieldValue.getBigDecimal());
        }
        return result;
    }
}
