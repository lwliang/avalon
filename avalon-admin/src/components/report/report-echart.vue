<script setup lang="ts">
/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/28
 */
import { ref, inject, getCurrentInstance, onMounted, watch } from "vue";
import { invokeMethod } from "../../api/modelApi.ts";
import ServiceInvokeParam from "../../model/ServiceInvokeParam.ts";
import type { ChartType, ChartData, ChartOptions, ReportEChartProps } from "./types";
import { use } from "echarts/core";
import { CanvasRenderer } from "echarts/renderers";
import { 
  BarChart, 
  LineChart, 
  PieChart, 
  ScatterChart,
  RadarChart,
  GaugeChart,
  FunnelChart,
  HeatmapChart,
  TreeChart,
  TreemapChart,
  SunburstChart,
  GraphChart,
  SankeyChart,
  ParallelChart,
  CandlestickChart,
  EffectScatterChart,
  LinesChart,
  MapChart,
  PictorialBarChart,
  ThemeRiverChart
} from "echarts/charts";
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  TransformComponent,
  LegendComponent,
  ToolboxComponent,
  BrushComponent,
  DataZoomComponent,
  VisualMapComponent,
  TimelineComponent,
  CalendarComponent,
  GraphicComponent,
  MarkPointComponent,
  MarkLineComponent,
  MarkAreaComponent
} from "echarts/components";
import VChart from "vue-echarts";

// 注册 ECharts 组件
use([
  CanvasRenderer,
  BarChart,
  LineChart,
  PieChart,
  ScatterChart,
  RadarChart,
  GaugeChart,
  FunnelChart,
  HeatmapChart,
  TreeChart,
  TreemapChart,
  SunburstChart,
  GraphChart,
  SankeyChart,
  ParallelChart,
  CandlestickChart,
  EffectScatterChart,
  LinesChart,
  MapChart,
  PictorialBarChart,
  ThemeRiverChart,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  TransformComponent,
  LegendComponent,
  ToolboxComponent,
  BrushComponent,
  DataZoomComponent,
  VisualMapComponent,
  TimelineComponent,
  CalendarComponent,
  GraphicComponent,
  MarkPointComponent,
  MarkLineComponent,
  MarkAreaComponent
]);

const props = defineProps<ReportEChartProps>();

const registerComponent = inject("registerReportComponent") as (
  id: string,
  instance: any
) => void;
const instance = getCurrentInstance();

const loading = ref(false);
const chartData = ref<ChartData | null>(null);
const chartOptions = ref<ChartOptions>({});

// 默认图表配置
const getDefaultOptions = (type: ChartType, data: ChartData): ChartOptions => {
  const baseOptions = {
    title: {
      text: data.title || '图表标题',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: data.legend || [],
      bottom: 10
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      containLabel: true
    }
  };

  switch (type) {
    case 'bar':
      return {
        ...baseOptions,
        xAxis: {
          type: 'category',
          data: data.xAxis || []
        },
        yAxis: {
          type: 'value'
        },
        series: data.series || []
      };
    case 'line':
      return {
        ...baseOptions,
        xAxis: {
          type: 'category',
          data: data.xAxis || []
        },
        yAxis: {
          type: 'value'
        },
        series: data.series || []
      };
    case 'pie':
      return {
        ...baseOptions,
        series: [{
          type: 'pie',
          radius: '50%',
          data: data.series || [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      };
    case 'scatter':
      return {
        ...baseOptions,
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'value'
        },
        series: data.series || []
      };
    case 'radar':
      return {
        ...baseOptions,
        radar: {
          indicator: data.indicator || []
        },
        series: data.series || []
      };
    case 'gauge':
      return {
        ...baseOptions,
        series: [{
          type: 'gauge',
          detail: { formatter: '{value}' },
          data: data.series || []
        }]
      };
    case 'funnel':
      return {
        ...baseOptions,
        series: [{
          type: 'funnel',
          left: '10%',
          width: '80%',
          data: data.series || []
        }]
      };
    default:
      return {
        ...baseOptions,
        series: data.series || []
      };
  }
};

const loadData = (condition: any) => {
  loading.value = true;
  invokeMethod(props.serviceName, {
    serviceName: props.serviceName,
    method: props.action || 'getReportChart',
    param: condition,
  } as ServiceInvokeParam)
    .then((reportChart: ChartOptions) => {
      chartData.value = reportChart;
      
      // 合并自定义配置和默认配置
      const defaultOptions = getDefaultOptions(
        props.chartType || reportChart.chartType || 'bar', 
        reportChart
      );
      
      chartOptions.value = {
        ...defaultOptions,
        ...props.chartOptions,
        ...reportChart
      };
    })
    .finally(() => {
      loading.value = false;
    });
};

// 监听图表类型变化
watch(() => props.chartType, (newType) => {
  if (chartData.value && newType) {
    chartOptions.value = getDefaultOptions(newType, chartData.value);
  }
});

onMounted(() => {
  if (registerComponent && instance) {
    registerComponent(instance.uid.toString(), { loadData });
  }
});

defineExpose({
  loadData,
  chartOptions,
  chartData
});
</script>

<template>
  <div class="w-full h-full" v-loading="loading">
    <v-chart
      :option="chartOptions"
      :style="{ height: height || '100%', width: '100%' }"
      :autoresize="autoResize !== false"
      :theme="theme"
    />
  </div>
</template>

<style scoped></style>