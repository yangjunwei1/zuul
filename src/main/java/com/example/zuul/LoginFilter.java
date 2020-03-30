package com.example.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 项目名称：myspringcloud<br>
 * *********************************<br>
 * <P>类名称：LoginFilter</P>
 * *********************************<br>
 * <P>类描述：</P>
 * 创建时间：2020/3/27 14:40<br>
 * 修改备注：<br>
 *
 * @version 1.0<br>
 */
@Component
public class LoginFilter extends ZuulFilter {
    /**
     * pre  路由前
     * routing  路由中
     * post  路由后
     * error   发送错误调用
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    //是否使用此过滤器
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext rc=RequestContext.getCurrentContext();
        HttpServletRequest request = rc.getRequest();
        String token = request.getParameter("token");
        if(token == null){
            rc.setSendZuulResponse(false);
            rc.setResponseStatusCode(401);
            try {
                rc.getResponse().setContentType("text/html;charset=utf-8");
                rc.getResponse().getWriter().write("请先登录");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}