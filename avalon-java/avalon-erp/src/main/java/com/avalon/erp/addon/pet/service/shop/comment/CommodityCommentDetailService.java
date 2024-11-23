/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.shop.comment;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommodityCommentDetailService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.commodity.comment.detail";
    }

    @Override
    public String getLabel() {
        return "商品评论详情"; // 图片
    }

    protected final Field url = Fields.createImage("图片");
    protected final Field urlThumb = Fields.createImage("缩略图");
    protected final Field sequence = Fields.createInteger("顺序");
    protected final Field commodityCommentId = Fields.createMany2one("商品评论",
            "pet.commodity.comment");
}
