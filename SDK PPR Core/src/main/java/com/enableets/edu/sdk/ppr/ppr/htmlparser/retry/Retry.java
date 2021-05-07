package com.enableets.edu.sdk.ppr.ppr.htmlparser.retry;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/23
 **/
public abstract class Retry {

    private int retryTime = 3;

    private Long sleepTime = 100L;

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    public void setSleepTime(Long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public abstract Boolean doBiz();

    public Boolean execute() throws InterruptedException{
        for (int i = 0; i < retryTime; i++) {
            try{
                return doBiz();
            } catch (Exception e){
                System.out.println(e.getMessage() + "Retry " + (i+1));
                Thread.sleep(100L);
            }
        }
        return Boolean.FALSE;
    }
}
