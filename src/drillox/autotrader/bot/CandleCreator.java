package drillox.autotrader.bot;

import java.util.TimerTask;

@Drillox(Description = "Class for dynamically creating the 10s candles")
public class CandleCreator extends TimerTask {
    private final CandleStick candleStick = new CandleStick();
    private double open = -1;
    private double close = -1;
    private double high = -1;
    private double low = -1;

    @Override
    public void run() {
        if (!Strategy.NofirstCandle){
            CandleStick.getCandles().add(Strategy.bufcandleStick);
            CandlesChangeListener.NotifyAll(Strategy.bufcandleStick);
            //System.out.println("Notification sent");
        }
        System.out.println("hello from Drillox candle creator");
        PriceChangeListener.register(new PriceChangeListener() {
            @Override
            public void perform(double newPrice) {
                if (open==-1) {
                    open = newPrice;
                }
                close = newPrice;
                high = getHigh(low,high,close,open);
                if (high == -1)
                    high = newPrice;
                low  = getLow(low,high,close,open);
                if (low == -1)
                    low=newPrice;

                //updating the candlestick properties
                candleStick.setClose(close);
                candleStick.setHigh(high);
                candleStick.setLow(low);
                candleStick.setOpen(open);

                //System.out.println("Price change in-----------------<><><><><><><");
            }

        });

        Strategy.bufcandleStick = candleStick;
        if (Strategy.NofirstCandle) Strategy.NofirstCandle = false;
        //System.out.println("hello from creator --> "+Thread.activeCount());
    }

    private double getHigh(double v1, double v2, double v3, double v4){
        double vv1 = Math.max(v1,v2);
        double vv2 = Math.max(v3,v4);
        return  Math.max(vv1,vv2);
    }

    private double getLow(double v1, double v2, double v3, double v4){
        double vv1 = Math.min(v1,v2);
        double vv2 = Math.min(v3,v4);
        return  Math.min(vv1,vv2);
    }

}
