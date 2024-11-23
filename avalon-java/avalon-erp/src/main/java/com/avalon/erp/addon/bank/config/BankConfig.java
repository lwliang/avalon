/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:conf.properties")
@Getter
public class BankConfig {
    @Value("${bridge_url_jsp}")
    private String bridge_url_jsp;
    @Value("${Bridge_URL_ConfirmTrace}")
    private String Bridge_URL_ConfirmTrace;
    @Value("${Bridge_URL_RefundTrace}")
    private String Bridge_URL_RefundTrace;
    @Value("${Bridge_URL_DownloadTrace}")
    private String Bridge_URL_DownloadTrace;
    @Value("${Bridge_URL_Contract}")
    private String Bridge_URL_Contract;
    @Value("${Bridge_URL_AgentPay}")
    private String Bridge_URL_AgentPay;
    @Value("${Bridge_URL_AgentPayQuery}")
    private String Bridge_URL_AgentPayQuery;


    //交易码
    @Value("${Bridge_TransCode_ConfirmTrace}")
    private String Bridge_TransCode_ConfirmTrace;
    @Value("${Bridge_TransCode_RefundTrace}")
    private String Bridge_TransCode_RefundTrace;
    @Value("${Bridge_TransCode_DownloadTrace}")
    private String Bridge_TransCode_DownloadTrace;
    @Value("${Bridge_TransCode_AgentSignReq}")
    private String Bridge_TransCode_AgentSignReq;
    @Value("${Bridge_TransCode_AgentSignSubmit}")
    private String Bridge_TransCode_AgentSignSubmit;
    @Value("${Bridge_TransCode_AgentSignResend}")
    private String Bridge_TransCode_AgentSignResend;
    @Value("${Bridge_TransCode_AgentUnSign}")
    private String Bridge_TransCode_AgentUnSign;
    @Value("${Bridge_TransCode_AgentSignQuery}")
    private String Bridge_TransCode_AgentSignQuery;
    @Value("${Bridge_TransCode_AgentPay}")
    private String Bridge_TransCode_AgentPay;
    @Value("${Bridge_TransCode_AgentPayQuery}")
    private String Bridge_TransCode_AgentPayQuery;
    @Value("${Bridge_TransCode_AgentQueryByCodEpay}")
    private String Bridge_TransCode_AgentQueryByCodEpay;
}
