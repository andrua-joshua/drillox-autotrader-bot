package drillox.autotrader.bot;

import cloud.metaapi.sdk.clients.meta_api.models.MarketTradeOptions;
import cloud.metaapi.sdk.clients.meta_api.models.PendingTradeOptions;
import com.jfx.*;

import java.util.Set;

@Drillox(Description = "The bot will be responsible for making the trades")
public class Bot extends PriceChangeListener{
    public static double lot = 0.01;
    @Override
    public void perform(double newPrice) throws ErrInvalidFunctionParamvalue, ErrTradeContextBusy, ErrNoConnection, ErrTradeDisabled, ErrCustomIndicatorError, ErrStringParameterExpected, ErrInvalidAccount, ErrTradeTooManyOrders, ErrLongsNotAllowed, ErrInvalidStops, ErrInvalidTradeVolume, ErrInvalidPrice, ErrPriceChanged, ErrOffQuotes, ErrTradeTimeout2, ErrTradeExpirationDenied, ErrIntegerParameterExpected, ErrShortsNotAllowed, ErrTradeTimeout3, ErrTradeTimeout4, ErrCommonError, ErrMarketClosed, ErrOrderLocked, ErrLongPositionsOnlyAllowed, ErrNotEnoughMoney, ErrAccountDisabled, ErrInvalidPriceParam, ErrInvalidTradeParameters, ErrTradeTimeout, ErrUnknownSymbol, ErrRequote, ErrServerBusy, ErrOldVersion, ErrTradeNotAllowed, ErrTradeModifyDenied, ErrTooManyRequests, ErrTooFrequentRequests {

        @Drillox(Description = "To check orders for execution")
        Set<Integer> keys = new Order().getAllNonExecutedOrders().keySet();
        for (int key:keys){
            Order order = new Order().getAllNonExecutedOrders().get(key);
            if (order.getEntryPrice() == newPrice){

                if (order.getStrategy().equals("TrendStrategy")) //to kill the entry from the TrendStrategy class
                    new Strategy.TrendStrategy().killEntry(order.getEntryPrice(),order.getOrderType());

                        order.setOrderStatus(OrderStatus.EXECUTED);
                        new Order().getAllNonExecutedOrders().remove(key);
                        new Order().getAllExecutedOrders().put(key,order);
                        new Order().getAllRunningOrders().put(key,order);
            }
        }

        @Drillox(Description = "To check all the closed orders")
        Set<Integer> Akeys = new Order().getAllRunningOrders().keySet();
        for (int key:Akeys){
            Order order = new Order().getAllRunningOrders().get(key);
            if (newPrice == order.getTP()){
                //actions incase of profits hit
                order.setOrderStatus(OrderStatus.CLOSED);
                new Order().getAllRunningOrders().remove(key);
            }
            if (newPrice == order.getSL()){
                //actions incase of sl hit
                order.setOrderStatus(OrderStatus.CLOSED);
                new Order().getAllRunningOrders().remove(key);
                if (order.getStrategy().equals("PriceActionStrategy")){
                    switch (order.getOrderType()){
                        case LONG:
                            //creat a loss fill order
                            switch (MarketView.getTradingMode()){
                                case BOT:
                                    MarketViewBot.connection.createMarketSellOrder(MarketViewBot.symbol, lot, order.getEntryPrice()+0.01, (newPrice-(order.getTP()/1.5)),
                                            new MarketTradeOptions());
                                case EA:
                                    MarketViewEA ea = new MarketViewEA();
                                    ea.orderSend(ea.getSymbol(),TradeOperation.OP_SELL,lot,newPrice,0,order.getEntryPrice()+0.01,(newPrice-(order.getTP()/1.5)),
                                            "@DRillox Bot Trades",24,null);
                                default:
                                    MarketViewBot.connection.createMarketSellOrder(MarketViewBot.symbol, lot, order.getEntryPrice()+0.01, (newPrice-(order.getTP()/1.5)),
                                            new MarketTradeOptions());
                            }
                        case SHORT:
                            //creat a loss fill order
                            switch (MarketView.getTradingMode()){
                                case BOT:
                                    MarketViewBot.connection.createMarketBuyOrder(MarketViewBot.symbol, lot, order.getEntryPrice()-0.01, (newPrice+(order.getTP()/1.5)),
                                            new MarketTradeOptions());
                                case EA:
                                    MarketViewEA ea = new MarketViewEA();
                                    ea.orderSend(ea.getSymbol(),TradeOperation.OP_BUY,lot,newPrice,0,order.getEntryPrice()-0.01,(newPrice+(order.getTP()/1.5)),
                                            "@DRillox Bot Trades",24,null);
                                default:
                                    MarketViewBot.connection.createMarketBuyOrder(MarketViewBot.symbol, lot, order.getEntryPrice()-0.01, (newPrice+(order.getTP()/1.5)),
                                            new MarketTradeOptions());
                            }
                    }
                }
            }

        }

    }

    @Drillox (Description = "for setting the lot to be used by the bot for order execution")
    public static void setLotSize(double lotSize){
        lot = lotSize;
    }
}
