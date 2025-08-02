package com.avalon.core.model.report.echart;

import java.util.LinkedHashMap;

/**
 * 图表配置
 * 比如option = {
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [150, 230, 224, 218, 135, 147, 260],
      type: 'line'
    }
  ]
};
参考网址：https://echarts.apache.org/examples/zh/index.html
 */
public class EchartOption extends LinkedHashMap<String, Object> {
   
}
