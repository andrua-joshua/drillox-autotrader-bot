package drillox.autotrader.bot;

import com.jfx.net.JFXServer;

import java.util.Timer;
import java.util.concurrent.CompletableFuture;

@Drillox (Description = "Main program Class where all the execution begins from")
public class MarketView {
    private static Mode tradingMode = Mode.BOT;
    private  static MarketViewBot BotMode= new MarketViewBot("BotModeThread");
    private static Timer timer = new Timer(true);  //task scheduling for the candle creator class

    public static void main(String [] args){
        try {
            //init();
            //PriceChangeListener.NotifyAll(2.9);
            //PriceChangeListener.NotifyAll(2.8089);
            BotMode.start();  //to start the BotMode instance code
            //JFXServer.getInstance();  //to start the EAMode instance code
            //System.out.println("Waiting for connection at "+JFXServer.getInstance().getBindHost()+
               //     " port "+JFXServer.getInstance().getBindPort());
            Thread.currentThread().join();
        }catch (Exception e){
            System.out.println("@Drillox Exceptions handling : "+e);
        }
    }
    @Drillox (Description = "method used for changing the trading mode")
    public static void setTraderMode(Mode mode){
        tradingMode = mode;
    }
    @Drillox (Description = "to get the trading mode")
    public static Mode getTradingMode(){return tradingMode;}

    @Drillox(Description = "Method for initializations")
    public static void init(){
        CandlesChangeListener.register(new Strategy());
        CandlesChangeListener.register(new Strategy.TrendStrategy());
        CandlesChangeListener.register(new Strategy.EMAStrategy());
        CandlesChangeListener.register(new DrilloxEMA());
        PriceChangeListener.register(new Strategy.StatArbitrayStrategy());
        PriceChangeListener.register(new Bot());
        timer.schedule(new CandleCreator(),0,10000);
    }
}
