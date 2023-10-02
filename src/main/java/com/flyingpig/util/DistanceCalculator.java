package com.flyingpig.util;

public class DistanceCalculator {
    private static final double R = 6371000; // 地球半径，单位为米

    //计算两个坐标之间的距离，单位为米
    public static double distanceBetweenCoordinates(double lat1, double lon1, double lat2, double lon2) {
        // 将经纬度转换为弧度
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 使用Haversine公式计算大圆距离
        double deltaLon = lon2Rad - lon1Rad;
        double deltaLat = lat2Rad - lat1Rad;
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance;
    }


    public static void main(String[] args) {
        // 示例：计算北京和上海之间的距离
        double lat1 = 39.9042; // 北京的纬度
        double lon1 = 116.4074; // 北京的经度
        double lat2 = 31.2304; // 上海的纬度
        double lon2 = 121.4737; // 上海的经度

        double distance = distanceBetweenCoordinates(lat1, lon1, lat2, lon2);

        System.out.printf("距离：%.2f 英里", distance);
    }
}
