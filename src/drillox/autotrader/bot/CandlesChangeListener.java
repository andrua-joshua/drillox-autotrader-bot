package drillox.autotrader.bot;

import java.util.ArrayList;

@Drillox(Description = "Listener for candle stick changes")
public abstract class CandlesChangeListener {
    private static ArrayList<CandlesChangeListener> Listeners = new ArrayList<>();

    @Drillox(Description = "To be overriden by all subclasses of CandlesChangeListener")
    public abstract void perform(CandleStick candleStick);

    @Drillox(Description = "Method to register willing listeners")
    public static boolean register(CandlesChangeListener listener){
        if (listener!=null) {
            Listeners.add(listener);
            return true;
        }
        else return false;
    }

    @Drillox(Description = "Method to notify all registered listeners")
    public static void NotifyAll(CandleStick candleStick){
        for (CandlesChangeListener listener:Listeners){
            listener.perform(candleStick);
        }
    }
}
