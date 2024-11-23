/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.shop.commodity;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PageInfo;
import com.avalon.core.model.PageParam;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetCommodityService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.commodity";
    }

    @Override
    public String getLabel() {
        return "宠物商品";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("商品名称");
    }

    public final Field price = Fields.createBigDecimal("价格", 10, 2);
    public final Field stock = Fields.createBigDecimal("库存", 10, 2);
    public final Field firstUrl = Fields.createImage("首图");
    public final Field firstUrlThumb = Fields.createImage("首图缩略图");
    public final Field description = Fields.createString("描述");
    public final Field packaging = Fields.createString("包装");
    public final Field isImport = Fields.createBoolean("进口");
    public final Field ingredient = Fields.createString("成分");
    public final Field efficacy = Fields.createString("功效");
    public final Field weight = Fields.createString("重量");
    public final Field productionPermitNumber = Fields.createString("生产许可证号/进口登记证号");
    public final Field productionDate = Fields.createDate("生产日期");
    public final Field expirationDate = Fields.createInteger("保质期"); // 单位为月
    public final Field brand = Fields.createString("品牌");
    public final Field manufacturer = Fields.createString("生产厂家");
    public final Field originPlace = Fields.createString("原厂地");
    public final Field storageCondition = Fields.createString("存储条件");

    public final Field productCategoryId = Fields.createMany2one("商品类别",
            "pet.commodity.type");

    public final Field details = Fields.createOne2many("商品详情",
            "pet.commodity.detail",
            "commodityId");

    public final Field images = Fields.createOne2many("轮播图",
            "pet.commodity.image",
            "commodityId");

    public final Field isRecommend = Fields.createBoolean("推荐");
    public final Field isHot = Fields.createBoolean("热销");
    public final Field isNew = Fields.createBoolean("新品");
    public final Field isDiscount = Fields.createBoolean("打折");

    public final Field petCategoryTags = Fields.createMany2many("宠物分类标签",
            "pet.big.type");

    public final Field source1688Url = Fields.createString("1688链接");
    public final Field offShelf = Fields.createBoolean("下架");

    private final String[] productFields = new String[]{
            "id", "name", "price", "firstUrl", "isHot", "isNew", "isDiscount",
            "isRecommend",
            "petCategoryTags.id",
            "petCategoryTags.petBigTypeId.name"};

    /**
     * 获取首页商品
     *
     * @param pageNum  几页 从1开始
     * @param pageSize 一页大小
     * @return 记录
     */
    public Record getFirstProduct(Integer pageNum, Integer pageSize) {
        PageInfo idDesc = selectPage(new PageParam(pageNum, pageSize),
                "id desc",
                null,
                productFields);
        return idDesc.getData();
    }

    public Record getProductCategoryPage(
            Integer pageNum,
            Integer pageSize,
            Integer petBigTypeId,
            Integer productCategoryId,
            String order) {
        Condition condition = Condition.inCondition("petCategoryTags.petBigTypeId", petBigTypeId);
        condition = condition.andEqualCondition("productCategoryId", productCategoryId);
        PageInfo pageInfo = selectPage(new PageParam(pageNum, pageSize),
                order,
                condition,
                productFields);
        return pageInfo.getData();
    }
}
