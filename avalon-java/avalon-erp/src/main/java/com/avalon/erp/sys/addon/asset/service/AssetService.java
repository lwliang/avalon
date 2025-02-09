package com.avalon.erp.sys.addon.asset.service;


import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/1/22
 */

@Service
@Slf4j
public class AssetService extends AbstractService {
    @Override
    public String getServiceName() {
        return "asset.asset";
    }

    @Override
    public String getLabel() {
        return "资产主表";
    }

    public Field code = Fields.createString("资产码", true);
    public Field categoryId = Fields.createMany2one("资产分类", "asset.category");
    public Field purchaseDate = Fields.createDate("购买日期");
    public Field purchaseValue = Fields.createBigDecimal("购买金额");
    public Field currentValue = Fields.createBigDecimal("当前金额");
    public Field depreciationRate = Fields.createBigDecimal("折旧率(百分比)");
    public Field depreciationMethod = Fields.createSelection("折旧方法",
            new SelectionHashMap() {{
                put("straight", "直线法");
                put("accelerate", "加速法");
            }},
            "straight");
    public Field depreciationStartDate = Fields.createDate("折旧开始日期");
    public Field depreciationEndDate = Fields.createDate("折旧结束日期");
    public Field state = Fields.createSelection("状态",
            new SelectionHashMap() {{
                put("draft", "草稿");
                put("using", "使用中");
                put("discard", "报废");
            }},
            "draft");
    public Field responsibleUserId = Fields.createMany2one("资产负责人", "hr.staff");
    public Field locationId = Fields.createMany2one("资产位置", "stock.location");
    public Field remark = Fields.createText("备注");
}
