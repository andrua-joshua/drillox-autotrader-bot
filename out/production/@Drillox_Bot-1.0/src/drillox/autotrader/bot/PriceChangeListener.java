package drillox.autotrader.bot;

import com.jfx.*;

import java.util.ArrayList;

@Drillox(Description = "Listener for Price changes")
public abstract class PriceChangeListener {
    private static ArrayList<PriceChangeListener> listeners = new ArrayList<>();

    @Drillox(Description = "Method to call after a change in price is noticed")
    public abstract void perform(double newPrice) throws ErrInvalidFunctionParamvalue, ErrTradeContextBusy, ErrNoConnection, ErrTradeDisabled, ErrCustomIndicatorError, ErrStringParameterExpected, ErrInvalidAccount, ErrTradeTooManyOrders, ErrLongsNotAllowed, ErrInvalidStops, ErrInvalidTradeVolume, ErrInvalidPrice, ErrPriceChanged, ErrOffQuotes, ErrTradeTimeout2, ErrTradeExpirationDenied, ErrIntegerParameterExpected, ErrShortsNotAllowed, ErrTradeTimeout3, ErrTradeTimeout4, ErrCommonError, ErrMarketClosed, ErrOrderLocked, ErrLongPositionsOnlyAllowed, ErrNotEnoughMoney, ErrAccountDisabled, ErrInvalidPriceParam, ErrInvalidTradeParameters, ErrTradeTimeout, ErrUnknownSymbol, ErrRequote, ErrServerBusy, ErrOldVersion, ErrTradeNotAllowed, ErrTradeModifyDenied, ErrTooManyRequests, ErrTooFrequentRequests;

    @Drillox(Description = "Method for registering listeners of the price")
    public static void register(PriceChangeListener listener){
        if (listener!=null) {
            listeners.add(listener);
        }
    }

    @Drillox(Description = "Method to notify all registered listeners of the new price")
    public static void NotifyAll(double newPrice) throws ErrInvalidFunctionParamvalue, ErrTradeContextBusy, ErrNoConnection, ErrTradeDisabled, ErrCustomIndicatorError, ErrStringParameterExpected, ErrInvalidAccount, ErrTradeTooManyOrders, ErrLongsNotAllowed, ErrInvalidStops, ErrInvalidTradeVolume, ErrInvalidPrice, ErrPriceChanged, ErrOffQuotes, ErrTradeTimeout2, ErrTradeExpirationDenied, ErrIntegerParameterExpected, ErrShortsNotAllowed, ErrTradeTimeout3, ErrTradeTimeout4, ErrCommonError, ErrMarketClosed, ErrOrderLocked, ErrLongPositionsOnlyAllowed, ErrNotEnoughMoney, ErrAccountDisabled, ErrInvalidPriceParam, ErrInvalidTradeParameters, ErrTradeTimeout, ErrUnknownSymbol, ErrRequote, ErrServerBusy, ErrOldVersion, ErrTradeNotAllowed, ErrTradeModifyDenied, ErrTooManyRequests, ErrTooFrequentRequests {
        for (int i = 0; i<listeners.size();i++){
            listeners.get(i).perform(newPrice);
        }
    }
}
