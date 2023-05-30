package drillox.autotrader.bot;


import com.jfx.*;
import org.ta4j.core.num.Num;

import javax.swing.*;
import java.net.MalformedURLException;
import java.util.EventListener;
import java.util.Timer;

@Drillox(Description = "Just main")
public class Main {
    private static Timer timer = new Timer(true);

    public static void main(String [] args) throws ErrInvalidFunctionParamvalue, ErrTradeContextBusy, ErrNoConnection, ErrTradeDisabled, ErrCustomIndicatorError, ErrStringParameterExpected, ErrInvalidAccount, ErrTradeTooManyOrders, ErrLongsNotAllowed, ErrInvalidStops, ErrInvalidTradeVolume, ErrInvalidPrice, ErrPriceChanged, ErrOffQuotes, ErrTradeTimeout2, ErrTradeExpirationDenied, ErrIntegerParameterExpected, ErrShortsNotAllowed, ErrTradeTimeout3, ErrTradeTimeout4, ErrCommonError, ErrMarketClosed, ErrOrderLocked, ErrLongPositionsOnlyAllowed, ErrNotEnoughMoney, ErrAccountDisabled, ErrInvalidPriceParam, ErrInvalidTradeParameters, ErrTradeTimeout, ErrUnknownSymbol, ErrRequote, ErrServerBusy, ErrOldVersion, ErrTradeNotAllowed, ErrTradeModifyDenied, ErrTooManyRequests, ErrTooFrequentRequests, MalformedURLException {
        /*init();
        CandlesChangeListener.NotifyAll(new CandleStick());
        PriceChangeListener.NotifyAll(4.67);
        PriceChangeListener.NotifyAll(9.0);
        PriceChangeListener.NotifyAll(9.8);
        PriceChangeListener.NotifyAll(9.77);

        try{
            Thread.currentThread().join();
        }catch (Exception e){
            e.printStackTrace();
        }*/
        JFrame window = new JFrame("@Drillox Trader Bot");
        window.setSize(1000,600);
        window.setContentPane(new UIpane());
        window.setVisible(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void init(){
        CandlesChangeListener.register(new Strategy());
        CandlesChangeListener.register(new Strategy.TrendStrategy());
        CandlesChangeListener.register(new Strategy.EMAStrategy());
        PriceChangeListener.register(new Strategy.StatArbitrayStrategy());
        PriceChangeListener.register(new Bot());
        timer.schedule(new CandleCreator(),0,10000);
    }
}
