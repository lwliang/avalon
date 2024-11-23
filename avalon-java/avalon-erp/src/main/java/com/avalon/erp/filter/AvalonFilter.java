/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.filter;

import com.avalon.core.context.Context;
import com.avalon.core.util.HttpRequestUtils;
import com.avalon.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "avalonFilter", urlPatterns = "/*")
public class AvalonFilter implements Filter {
    private final Context context;

    public AvalonFilter(Context context) {
        this.context = context;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String db = HttpRequestUtils.getRequestParam("db", (HttpServletRequest) servletRequest);
        if (StringUtils.isEmpty(db)) {
            db = context.getApplicationConfig().getDataSource().getDatabase();
        }
        if (StringUtils.isNotEmpty(db)) {
            context.init(db);
        } else {
            context.init(context.getDefaultDatabase());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
