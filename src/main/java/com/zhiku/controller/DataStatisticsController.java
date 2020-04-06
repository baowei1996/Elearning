package com.zhiku.controller;

import com.zhiku.service.DataStatisticsService;
import com.zhiku.util.ResponseData;
import com.zhiku.util.SmallTools;
import com.zhiku.util.monitor.VisitStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;


@CrossOrigin(value = "*")
@Controller
@RequestMapping("dataStatistics")
public class DataStatisticsController {

    @Autowired
    private DataStatisticsService dataStatisticsService;
    /**
     * 访问信息记录
     * @param response
     * @param lastURI
     * @param nextURI
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping("access")
    public ResponseData access(HttpServletRequest response,String lastURI, String nextURI) throws ParseException {
        String ip= SmallTools.getIp( response );
        System.out.println("访问："+ip+"|"+lastURI+"|"+nextURI);///////////
        VisitStatistics.setDataStatisticsService( dataStatisticsService );
        if(nextURI!=null){
            VisitStatistics.addStart( ip,nextURI,new Date(  ) );
        }
        if(lastURI!=null){
            VisitStatistics.addEnd( ip,lastURI,new Date(  ) );
        }
        return ResponseData.ok();
    }

//    /**
//     * 获取访问统计信息
//     * @param beginDay
//     * @param endDay
//     * @return
//     */
//    @RequestMapping(value = "getFlow",method = RequestMethod.GET)
//    public ResponseData getFlow(Date beginDay,Date endDay){
//        ResponseData responseData=ResponseData.ok();
//        responseData.putDataValue( "accessData",dataStatisticsService.listByDateInterval( beginDay,endDay ) );
//        return responseData;
//    }

}