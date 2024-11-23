/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


import java.io.Serializable;

/**
 * 直连商户平台账单销账返回对象
 *
 * @author DELL
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ChargeBillResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 格式
     */
    @JSONField(name = "format")
    private String format;

    /**
     * 消息体
     */
    @JSONField(name = "message")
    private Message message;


    public ChargeBillResponse() {

    }

    /**
     * 构造函数，通过输入对象，构造返回对象数据信息
     *
     * @param request
     */
    public ChargeBillResponse(ChargeBillRequest request) {
        this.setFormat(request.getFormat());
        this.setMessage(new Message(request.getMessage()));
    }

    @Override
    public String toString() {
        return "ChargeBillResponse[format=" + format + ",message=" + message == null ? "" : message.toString() + "]";
    }

    public String getFormat() {
        return format;
    }

    @JSONField(name = "format")
    public void setFormat(String format) {
        this.format = format;
    }

    public Message getMessage() {
        return message;
    }

    @JSONField(name = "message")
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * 账单查询内部消息对象返回实体message内部类
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public class Message implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 消息头部
         */
        @JSONField(name = "head")
        private Head head;

        /**
         * 消息实体
         */
        @JSONField(name = "info")
        private Info info;

        public Message() {
            this.head = new Head();
            this.info = new Info();
        }

        public Message(ChargeBillRequest.Message requestMessage) {
            this.setHead(new Head(requestMessage.getHead()));
            this.setInfo(new Info(requestMessage.getInfo()));
        }

        @Override
        public String toString() {
            return "ChargeBillResponse.Message[head=" + head.toString() + ",info=" + info.toString() + "]";
        }

        public Head getHead() {
            return head;
        }

        @JSONField(name = "head")
        public void setHead(Head head) {
            this.head = head;
        }

        public Info getInfo() {
            return info;
        }

        @JSONField(name = "info")
        public void setInfo(Info info) {
            this.info = info;
        }

        /**
         * 账单销账内部消息对象返回实体Head内部类
         */
        @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        public class Head implements Serializable {

            private static final long serialVersionUID = 1L;

            /**
             * 渠道
             */
            @JSONField(name = "channel")
            private String channel;

            /**
             * 交易码
             */
            @JSONField(name = "transCode")
            private String transCode;

            /**
             * 交易上行下送标志
             */
            @JSONField(name = "transFlag")
            private String transFlag;

            /**
             * 缴费中心交易序列号
             */
            @JSONField(name = "transSeqNum")
            private String transSeqNum;

            /**
             * 时间戳
             */
            @JSONField(name = "timeStamp")
            private String timeStamp;

            /**
             * 查询返回码
             */
            @JSONField(name = "returnCode")
            private String returnCode;

            /**
             * 返回提示信息
             */
            @JSONField(name = "returnMessage")
            private String returnMessage;

            public Head() {

            }

            public Head(ChargeBillRequest.Message.Head reqMessHead) {
                this.setChannel(reqMessHead.getChannel());
                this.setTransSeqNum(reqMessHead.getTransSeqNum());
                this.setTransCode(reqMessHead.getTransCode());
            }

            @Override
            public String toString() {
                return "ChargeBillResponse.Message.Head[channel=" + channel + ",transCode=" + transCode + ",transSeqNum=" + transSeqNum
                        + ",timeStamp=" + timeStamp + ",returnCode=" + returnCode + ",returnMessage=" + returnMessage + "]";
            }

            public String getChannel() {
                return channel;
            }

            public String getTransCode() {
                return transCode;
            }

            @JSONField(name = "transCode")
            public void setTransCode(String transCode) {
                this.transCode = transCode;
            }

            public String getTransFlag() {
                return transFlag;
            }

            @JSONField(name = "transFlag")
            public void setTransFlag(String transFlag) {
                this.transFlag = transFlag;
            }

            @JSONField(name = "channel")
            public void setChannel(String channel) {
                this.channel = channel;
            }

            public String getTransSeqNum() {
                return transSeqNum;
            }

            @JSONField(name = "transSeqNum")
            public void setTransSeqNum(String transSeqNum) {
                this.transSeqNum = transSeqNum;
            }


            public String getTimeStamp() {
                return timeStamp;
            }

            @JSONField(name = "timestamp")
            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }

            public String getReturnCode() {
                return returnCode;
            }

            @JSONField(name = "returnCode")
            public void setReturnCode(String returnCode) {
                this.returnCode = returnCode;
            }

            public String getReturnMessage() {
                return returnMessage;
            }

            @JSONField(name = "returnMessage")
            public void setReturnMessage(String returnMessage) {
                this.returnMessage = returnMessage;
            }


        }

        /**
         * 账单查询内部消息对象返回实体Info内部类
         */
        @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        public class Info implements Serializable {

            private static final long serialVersionUID = 1L;

            /**
             * 缴费项目唯一标识号
             */
            @JSONField(name = "epayCode")
            private String epayCode;

            /**
             * 缴费中心流水号
             */
            @JSONField(name = "traceNo")
            private String traceNo;

            /**
             * 退款标志位
             */
            @JSONField(name = "refundFlag")
            private String refundFlag;

            /**
             * 第三方支付平台商户订单号 第三方平台例如微信支付宝的支付订单号 add 2020-01-13
             */
            @JSONField(name = "numOpenMerchantOrder")
            private String numOpenMerchantOrder;

            public String getNumOpenMerchantOrder() {
                return numOpenMerchantOrder;
            }

            @JSONField(name = "numOpenMerchantOrder")
            public void setNumOpenMerchantOrder(String numOpenMerchantOrder) {
                this.numOpenMerchantOrder = numOpenMerchantOrder;
            }

            public Info() {

            }

            public Info(ChargeBillRequest.Message.Info reqMessInfo) {
                this.setEpayCode(reqMessInfo.getEpayCode());
                this.setTraceNo(reqMessInfo.getTraceNo());
            }

            @Override
            public String toString() {
                return "ChargeBillResponse.Message.Info[epayCode=" + epayCode + "，traceNo="
                        + traceNo + "，refundFlag=" + refundFlag + "，numOpenMerchantOrder"
                        + numOpenMerchantOrder + "]";
            }

            public String getEpayCode() {
                return epayCode;
            }

            @JSONField(name = "epayCode")
            public void setEpayCode(String epayCode) {
                this.epayCode = epayCode;
            }

            public String getTraceNo() {
                return traceNo;
            }

            @JSONField(name = "traceNo")
            public void setTraceNo(String traceNo) {
                this.traceNo = traceNo;
            }

            public String getRefundFlag() {
                return refundFlag;
            }

            @JSONField(name = "refundFlag")
            public void setRefundFlag(String refundFlag) {
                this.refundFlag = refundFlag;
            }
        }
    }
}