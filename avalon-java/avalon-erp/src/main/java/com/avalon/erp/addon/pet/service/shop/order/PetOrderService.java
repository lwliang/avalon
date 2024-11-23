/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.shop.order;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.SnowflakeIdWorker3rd;
import com.avalon.core.util.SnowflakeIdWorker4rd;
import com.avalon.erp.addon.pet.model.enums.OrderStateEnum;
import com.avalon.erp.addon.pet.service.shop.UserCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PetOrderService extends AbstractService {
    public PetOrderService(UserCartService userCartService) {
        this.userCartService = userCartService;
    }

    @Override
    public String getServiceName() {
        return "pet.order";
    }

    private final UserCartService userCartService;

    @Override
    public String getLabel() {
        return "订单";
    }

    protected final Field userId = Fields.createMany2one("用户", "pet.user");
    protected final Field addressId = Fields.createMany2one("地址", "pet.address");
    protected final Field address = Fields.createString("街道地址");
    protected final Field province = Fields.createString("省");
    protected final Field city = Fields.createString("市");
    protected final Field district = Fields.createString("区");
    protected final Field longitude = Fields.createFloat("经度");
    protected final Field latitude = Fields.createFloat("纬度");
    public final Field addressDetail = Fields.createString("详细地址"); // =province+city+district+address
    protected final Field phone = Fields.createString("收件人电话");
    protected final Field receiverName = Fields.createString("收件人");

    protected final Field senderPhone = Fields.createString("寄件电话");
    protected final Field senderName = Fields.createString("寄件人");
    public final Field sendAddressDetail = Fields.createString("寄件详细地址"); //
    protected final Field postCode = Fields.createString("邮编");
    protected final Field state = Fields.createSelection("订单状态", OrderStateEnum.class);
    public final Field isPay = Fields.createBoolean("已支付");
    private final Field patDate = Fields.createDate("支付时间");
    public final Field isSend = Fields.createBoolean("已发货");
    public final Field sendDate = Fields.createDate("发货时间");
    public final Field isReceive = Fields.createBoolean("已收货");
    public final Field receiveDate = Fields.createDate("收货时间");
    public final Field isComment = Fields.createBoolean("已评价");
    public final Field commentDate = Fields.createDate("评价时间");
    public final Field isCancel = Fields.createBoolean("已取消");
    public final Field cancelDate = Fields.createDate("取消时间");

    public final Field orderDetail = Fields.createOne2many("订单详情", "pet.order.detail", "orderId");
    public final Field orderNo = Fields.createString("订单号");
    public final Field totalAmount = Fields.createBigDecimal("总金额");

    public final Field expressNo = Fields.createString("快递单号");
    public final Field expressCompany = Fields.createString("快递公司");
    public final Field expressFree = Fields.createBigDecimal("快递费");
    public final Field expressState = Fields.createString("快递最新状态");

    public final Field backExpressNo = Fields.createString("退货快递单号");
    public final Field backExpressCompany = Fields.createString("退货快递公司");
    public final Field backReason = Fields.createString("退单原因");
    public final Field backDate = Fields.createDate("退单时间");

    @Override
    protected void checkBeforeInsert(RecordRow recordRow) throws AvalonException {
        super.checkBeforeInsert(recordRow);
        recordRow.put(orderNo, SnowflakeIdWorker4rd.nextUId());
    }

    @Override
    protected void checkAfterInsert(RecordRow recordRow) throws AvalonException {
        super.checkAfterInsert(recordRow);
        if (recordRow.containsKey(orderDetail)) {
            recordRow.getRecord("orderDetail").forEach(row -> {
                userCartService.delete(
                        Condition.equalCondition("commodityId", row.get("commodityId"))
                                .andCondition(Condition.equalCondition("userId", recordRow.get("userId"))));
            });
        }
    }

    @Override
    protected void checkAfterUpdate(RecordRow recordRow) throws AvalonException {
        super.checkAfterUpdate(recordRow);
        /*FieldValue sumFieldValue = getServiceBean("pet.order.detail").getSumFieldValue("total",
                Condition.equalCondition("orderId", recordRow.get("id")));

        setFieldValue(totalAmount, sumFieldValue.getValue(), recordRow.get("id"));*/
    }

    /**
     * 发货
     *
     * @param id             订单id
     * @param expressNo      快递单号
     * @param expressCompany 快递公司
     * @param senderName     寄件人
     * @param senderPhone    寄件电话
     * @param addressDetail  寄件地址
     */
    public void senderExpress(Integer id,
                              String expressNo,
                              String expressCompany,
                              String senderName,
                              String senderPhone,
                              String addressDetail) {
        RecordRow recordRow = new RecordRow();
        recordRow.put(this.getPrimaryKeyName(), id);
        recordRow.put(this.expressNo, expressNo);
        recordRow.put(this.expressCompany, expressCompany);
        recordRow.put(this.state, OrderStateEnum.receive.toString());
        recordRow.put(this.senderName, senderName);
        recordRow.put(this.senderPhone, senderPhone);
        recordRow.put(this.sendAddressDetail, addressDetail);
        recordRow.put(this.sendDate, DateTimeUtils.getCurrentDate());
        recordRow.put(this.isSend, true);
        update(recordRow);
    }

    /**
     * 确认收货
     *
     * @param id 订单id
     */
    public void orderReceive(Integer id) {
        RecordRow recordRow = new RecordRow();
        recordRow.put(this.getPrimaryKeyName(), id);
        recordRow.put(this.state, OrderStateEnum.comment.toString());
        recordRow.put(this.receiveDate, DateTimeUtils.getCurrentDate());
        recordRow.put(this.isReceive, true);
        update(recordRow);
    }

    /**
     * 提交评价
     *
     * @param id 订单id
     */
    public void orderComment(Integer id) {
        RecordRow recordRow = new RecordRow();
        recordRow.put(this.getPrimaryKeyName(), id);
        recordRow.put(this.state, OrderStateEnum.finish.toString());
        recordRow.put(this.commentDate, DateTimeUtils.getCurrentDate());
        recordRow.put(this.isComment, true);
        update(recordRow);
    }

    /**
     * 退货申请
     *
     * @param id
     * @param backExpressNo
     * @param backExpressCompany
     * @param backReason
     */
    public void orderBack(Integer id, String backExpressNo, String backExpressCompany, String backReason) {
        RecordRow recordRow = new RecordRow();
        recordRow.put(this.getPrimaryKeyName(), id);
        recordRow.put(this.state, OrderStateEnum.cancel);
        recordRow.put(this.backDate, DateTimeUtils.getCurrentDate());
        recordRow.put(this.backExpressNo, backExpressNo);
        recordRow.put(this.backExpressCompany, backExpressCompany);
        recordRow.put(this.backReason, backReason);
        update(recordRow);
    }

    /**
     * 退单确认 先找顾客沟通，确认原因后 全额退款 后续改进为可输入退款金额
     *
     * @param id 订单id
     */
    public void orderBackSure(Integer id) {
        RecordRow recordRow = new RecordRow();
        recordRow.put(this.getPrimaryKeyName(), id);
        recordRow.put(this.state, OrderStateEnum.finish);
        recordRow.put(this.cancelDate, DateTimeUtils.getCurrentDate());
        recordRow.put(this.isCancel, true);
        update(recordRow);
    }
}
