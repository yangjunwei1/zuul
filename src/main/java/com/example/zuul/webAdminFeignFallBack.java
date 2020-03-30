package com.example.zuul;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：myspringcloud<br>
 * *********************************<br>
 * <P>类名称：webAdminFeignFallBack</P>
 * *********************************<br>
 * <P>类描述：</P>
 * 创建时间：2020/3/27 10:26<br>
 * 修改备注：<br>
 *
 * @version 1.0<br>
 */
@Component
public class webAdminFeignFallBack implements FallbackProvider {
    @Override
    public String getRoute() {
        return "web-admin-feign";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        /**
         * 网关向api服务请求失败了，但是客户端向网关发起的请求是成功的
         * 不应该把api的404,500等问题抛给客户端
         * 网关和api服务集群对于客户端来说是黑盒
         */
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("status",200);
                map.put("message","无法连接");
                ObjectMapper om=new ObjectMapper();
                return new ByteArrayInputStream(om.writeValueAsString(map).getBytes("utf-8"));
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders hh=new HttpHeaders();
                hh.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return hh;
            }
        };
    }
}