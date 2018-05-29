package cn.com.pax.entity;

/**
 * 停止运动标志位
 *
 * @author luohl
 * @create 2017-12-13-15:21
 */
public class StopMove {
   private static  boolean isRunning = true;

    public static synchronized boolean isRunning() {
        return isRunning;
    }
    public static synchronized void setRunning(boolean running) {
        isRunning = running;
    }
}
    