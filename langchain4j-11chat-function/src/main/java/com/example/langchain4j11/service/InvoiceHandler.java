package com.example.langchain4j11.service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvoiceHandler {

    @Tool("根据用户提交的开票信息进行开票")
    public String handle(@P("公司名称")String companyName,@P("税号")String dutyNumber,@P("开票金额") String amount) throws Exception {
        log.info("companyName=={} dutyNumber=={} amount=={}",companyName,dutyNumber,amount);
        System.out.println(new WeatherService().getWeatherV2("101010100"));
        return "开票成功";
    }
}
