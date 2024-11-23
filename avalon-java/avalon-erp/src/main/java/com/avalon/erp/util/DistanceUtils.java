/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.util;

/**
 * 通过经纬度计算距离
 *
 * @since 2021/8/12
 **/
public class DistanceUtils {

    /**
     * 地球半径,单位 km
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 基于余弦定理求两经纬度距离
     * Math.pow(x,y)      //这个函数是求x的y次方
     * Math.toRadians     //将一个角度测量的角度转换成以弧度表示的近似角度
     * Math.sin           //正弦函数
     * Math.cos           //余弦函数
     * Math.sqrt          //求平方根函数
     * Math.asin          //反正弦函数
     */
    public static double getDistanceStr(String lng1, String lat1, String lng2, String lat2) {
        return getDistance(Double.parseDouble(lng1), Double.parseDouble(lat1), Double.parseDouble(lng2), Double.parseDouble(lat2));
    }

    /**
     * 根据经纬度计算两点间的距离
     * 基于余弦定理求两经纬度距离
     *
     * @param longitude1 第一个点的经度
     * @param latitude1  第一个点的纬度
     * @param longitude2 第二个点的经度
     * @param latitude2  第二个点的纬度
     * @return 返回距离 单位千米
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        System.out.println("经纬度距离：longitude1=" + longitude1 + ",latitude1=" + latitude1 + ",longitude2=" + longitude2 + ",latitude2=" + latitude2);
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 千米
        s = s * EARTH_RADIUS;
        return s;
    }

    /**
     * 根据经纬度，计算两点间的距离
     * 由于三角函数中特定的关联关系，Haversine公式的最终实现方式可以有多种，比如借助转角度的函数atan2：
     *
     * @param longitude1 第一个点的经度
     * @param latitude1  第一个点的纬度
     * @param longitude2 第二个点的经度
     * @param latitude2  第二个点的纬度
     * @return double
     */
    public static double getDistance2(double longitude1, double latitude1, double longitude2, double latitude2) {

        double latDistance = Math.toRadians(longitude1 - longitude2);
        double lngDistance = Math.toRadians(latitude1 - latitude2);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(longitude1)) * Math.cos(Math.toRadians(longitude2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c * EARTH_RADIUS;
    }
}
