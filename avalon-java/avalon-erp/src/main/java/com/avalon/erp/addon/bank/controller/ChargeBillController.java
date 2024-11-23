/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.avalon.erp.addon.bank.model.ChargeBillRequest;
import com.avalon.erp.addon.bank.model.ChargeBillResponse;
import com.avalon.erp.addon.bank.sign.SignatureAndVerification;
import com.avalon.erp.addon.bank.utils.DateUtil;
import com.avalon.erp.addon.bank.utils.HttpClientUtils;
import com.avalon.erp.addon.odoo.service.OdooService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 接收账单缴费（销账成功）以及 接收账单缴费（销账失败）
 *
 * @author yzz
 */

@Controller
@RequestMapping("/bank")
@Slf4j
public class ChargeBillController {
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    private final SignatureAndVerification signatureAndVerification;
    private final OdooService odooService;

    public ChargeBillController(SignatureAndVerification signatureAndVerification, OdooService odooService) {
        this.signatureAndVerification = signatureAndVerification;
        this.odooService = odooService;
    }

    /**
     * 接收账单缴费（销账）接口
     *
     * @param queryRequest
     * @return
     */
    @RequestMapping(value = "/getRequest4Sale.do", method = RequestMethod.POST)
    @ResponseBody
    public void getRequest4Sale(String queryRequest,
                                HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String responseJson = null;
        try {
            log.info("--------进入getRequest4Sale----------------------------------");
            // 接收报文
            String requestContent = HttpClientUtils.getRequestBody(httpRequest).trim();
            if (log.isWarnEnabled()) {
                log.info("-----ChargeBillController------------收到的报文：{}", requestContent);
            }
            String signatureString = requestContent.substring(0,
                    requestContent.indexOf("||"));
            log.info("-----ChargeBillController------------截取报文的signatureString:{}", signatureString);
            String requestBody = requestContent.substring(signatureString
                    .length() + 2);
            log.info("-----ChargeBillController------------截取报文的requestBody:{}", requestBody);
            //如果有双引号，则截取双引号内requestBody的内容
            Pattern p = Pattern.compile("\"");
            Matcher m = p.matcher(requestBody);
            while (m.find()) {
                requestBody = requestBody.replace(m.group(), "");
                log.info("-----ChargeBillController------如果有双引号，则截取后的requestBody:{}", requestBody);
            }
            //requestBody是base64加密后的数据，需解析出来
            String request = new String(
                    com.alibaba.fastjson.util.Base64.decodeFast(requestBody));
            log.info("-----ChargeBillController------------解析完成后的requestBody-------{}" + request);
            ChargeBillRequest chargeBillRequest = JSON.parseObject(request,
                    new TypeReference<ChargeBillRequest>() {
                    });
            /** 销账报文重发次数，通过resendTimes此字段识别销账报文是否为重发的，0表示首次、1表示重发一次，2表示重发2次，最多重发3次*/
            if (chargeBillRequest != null && "0".equals(chargeBillRequest.getMessage().getInfo().getResendTimes())) {
                // 验签(验签是解析前的requestBody)
                boolean signVerify = signatureAndVerification.read_cer_and_verify_sign(requestBody,
                        signatureString);
                ChargeBillResponse chargeBillResponse = new ChargeBillResponse(
                        chargeBillRequest);
                ChargeBillResponse.Message respMessage = chargeBillResponse
                        .getMessage();
                ChargeBillResponse.Message.Head respHead = chargeBillResponse
                        .getMessage().getHead();
                ChargeBillResponse.Message.Info respInfo = chargeBillResponse
                        .getMessage().getInfo();
                respHead.setTransFlag("02");
                respHead.setTimeStamp(DateUtil.get(YYYYMMDDHHMMSSSSS));
                // respHead.setChannel("MBNK");
                respHead.setChannel(chargeBillRequest.getMessage().getHead()
                        .getChannel());
                // respHead.setTranCode("chargeBill");
                respHead.setTransCode(chargeBillRequest.getMessage().getHead()
                        .getTransCode());
                respHead.setTransSeqNum(chargeBillRequest.getMessage().getHead()
                        .getTransSeqNum());
                String order_no = chargeBillRequest.getMessage().getInfo().getInput1(); // 获取
                String payBillNo = chargeBillRequest.getMessage().getInfo().getPayBillNo();
                Integer payBillCount = Integer.parseInt(chargeBillRequest.getMessage().getInfo().getPayBillCount());
                HashMap<String, Object> order = null;
                HashMap<String, Object> order_result = new HashMap<>();
                if (payBillCount.equals(1)) {
                    List<Object> domain = List.of("order_no", "=", order_no);
                    List<HashMap<String, Object>> hashMaps = odooService.searchRead("study.school.order",
                            List.of(List.of(domain)), List.of("school_name", "course",
                                    "amount", "student_name", "grade",
                                    "class_name", "student_id_card",
                                    "parent_name", "parent_phone"), null, null);
                    if (!hashMaps.isEmpty()) {
                        order = hashMaps.get(0);
                        respHead.setReturnCode("0000");
                        respHead.setReturnMessage("账单缴费成功");

                        order_result.put("payBillAmt", chargeBillRequest.getMessage().getInfo().getPayBillAmt());
                        order_result.put("state", chargeBillRequest.getMessage().getInfo().getPayBillAmt());
                        odooService.write("study.school.order",
                                Integer.parseInt(order.get("id").toString()),
                                order_result);
                    } else {
                        respHead.setReturnCode("JH02");
                        respHead.setReturnMessage("未查询到账单,请核实您的查询信息");
                    }
                } else {
                    respHead.setReturnCode("JH02");
                    respHead.setReturnMessage("未查询到账单,请核实您的查询信息");
                }
                //测试销账返回报文中，本来是销账成功的报文，但是不要送0000成功码   (JF190510134746710555这个流水号是在Demo的returnCode设置成null的时候产生的，流水状态为6；)

                String epayCode = chargeBillRequest.getMessage().getInfo()
                        .getEpayCode();
                String traceNo = chargeBillRequest.getMessage().getInfo()
                        .getTraceNo();
                String numOpenMerchantOrder = chargeBillRequest.getMessage()
                        .getInfo().getNumOpenMerchantOrder();
                respInfo.setNumOpenMerchantOrder(numOpenMerchantOrder);
                respInfo.setEpayCode(epayCode);
                respInfo.setTraceNo(traceNo);
                /**
                 * 返回码为0000时不读取本字段；
                 * 返回码非0000时，必须返回本标志位信息。返回true标志自动实时退款，返回false标志不做退款
                 */
                if (!"0000".equals(respHead.getReturnCode())) {
                    respInfo.setRefundFlag("true");
                }
                respMessage.setInfo(respInfo);
                respMessage.setHead(respHead);
                chargeBillResponse.setMessage(respMessage);
                responseJson = JSON.toJSONString(chargeBillResponse);
                //加签名
                String signatrue = signatureAndVerification
                        .signWhithsha1withrsa(responseJson);
                log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密前）:" + responseJson);
                responseJson = signatrue + "||"
                        + new String(Base64.encodeBase64(responseJson.getBytes("utf-8")));
                log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密后）:" + responseJson);
                httpResponse.setCharacterEncoding("utf-8");
                httpResponse.setContentType("text/plain");
                httpResponse.getWriter().write(responseJson);
            } else {
                //销账报文重发次数，通过resendTimes此字段识别销账报文是否为重发的，0表示首次、1表示重发一次，2表示重发2次，最多重发3次
                //商户端要注意销账重复通知的情况，要进行订单唯一性处理

                // 验签(验签是解析前的requestBody)
                boolean signVerify = signatureAndVerification.read_cer_and_verify_sign(requestBody,
                        signatureString);
                ChargeBillResponse chargeBillResponse = new ChargeBillResponse(
                        chargeBillRequest);
                ChargeBillResponse.Message respMessage = chargeBillResponse
                        .getMessage();
                ChargeBillResponse.Message.Head respHead = chargeBillResponse
                        .getMessage().getHead();
                ChargeBillResponse.Message.Info respInfo = chargeBillResponse
                        .getMessage().getInfo();
                respHead.setTransFlag("02");
                respHead.setTimeStamp(DateUtil.get(YYYYMMDDHHMMSSSSS));
                // respHead.setChannel("MBNK");
                respHead.setChannel(chargeBillRequest.getMessage().getHead()
                        .getChannel());
                // respHead.setTranCode("chargeBill");
                respHead.setTransCode(chargeBillRequest.getMessage().getHead()
                        .getTransCode());
                respHead.setTransSeqNum(chargeBillRequest.getMessage().getHead()
                        .getTransSeqNum());
                // 销账报文重发次数，通过resendTimes此字段识别销账报文是否为重发的，0表示首次、1表示重发一次，2表示重发2次，最多重发3次
                // 商户端要注意销账重复通知的情况，要进行订单唯一性处理
                if (chargeBillRequest != null
                        && ((Integer.parseInt(chargeBillRequest.getMessage()
                        .getInfo().getResendTimes()))) % 4 == 0) {
                    respHead.setReturnCode("0000");
                    respHead.setReturnMessage("账单缴费成功");
                    /**
                     * 返回码为0000时不读取本字段；
                     * 返回码非0000时，必须返回本标志位信息。返回true标志自动实时退款，返回false标志不做退款
                     */
                    if (!"0000".equals(respHead.getReturnCode())) {
                        respInfo.setRefundFlag("true");
                    }

                } else if (chargeBillRequest != null
                        && ((Integer.parseInt(chargeBillRequest.getMessage()
                        .getInfo().getResendTimes()))) % 4 == 1) {
                    respHead.setReturnCode("1111");
                    respHead.setReturnMessage("账单缴费失敗");
                    /**
                     * 返回码为0000时不读取本字段；
                     * 返回码非0000时，必须返回本标志位信息。返回true标志自动实时退款，返回false标志不做退款
                     */
                    if (!"0000".equals(respHead.getReturnCode())) {
                        respInfo.setRefundFlag("false");
                    }

                } else if (chargeBillRequest != null
                        && ((Integer.parseInt(chargeBillRequest.getMessage()
                        .getInfo().getResendTimes()))) % 4 == 2) {
                    respHead.setReturnCode("1111");
                    respHead.setReturnMessage("账单缴费失敗");
                    /**
                     * 返回码为0000时不读取本字段；
                     * 返回码非0000时，必须返回本标志位信息。返回true标志自动实时退款，返回false标志不做退款
                     */
                    if (!"0000".equals(respHead.getReturnCode())) {
                        respInfo.setRefundFlag("true");
                    }


                } else if (chargeBillRequest != null
                        && ((Integer.parseInt(chargeBillRequest.getMessage()
                        .getInfo().getResendTimes()))) % 4 == 3) {
                    respHead.setReturnCode("JH01");
                    respHead.setReturnMessage("账单缴费失敗");
                    /**
                     * 返回码为0000时不读取本字段；
                     * 返回码非0000时，必须返回本标志位信息。返回true标志自动实时退款，返回false标志不做退款
                     */
                    if (!"0000".equals(respHead.getReturnCode())) {
                        respInfo.setRefundFlag("");
                    }

                }
                String epayCode = chargeBillRequest.getMessage().getInfo()
                        .getEpayCode();
                String traceNo = chargeBillRequest.getMessage().getInfo()
                        .getTraceNo();
                String numOpenMerchantOrder = chargeBillRequest.getMessage()
                        .getInfo().getNumOpenMerchantOrder();
                respInfo.setNumOpenMerchantOrder(numOpenMerchantOrder);
                respInfo.setEpayCode(epayCode);
                respInfo.setTraceNo(traceNo);

                respMessage.setInfo(respInfo);
                respMessage.setHead(respHead);
                chargeBillResponse.setMessage(respMessage);
                responseJson = JSON.toJSONString(chargeBillResponse);
                //加签名
                String signatrue = signatureAndVerification
                        .signWhithsha1withrsa(responseJson);
                log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密前）:" + responseJson);
                responseJson = signatrue + "||"
                        + new String(Base64.encodeBase64(responseJson.getBytes("utf-8")));
                log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密后）:" + responseJson);
                httpResponse.setCharacterEncoding("utf-8");
                httpResponse.setContentType("text/plain");
                httpResponse.getWriter().write(responseJson);

            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 接收账单缴费（销账失败）接口   实时退款respInfo.setRefundFlag("true");
     *
     * @param queryRequest
     * @return
     */
    @RequestMapping(value = "/getRequest4SaleFail.do", method = RequestMethod.POST)
    @ResponseBody
    public void getRequest4SaleFail(String queryRequest,
                                    HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String responseJson = null;
        try {
            log.info("---------------进入--getRequest4SaleFail------------------------------");
            // 接收报文
            String requestContent = HttpClientUtils.getRequestBody(httpRequest).trim();
            if (log.isWarnEnabled()) {
                log.info("-----ChargeBillController------------收到的报文：{}", requestContent);
            }
            String signatureString = requestContent.substring(0,
                    requestContent.indexOf("||"));
            log.info("-----ChargeBillController------------截取报文的signatureString:", signatureString);
            String requestBody = requestContent.substring(signatureString
                    .length() + 2);
            log.info("-----ChargeBillController------------截取报文的requestBody:", requestBody);
            //requestBody是base64加密后的数据，需解析出来
            String request = new String(
                    com.alibaba.fastjson.util.Base64.decodeFast(requestBody));
            log.info("-----ChargeBillController------------解析完成后的requestBody-------" + request);
            ChargeBillRequest chargeBillRequest = JSON.parseObject(request,
                    new TypeReference<ChargeBillRequest>() {
                    });
            // 验签
            signatureAndVerification.read_cer_and_verify_sign(requestBody, signatureString);
            ChargeBillResponse chargeBillResponse = new ChargeBillResponse(
                    chargeBillRequest);
            ChargeBillResponse.Message respMessage = chargeBillResponse
                    .getMessage();
            ChargeBillResponse.Message.Head respHead = chargeBillResponse
                    .getMessage().getHead();
            ChargeBillResponse.Message.Info respInfo = chargeBillResponse
                    .getMessage().getInfo();
            respHead.setTransFlag("02");
            respHead.setTimeStamp(DateUtil.get(YYYYMMDDHHMMSSSSS));
            // respHead.setChannel("MBNK");
            respHead.setChannel(chargeBillRequest.getMessage().getHead()
                    .getChannel());
            // respHead.setTranCode("chargeBill");
            respHead.setTransCode(chargeBillRequest.getMessage().getHead()
                    .getTransCode());
            respHead.setTransSeqNum(chargeBillRequest.getMessage().getHead()
                    .getTransSeqNum());
            respHead.setReturnCode("1111");
            respHead.setReturnMessage("账单缴费失败");

            String epayCode = chargeBillRequest.getMessage().getInfo()
                    .getEpayCode();
            String traceNo = chargeBillRequest.getMessage().getInfo()
                    .getTraceNo();
            String numOpenMerchantOrder = chargeBillRequest.getMessage()
                    .getInfo().getNumOpenMerchantOrder();
            respInfo.setNumOpenMerchantOrder(numOpenMerchantOrder);
            respInfo.setEpayCode(epayCode);
            respInfo.setTraceNo(traceNo);
            /**
             * 返回码为0000时不读取本字段；
             * 返回码非0000时，必须返回本标志位信息。返回true标志自动实时退款，返回false标志不做退款
             */
            if (!"0000".equals(respHead.getReturnCode())) {
                respInfo.setRefundFlag("true");
            }
            respMessage.setInfo(respInfo);
            respMessage.setHead(respHead);
            chargeBillResponse.setMessage(respMessage);
            responseJson = JSON.toJSONString(chargeBillResponse);
            //加签名
            String signatrue = signatureAndVerification
                    .signWhithsha1withrsa(responseJson);
            log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密前）:" + responseJson);
            responseJson = signatrue + "||"
                    + new String(Base64.encodeBase64(responseJson.getBytes("utf-8")));
            log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密后）:" + responseJson);
            httpResponse.setCharacterEncoding("utf-8");
            httpResponse.setContentType("text/plain");
            httpResponse.getWriter().write(responseJson);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 接收账单缴费（销账失败）接口   不做退款respInfo.setRefundFlag("false");
     *
     * @param queryRequest
     * @return
     */
    @RequestMapping(value = "/getRequest4SaleFailFalse.do", method = RequestMethod.POST)
    @ResponseBody
    public void getRequest4SaleFailFalse(String queryRequest,
                                         HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String responseJson = null;
        try {
            log.info("---------------进入--getRequest4SaleFail------------------------------");
            // 接收报文
            String requestContent = HttpClientUtils.getRequestBody(httpRequest).trim();
            if (log.isWarnEnabled()) {
                log.info("-----ChargeBillController------------收到的报文：{}", requestContent);
            }
            String signatureString = requestContent.substring(0,
                    requestContent.indexOf("||"));
            log.info("-----ChargeBillController------------截取报文的signatureString:", signatureString);
            String requestBody = requestContent.substring(signatureString
                    .length() + 2);
            log.info("-----ChargeBillController------------截取报文的requestBody:", requestBody);
            //requestBody是base64加密后的数据，需解析出来
            String request = new String(
                    com.alibaba.fastjson.util.Base64.decodeFast(requestBody));
            log.info("-----ChargeBillController------------解析完成后的requestBody-------" + request);
            ChargeBillRequest chargeBillRequest = JSON.parseObject(request,
                    new TypeReference<ChargeBillRequest>() {
                    });
            // 验签
            signatureAndVerification.read_cer_and_verify_sign(requestBody, signatureString);
            ChargeBillResponse chargeBillResponse = new ChargeBillResponse(
                    chargeBillRequest);
            ChargeBillResponse.Message respMessage = chargeBillResponse
                    .getMessage();
            ChargeBillResponse.Message.Head respHead = chargeBillResponse
                    .getMessage().getHead();
            ChargeBillResponse.Message.Info respInfo = chargeBillResponse
                    .getMessage().getInfo();
            respHead.setTransFlag("02");
            respHead.setTimeStamp(DateUtil.get(YYYYMMDDHHMMSSSSS));
            // respHead.setChannel("MBNK");
            respHead.setChannel(chargeBillRequest.getMessage().getHead()
                    .getChannel());
            // respHead.setTranCode("chargeBill");
            respHead.setTransCode(chargeBillRequest.getMessage().getHead()
                    .getTransCode());
            respHead.setTransSeqNum(chargeBillRequest.getMessage().getHead()
                    .getTransSeqNum());
            String numOpenMerchantOrder = chargeBillRequest.getMessage()
                    .getInfo().getNumOpenMerchantOrder();
            respInfo.setNumOpenMerchantOrder(numOpenMerchantOrder);
            respHead.setReturnCode("1111");
            respHead.setReturnMessage("账单缴费失败");

            String epayCode = chargeBillRequest.getMessage().getInfo()
                    .getEpayCode();
            String traceNo = chargeBillRequest.getMessage().getInfo()
                    .getTraceNo();
            respInfo.setEpayCode(epayCode);
            respInfo.setTraceNo(traceNo);
            /**
             * 返回码为0000时不读取本字段；
             * 返回码非0000时，必须返回本标志位信息。返回true标志自动实时退款，返回false标志不做退款
             */
            if (!"0000".equals(respHead.getReturnCode())) {
                respInfo.setRefundFlag("false");
            }
            respMessage.setInfo(respInfo);
            respMessage.setHead(respHead);
            chargeBillResponse.setMessage(respMessage);
            responseJson = JSON.toJSONString(chargeBillResponse);
            //加签名
            String signatrue = signatureAndVerification
                    .signWhithsha1withrsa(responseJson);
            log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密前）:" + responseJson);
            responseJson = signatrue + "||"
                    + new String(Base64.encodeBase64(responseJson.getBytes("utf-8")));
            log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密后）:" + responseJson);
            httpResponse.setCharacterEncoding("utf-8");
            httpResponse.setContentType("text/plain");
            httpResponse.getWriter().write(responseJson);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 接收账单缴费（销账失败）接口  返回空报文
     *
     * @param queryRequest
     * @return
     */
    @RequestMapping(value = "/getRequest4SaleFailNull.do", method = RequestMethod.POST)
    @ResponseBody
    public void getRequest4SaleFailNull(String queryRequest,
                                        HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String responseJson = null;
        try {
            log.info("---------------进入--getRequest4SaleFailNull------------------------------");
            // 接收报文
            String requestContent = HttpClientUtils.getRequestBody(httpRequest).trim();
            if (log.isWarnEnabled()) {
                log.info("-----ChargeBillController------------收到的报文：{}", requestContent);
            }
            String signatureString = requestContent.substring(0,
                    requestContent.indexOf("||"));
            log.info("-----ChargeBillController------------截取报文的signatureString:", signatureString);
            String requestBody = requestContent.substring(signatureString
                    .length() + 2);
            log.info("-----ChargeBillController------------截取报文的requestBody:", requestBody);
            //requestBody是base64加密后的数据，需解析出来
            String request = new String(
                    com.alibaba.fastjson.util.Base64.decodeFast(requestBody));
            log.info("-----ChargeBillController------------解析完成后的requestBody-------" + request);
            ChargeBillRequest chargeBillRequest = JSON.parseObject(request,
                    new TypeReference<ChargeBillRequest>() {
                    });
            // 验签
            signatureAndVerification.read_cer_and_verify_sign(requestBody, signatureString);
            ChargeBillResponse chargeBillResponse = new ChargeBillResponse(
                    chargeBillRequest);
            ChargeBillResponse.Message respMessage = chargeBillResponse
                    .getMessage();
            ChargeBillResponse.Message.Head respHead = chargeBillResponse
                    .getMessage().getHead();
            ChargeBillResponse.Message.Info respInfo = chargeBillResponse
                    .getMessage().getInfo();
            respHead.setTransFlag("02");
            respHead.setTimeStamp(DateUtil.get(YYYYMMDDHHMMSSSSS));
            // respHead.setChannel("MBNK");
            respHead.setChannel(chargeBillRequest.getMessage().getHead()
                    .getChannel());
            // respHead.setTranCode("chargeBill");
            respHead.setTransCode(chargeBillRequest.getMessage().getHead()
                    .getTransCode());
            respHead.setTransSeqNum(chargeBillRequest.getMessage().getHead()
                    .getTransSeqNum());
            respHead.setReturnCode("1111");
            respHead.setReturnMessage("账单缴费失败");

            String epayCode = chargeBillRequest.getMessage().getInfo()
                    .getEpayCode();
            String traceNo = chargeBillRequest.getMessage().getInfo()
                    .getTraceNo();
            String numOpenMerchantOrder = chargeBillRequest.getMessage()
                    .getInfo().getNumOpenMerchantOrder();
            respInfo.setNumOpenMerchantOrder(numOpenMerchantOrder);
            respInfo.setEpayCode(epayCode);
            respInfo.setTraceNo(traceNo);
            /**
             * 返回码为0000时不读取本字段；
             * 返回码非0000时，必须返回本标志位信息。返回true标志自动实时退款，返回false标志不做退款
             */
            if (!"0000".equals(respHead.getReturnCode())) {
                respInfo.setRefundFlag("false");
            }
            respMessage.setInfo(respInfo);
            respMessage.setHead(respHead);
            chargeBillResponse.setMessage(respMessage);
            responseJson = JSON.toJSONString(chargeBillResponse);
            //加签名
            String signatrue = signatureAndVerification
                    .signWhithsha1withrsa(responseJson);
            log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密前）:" + responseJson);
            responseJson = signatrue + "||"
                    + new String(Base64.encodeBase64(responseJson.getBytes("utf-8")));
            log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密后）:" + responseJson);
            httpResponse.setCharacterEncoding("utf-8");
            httpResponse.setContentType("text/plain");
            httpResponse.getWriter().write("");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 接收账单缴费（销账失败）接口   实时退款respInfo.setRefundFlag("true");
     *
     * @param queryRequest
     * @return
     */
    @RequestMapping(value = "/getRequestOptionCode.do", method = RequestMethod.POST)
    @ResponseBody
    public void getRequestOptionCode(String queryRequest,
                                     HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String responseJson = null;
        try {
            log.info("---------------进入--getRequest4SaleFail------------------------------");
            // 接收报文
            String requestContent = HttpClientUtils.getRequestBody(httpRequest).trim();
            if (log.isWarnEnabled()) {
                log.info("-----ChargeBillController------------收到的报文：{}", requestContent);
            }
            String signatureString = requestContent.substring(0,
                    requestContent.indexOf("||"));
            log.info("-----ChargeBillController------------截取报文的signatureString:", signatureString);
            String requestBody = requestContent.substring(signatureString
                    .length() + 2);
            log.info("-----ChargeBillController------------截取报文的requestBody:", requestBody);
            //requestBody是base64加密后的数据，需解析出来
            String request = new String(
                    com.alibaba.fastjson.util.Base64.decodeFast(requestBody));
            log.info("-----ChargeBillController------------解析完成后的requestBody-------" + request);
            ChargeBillRequest chargeBillRequest = JSON.parseObject(request,
                    new TypeReference<ChargeBillRequest>() {
                    });
            // 验签
            signatureAndVerification.read_cer_and_verify_sign(requestBody, signatureString);
            ChargeBillResponse chargeBillResponse = new ChargeBillResponse(
                    chargeBillRequest);
            ChargeBillResponse.Message respMessage = chargeBillResponse
                    .getMessage();
            ChargeBillResponse.Message.Head respHead = chargeBillResponse
                    .getMessage().getHead();
            ChargeBillResponse.Message.Info respInfo = chargeBillResponse
                    .getMessage().getInfo();
            respHead.setTransFlag("02");
            respHead.setTimeStamp(DateUtil.get(YYYYMMDDHHMMSSSSS));
            // respHead.setChannel("MBNK");
            respHead.setChannel(chargeBillRequest.getMessage().getHead()
                    .getChannel());
            // respHead.setTranCode("chargeBill");
            respHead.setTransSeqNum(chargeBillRequest.getMessage().getHead()
                    .getTransSeqNum());
            respHead.setTransCode(chargeBillRequest.getMessage().getHead()
                    .getTransCode());
            respHead.setReturnCode("0000");
            respHead.setReturnMessage("账单缴费成功！");

            String optionCode = chargeBillRequest.getMessage().getInfo().getOptionCode();
            if ("".equals(optionCode) || null == optionCode) {
                respHead.setReturnCode("1111");
                respHead.setReturnMessage("缺少套餐码，账单缴费失败！");

            }

            /**
             * 返回码为0000时不读取本字段；
             * 返回码非0000时，必须返回本标志位信息。返回true标志自动实时退款，返回false标志不做退款
             */
            if (!"0000".equals(respHead.getReturnCode())) {
                respInfo.setRefundFlag("true");
            }
            String epayCode = chargeBillRequest.getMessage().getInfo()
                    .getEpayCode();
            String traceNo = chargeBillRequest.getMessage().getInfo()
                    .getTraceNo();
            String numOpenMerchantOrder = chargeBillRequest.getMessage()
                    .getInfo().getNumOpenMerchantOrder();
            respInfo.setNumOpenMerchantOrder(numOpenMerchantOrder);
            respInfo.setEpayCode(epayCode);
            respInfo.setTraceNo(traceNo);
            respMessage.setInfo(respInfo);
            respMessage.setHead(respHead);
            chargeBillResponse.setMessage(respMessage);
            responseJson = JSON.toJSONString(chargeBillResponse);
            //加签名
            String signatrue = signatureAndVerification
                    .signWhithsha1withrsa(responseJson);
            log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密前）:" + responseJson);
            responseJson = signatrue + "||"
                    + new String(Base64.encodeBase64(responseJson.getBytes("utf-8")));
            log.info("-----ChargeBillController------------responseJson打印结果是（responseJson加密后）:" + responseJson);
            httpResponse.setCharacterEncoding("utf-8");
            httpResponse.setContentType("text/plain");
            httpResponse.getWriter().write(responseJson);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

}
