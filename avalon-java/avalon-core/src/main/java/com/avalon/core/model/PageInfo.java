/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.util.ObjectUtils;
import lombok.Data;

@Data
public class PageInfo {
    private Integer total;//总个数
    private Integer pageCur;//当前页数
    private Integer pageSize;//每页大小
    private Integer pageCount;//页面个数
    private Boolean nextPage;//下一页
    private Boolean prePage;//上一页
    private Record data;

    public PageInfo(Record data, Integer total, Integer pageCur, Integer pageSize) {
        this.data = data;
        this.pageCur = pageCur;
        this.pageSize = pageSize;
        this.total = total;
        this.refresh();
    }

    private void refresh() {
        this.pageCount = new Double(Math.ceil(this.total * 1.0 / this.pageSize)).intValue();
        if (ObjectUtils.isNotNull(data)) {
            int left = total - ((pageCur - 1) * pageSize + data.size());//剩余个数
            this.setNextPage(false);
            if (data.size() == pageSize && left > 0) {
                this.setNextPage(true);
            }
        } else {
            this.setNextPage(false);
            this.setPrePage(false);
            this.setData(new Record());
        }
        this.prePage = !this.pageCur.equals(1);
    }
}
