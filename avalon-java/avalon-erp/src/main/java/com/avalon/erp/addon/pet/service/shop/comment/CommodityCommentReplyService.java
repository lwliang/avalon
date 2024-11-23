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

@Service
@Slf4j
public class CommodityCommentReplyService extends AbstractService {
    @Override
    public String getServiceName() {
        return "pet.commodity.comment.reply";
    }

    @Override
    public String getLabel() {
        return "商品评论回复";
    }

    protected final Field commodityCommentId = Fields.createMany2one("商品评论",
            "pet.commodity.comment");
    protected final Field comment = Fields.createString("回复");
    protected final Field userId = Fields.createMany2one("用户", "pet.user");// 商家回复
    protected final Field userName = Fields.createString("用户名");
}
