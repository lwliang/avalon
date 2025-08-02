/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/28
 */

// ECharts 图表类型
export type ChartType = 
  | 'bar' 
  | 'line' 
  | 'pie' 
  | 'scatter'
  | 'radar'
  | 'gauge'
  | 'funnel'
  | 'heatmap'
  | 'tree'
  | 'treemap'
  | 'sunburst'
  | 'graph'
  | 'sankey'
  | 'parallel'
  | 'candlestick'
  | 'effectScatter'
  | 'lines'
  | 'map'
  | 'pictorialBar'
  | 'themeRiver';

// 图表数据接口
export interface ChartData {
  title?: string;
  legend?: string[];
  xAxis?: any[];
  yAxis?: any[];
  series?: any[];
  indicator?: any[];
  options?: any;
  chartType?: ChartType;
}

// 图表配置接口
export interface ChartOptions {
  title?: any;
  tooltip?: any;
  legend?: any;
  grid?: any;
  xAxis?: any;
  yAxis?: any;
  series?: any[];
  radar?: any;
  [key: string]: any;
}

// 组件 Props 接口
export interface ReportEChartProps {
  height?: string;
  serviceName: string;
  action?: string;
  chartType?: ChartType;
  chartOptions?: ChartOptions;
  autoResize?: boolean;
  theme?: string;
}
