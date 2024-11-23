/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.avalon.erp.addon.bank.config.BankConfig;
import com.avalon.erp.addon.bank.model.QueryBillRequest;
import com.avalon.erp.addon.bank.model.QueryBillResponse;
import com.avalon.erp.addon.bank.sign.SignatureAndVerification;
import com.avalon.erp.addon.bank.utils.DateUtil;
import com.avalon.erp.addon.odoo.service.OdooService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 农行缴费中心发交易给直连商户
 * 1、 下载农行公钥证书 TrustPay.cer 在哪里下载，是不是统一的
 * 直连商户发起交易到农行缴费中心
 * 1、商户证书（*.pfx 证书中的私钥） 让甲方注册，并协助下载
 */

@Controller
@Slf4j
@RequestMapping("/bank")
public class QueryBillController {
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    private final SignatureAndVerification signatureAndVerification;

    private final OdooService odooService;

    private BankConfig bankConfig;

    public QueryBillController(SignatureAndVerification signatureAndVerification,
                               OdooService odooService,
                               BankConfig bankConfig) {
        this.signatureAndVerification = signatureAndVerification;
        this.odooService = odooService;
        this.bankConfig = bankConfig;
    }

    /**
     * 账单查询接口(金额规则为0的单账单)
     *
     * @param queryRequest
     * @param request
     * @param httpResponse
     */
    @RequestMapping(value = "/getDirectJoinMerchBill.do", method = RequestMethod.POST)
    @ResponseBody
    public void getBill4DirectJoinMerch(String queryRequest,
                                        HttpServletRequest request, HttpServletResponse httpResponse) {
        log.info("进入QueryBillController账单查询接口--------(金额规则为0的)-------");

        String responseJson = null;
        try {
            //接收报文request返回截取并返回requestBody和使用base64解析后的requestBody
            Map<String, String> requestMap = signatureAndVerification.requestBodyOfBase64(request);
            //使用base64解析完成后的requestBody
            String requestBodyOfDecoded = requestMap.get("requestBodyOfDecoded");
            //解析前的requestBody
            String requestBody = requestMap.get("requestBody");
            //获取缴费中心传送过来的签名
            String signatureString = requestMap.get("signatureString");

            // 验签
            boolean flag = signatureAndVerification.read_cer_and_verify_sign(requestBody, signatureString);

            log.info("【QueryBill：getBill4DirectJoinMerch】缴费中心响应的报文验签结果为：{}", flag);

            QueryBillRequest queryBillRequest = JSON.parseObject(requestBodyOfDecoded,
                    new TypeReference<QueryBillRequest>() {
                    });
            //交易编号
            String traceNo = queryBillRequest.getMessage().getInfo()
                    .getTraceNo();
            String input1 = queryBillRequest.getMessage().getInfo()
                    .getInput1(); // 订单号

            List<Object> domain = List.of("order_no", "=", input1);
            List<HashMap<String, Object>> hashMaps = odooService.searchRead("study.school.order",
                    List.of(List.of(domain)), List.of("school_name", "course",
                            "amount", "student_name", "grade",
                            "class_name", "student_id_card",
                            "parent_name", "parent_phone"), null,null);

            //返回给缴费中心的响应
            QueryBillResponse response = new QueryBillResponse(queryBillRequest);
            QueryBillResponse.Message respMessage = response.getMessage();
            QueryBillResponse.Message.Head respHead = response.getMessage()
                    .getHead();

            QueryBillResponse.Message.Info respInfo = response.getMessage()
                    .getInfo();
            //缴费账单子账单
            ArrayList<QueryBillResponse.Message.Info.Bill> respBills = new ArrayList<QueryBillResponse.Message.Info.Bill>();
            ArrayList<QueryBillResponse.Message.Info.Bill.DescDetail> respDescDetail =
                    new ArrayList<QueryBillResponse.Message.Info.Bill.DescDetail>();

            //缴费子商户账单
            ArrayList<QueryBillResponse.Message.Info.Bill.SplitSubMerInfo> splitSubMerInfos = new ArrayList<QueryBillResponse.Message.Info.Bill.SplitSubMerInfo>();

            //封装返回给缴费中心的响应
            String epayCode = queryBillRequest.getMessage().getInfo()
                    .getEpayCode();
            respInfo.setEpayCode(epayCode);
            String merchantId = queryBillRequest.getMessage().getInfo()
                    .getMerchantId();
            respInfo.setMerchantId(merchantId);
            respInfo.setTraceNo(traceNo);
            respInfo.setInput1(queryBillRequest.getMessage().getInfo()
                    .getInput1());
            respInfo.setInput2(queryBillRequest.getMessage().getInfo()
                    .getInput2());
            respInfo.setInput3(queryBillRequest.getMessage().getInfo()
                    .getInput3());
            respInfo.setInput4(queryBillRequest.getMessage().getInfo()
                    .getInput4());
            respInfo.setInput5(queryBillRequest.getMessage().getInfo()
                    .getInput5());

            if (flag && !hashMaps.isEmpty()) {
//	        封装详细账单信息
                HashMap<String, Object> order = hashMaps.get(0); // 订单数据
                HashMap<String, Object> param = new HashMap<>();
                QueryBillRequest.Message.Head head = queryBillRequest.getMessage().getHead();
                param.put("channel", head.getChannel());
                param.put("trans_seq_num", head.getTransSeqNum());
                param.put("branch_code", head.getBranchCode());
                param.put("transaction_timestamp", head.getTimeStamp());
                param.put("epay_code", epayCode);
                param.put("merchant_id", merchantId);
                param.put("trace_no", traceNo);
                param.put("custom_user_id", queryBillRequest.getMessage().getInfo().getUserId());
                odooService.write("study.school.order", Integer.parseInt(order.get("id").toString()),
                        param); // 更新对应银行的账单

                //根据输入停车场标示返回商户号 账单查询接口根据停车上唯一标示返回两条商户ID（103881104410001，103881104990018）
                String parkInput = queryBillRequest.getMessage().getInfo()
                        .getInput1();
                QueryBillResponse.Message.Info.Bill respBill = respInfo.new Bill(); // 一笔帐单
                respBill.setBillNo(order.get("order_no").toString());
                respBill.setBillName(order.get("school_name").toString() + "--" + order.get("course").toString());
                // respBill.setExpireDate("20210731");
                respBills.add(respBill);
                respInfo.setCustName(order.get("parent_name").toString());
                // respInfo.setCustAddress("北京海淀区温泉凯盛家园1区1号楼2单元999室");
                // respInfo.setCacheMem("0,0.00,S,张三丰,4340152");
                respInfo.setRemark("");
                //respInfo.setCallBackText("中国农业银行官网");
                //respInfo.setCallBackUrl("https://abcsr.keepfx.cn/b/ejy/payResult/");
                //使用base64加密信息
                // respInfo.setCallBackUrl("aHR0cDp3d3cuYWJjaGluYS5jb20vY24v");
                //金额规则字段
                String amtRule = "0";
                respInfo.setAmtRule(amtRule);
	            /*QueryBillResponse.Message.Info.Bill.UnitDetail unitDetail = respBill.new UnitDetail(
	                    "unitName", "6.66", "1");*/
                respBill.setOweAmt(order.get("amount").toString());
                respBill.setFeeAmt("0.00");
//                QueryBillResponse.Message.Info.Bill.DescDetail descDtail1 = respBill.new DescDetail(
//                        "缴费月份:", "2020年6月份");
//                QueryBillResponse.Message.Info.Bill.DescDetail descDtail2 = respBill.new DescDetail(
//                        "供电局编号:", "4340152");
//                QueryBillResponse.Message.Info.Bill.DescDetail descDtail3 = respBill.new DescDetail(
//                        "欠费金额:", "0.00元");
//                QueryBillResponse.Message.Info.Bill.DescDetail descDtail4 = respBill.new DescDetail(
//                        "缴费月份:", "2020年6月份");
//                QueryBillResponse.Message.Info.Bill.DescDetail descDtail5 = respBill.new DescDetail(
//                        "服务时间:", "每天0:30-23:30期间均可缴费");
//                QueryBillResponse.Message.Info.Bill.DescDetail descDtail6 = respBill.new DescDetail(
//                        "温馨提示:", "北京电力电费代缴，咨询电话：95598 该用户为：预付费用户");
//                respDescDetail.add(descDtail1);
//                respDescDetail.add(descDtail2);
//                respDescDetail.add(descDtail3);
//                respDescDetail.add(descDtail4);
//                respDescDetail.add(descDtail5);
//                respDescDetail.add(descDtail6);

//                log.info("--------parkInput------------" + parkInput);
//                if ("0018".equals(parkInput)) {
//                    respBill.setRcvMerchantId("103881104990018");
//                } else if ("0002".equals(parkInput)) {
//                    respBill.setRcvMerchantId("103881399990002");
//                } else {
//                    respBill.setRcvMerchantId("103881104410001");
//                }

//	          商户子商户详细信息
//                QueryBillResponse.Message.Info.Bill.SplitSubMerInfo splitSubMerInfo1 = respBill.new SplitSubMerInfo("10388", "0.01");
//                QueryBillResponse.Message.Info.Bill.SplitSubMerInfo splitSubMerInfo2 = respBill.new SplitSubMerInfo("1038819201", "0.02");
//                splitSubMerInfos.add(splitSubMerInfo1);
//                splitSubMerInfos.add(splitSubMerInfo2);

                // respBill.setSplitSubMerInfos(splitSubMerInfos);
                // respBill.setDescDetails(respDescDetail);
                respInfo.setTotalBillCount("1");
                respInfo.setBill(respBills);

                // 有定制电子回单附言信息的，需添加自定义定制附言信息字段
                // respInfo.setMerchantRemark("定制附言信息");

                log.info("----------------------账单查询成功0");
                respHead.setReturnCode("0000");
                respHead.setReturnMessage("账单查询成功，返回成功标志");
            } else {
                if (!flag) {
                    respHead.setReturnCode("JH01");
                    respHead.setReturnMessage("缴费中心传送给商户的请求报文签名验签失败！");
                } else {
                    respHead.setReturnCode("JH02");
                    respHead.setReturnMessage("未查询到账单,请核实您的查询信息！");
                }
            }
            respHead.setTransFlag("02");
            respHead.setTimeStamp(DateUtil.get(YYYYMMDDHHMMSSSSS));

            respMessage.setInfo(respInfo);
            respMessage.setHead(respHead);
            response.setMessage(respMessage);
            responseJson = JSON.toJSONString(response);
            // 加签名
            String signatrue = signatureAndVerification
                    .signWhithsha1withrsa(responseJson);
            log.info("signatrue" + responseJson);
            log.info("responseJson打印结果是（responseJson加密前）:" + responseJson);
            responseJson = signatrue + "||"
                    + new String(Base64.encodeBase64(responseJson.getBytes("utf-8")));
            log.info("responseJson打印结果是（responseJson加密后）:" + responseJson);
            httpResponse.setCharacterEncoding("utf-8");
            httpResponse.setContentType("text/plain");
            httpResponse.getWriter().write(responseJson);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
