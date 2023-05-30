package drillox.autotrader.bot;

import java.util.ArrayList;

@Drillox (Description = "for switching of the platform trading modes (EA or BOT)")
public abstract class ModeChangeListener {
    private static ArrayList<ModeChangeListener> listeners = new ArrayList<>();

    @Drillox (Description = "Call back method for actions depending on the listener")
    public abstract void perform(Mode newTradingMode);

    @Drillox (Description = "for registering register listeners")
    public static void register(ModeChangeListener listener){
        if (listener!=null)
            listeners.add(listener);
    }

    @Drillox (Description = "method to notify all the registered listeners of the change")
    public static void NotifyAll(Mode newMode){
        for (ModeChangeListener cl:listeners){
            cl.perform(newMode);
        }
    }
}
