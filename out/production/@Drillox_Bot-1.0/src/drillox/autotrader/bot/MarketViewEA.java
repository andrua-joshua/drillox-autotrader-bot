package drillox.autotrader.bot;

import com.jfx.*;
import com.jfx.strategy.Strategy;
import com.jfx.strategy.StrategyRunner;
import org.ta4j.core.indicators.ATRIndicator;

import java.io.IOException;

public class MarketViewEA extends Strategy {
    /*@Override
    public void init(String symbol, int period, StrategyRunner strategyRunner) throws ErrUnknownSymbol, IOException {
        super.init(symbol,period,strategyRunner);
        System.out.println("@Drillox-Controls: Init method executed");
    }*/

    @Override
    public void coordinate(){
        try {
            double price = marketInfo(getSymbol(), MarketInfo.MODE_ASK);
            //if (MarketView.getTradingMode()==Mode.EA)PriceChangeListener.NotifyAll(price);
            System.out.println("@Drillox-Controls : new Price :"+price);

        } catch (Exception e) {
            System.out.println("@Drillox Exceptions IN {{MarketViewEA}} : "+e);
        }
    }

    /*@Override
    public void deinit(){
        System.out.println("@Drillox-Controls : denit executed");
    }*/
}
