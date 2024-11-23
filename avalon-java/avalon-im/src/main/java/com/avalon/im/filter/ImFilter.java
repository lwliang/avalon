/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.filter;

import com.avalon.core.context.Context;
import com.avalon.core.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


@WebFilter(filterName = "imFilter", urlPatterns = "/*")
public class ImFilter implements Filter {
    private final Context context;

    public ImFilter(Context context) {
        this.context = context;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        String database = context.getApplicationConfig().getDataSource().getDatabase();
        if (StringUtils.isNotEmpty(database)) {
            context.init(database);
        } else {
            context.init(context.getDefaultDatabase());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}