/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *httpClient操作远程url工具类
 */
public class HttpClientUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    /**
     * 发送str格式的post请求
     * @param url
     * @param jsonStr
     * @return
     */
    public static String doPostStr(String url, String str) {
        // 建立HttpPost对象
        HttpPost httppost = new HttpPost(url);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = httpClientBuilder.build();
//		int statusCode = 0;
        String responseBody = null;
        try {
            // 设置发送内容、编码等
            StringEntity stringEntity = new StringEntity(str, "utf-8");
            stringEntity.setContentType("text/plain");
            httppost.setEntity(stringEntity);
            // 发送Post,并返回一个HttpResponse对象
            HttpResponse response = client.execute(httppost);
//			// 获取返回码
//			statusCode = response.getStatusLine().getStatusCode();
            // 获取返回报文
            responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 下载文件
     * @param url
     * @param jsonStr
     * @param 前台的返回流
     * @return
     */
    public static void doPostFile(String url, String str,HttpServletResponse httpResponse) {
        ServletOutputStream out = null;
        // 建立HttpPost对象
        HttpPost httppost = new HttpPost(url);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = httpClientBuilder.build();
        try {
            // 设置发送内容、编码等
            StringEntity stringEntity = new StringEntity(str, "utf-8");
            stringEntity.setContentType("text/plain");
            httppost.setEntity(stringEntity);
            // 发送Post,并返回一个HttpResponse对象
            HttpResponse response = client.execute(httppost);
            // 获取返回报文
            String responseStr = EntityUtils.toString(response.getEntity());

            //如果系统返回未加密异常则会返回"{"+json+"}"
            if (responseStr.startsWith("{")){
                //设置响应头信息为页面形式
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("text/html;charset=utf-8");
                httpResponse.sendRedirect("downloadTraceFail.jsp");

                //如果返回内容为异常信息则返回内容为"签名||base64加密"形式
            }else if (responseStr.contains("||")) {
                //截取签名信息
                String headSub = responseStr.substring(0, responseStr.indexOf("||"));
                logger.warn("获取签名的前半部分："+headSub);
                //截取加密的json信息，进行解密
                String tailSub = responseStr.substring(responseStr.indexOf("||")+2);
                logger.warn("获取签名的后半部分："+tailSub);
                //将获取到的json报文进行解析，获取报文体信息
                responseStr = Base64Util.decodeData(tailSub);
                logger.warn("获取签名解密后："+responseStr);

                //设置响应头信息为页面形式
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("text/html;charset=utf-8");
                httpResponse.sendRedirect("downloadTraceFail.jsp");

                //返回内容为正常内容，将response消息头读取直接写入httpResponse中
            }else{
                Header[] encode = response.getAllHeaders();
                for (Header header : encode) {
                    httpResponse.setHeader(header.getName(), header.getValue());
                }
            }
            //将获取到的信息封装到httpEntity中，反映到页面中去
            logger.warn("最终获取到的信息："+responseStr);
            HttpEntity entity = new StringEntity(responseStr,"UTF-8");
            //通过输出流将结果反映到页面上
            out = httpResponse.getOutputStream();
            entity.writeTo(out);
            out.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * get 请求方法
     * @param url
     * @return
     */
    public static String doGet(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";

        try {
            //通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            //创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            //设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(35000)//连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)//请求超时时间
                    .setSocketTimeout(60000)//数据读取超时时间
                    .build();
            //为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            //执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            //通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            //通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            if(null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * post请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String doPost(String url, Map<String,Object> paramMap) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";

        try {
            //创建httpClient实例
            httpClient = HttpClients.createDefault();
            //创建httpPost远程连接实例
            HttpPost httpPost = new HttpPost(url);
            //配置请求参数实例
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(35000)//设置连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)//设置连接请求超时时间
                    .setSocketTimeout(60000)//设置读取数据连接超时时间
                    .build();
            //为httpPost实例设置配置
            httpPost.setConfig(requestConfig);

            //封装post请求参数
            if(null != paramMap && paramMap.size() > 0) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                //通过map集成entrySet方法获取entity
                Set<Entry<String, Object>> entrySet = paramMap.entrySet();
                //循环遍历，获取迭代器
                Iterator<Entry<String, Object>> iterator = entrySet.iterator();
                while (iterator.hasNext()) {
                    Entry<String, Object> mapEntry = iterator.next();
                    nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
                }

                //为httpPost设置封装好的请求参数
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            }

            //执行post请求得到返回对象
            response = httpClient.execute(httpPost);
            //通过返回对象获取数据
            HttpEntity entity = response.getEntity();
            //将返回的数据转换为字符串
            result = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            if(null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestBody(HttpServletRequest request)
            throws IOException {
        /** 读取httpbody内容 */
        StringBuilder httpBody = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    request.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                httpBody.append(line);
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return httpBody.toString();
    }
}