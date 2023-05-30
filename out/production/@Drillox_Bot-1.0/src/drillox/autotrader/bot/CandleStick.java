package drillox.autotrader.bot;

import java.util.ArrayList;

@Drillox(Description = "Class for respresenting the candlesticks with all of there properties")
public class CandleStick{
    private static ArrayList<CandleStick> Bars = new ArrayList<>();
    private double close;
    private double open;
    private double high;
    private double low;
    private double volume;

    @Drillox(Description = "constructor for dynamic candle creation")
    public CandleStick(){
        this.close = -1;
        this.open = -1;
        this.high = -1;
        this.low = -1;
        this.volume = 0;
    }
    @Drillox(Description = "comstructor for static candle creation")
    public CandleStick(double high,double low ,double open, double close, double volume){
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.volume = volume;
    }

    @Drillox(Description = "setter methods for dynamic candle creation")
    public void setClose(double close){this.close = close;}
    @Drillox
    public void setOpen(double open){this.open = open;}
    @Drillox
    public void setHigh(double high){this.high = high;}
    @Drillox
    public void setLow(double low){this.low = low;}
    @Drillox
    public void setVolume(double volume){this.volume = volume;}

    @Drillox (Description = "To get the candles close price")
    public double getClose() {return this.close;}
    @Drillox (Description = "To get the candles high price")
    public double getHigh() {return this.high;}
    @Drillox (Description = "To get the candles low price")
    public double getLow() {return this.low;}
    @Drillox (Description = "To get the Candles open price")
    public double getOpen() {return this.open;}
    @Drillox (Description = "To get the Candles volume")
    public double getVolume() {return this.volume;}

    @Drillox (Description = "Method to get the all Created candles")
    public static ArrayList<CandleStick> getCandles(){
        return Bars;
    }

}
