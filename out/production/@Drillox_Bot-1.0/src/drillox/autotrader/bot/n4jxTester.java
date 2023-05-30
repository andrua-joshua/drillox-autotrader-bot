package drillox.autotrader.bot;

import com.jfx.Broker;
import com.jfx.MT4;
import com.jfx.net.JFXServer;
import com.jfx.strategy.Strategy;

public class n4jxTester extends Strategy {

//    private static MT4TerminalConnection terminalConnection = new MT4TerminalConnection("");
    private static Broker broker = new Broker("Drillox","Exness-MT5Trial9", Broker.ProxyType.HTTP,"100216519","Benoxide43");

    public void n4jxTester(){}

    public static void main(String [] args){
        //JFXServer server = new JFXServer("127.0.0.1",7788);
        //new n4jxTester().callMe();
        try{
            JFXServer.getInstance("127.0.0.1",7788);
        }catch (Exception e){
            System.out.println("@Drillox-Exceptions: "+e);
        }
    }
    private void callMe(){
        try {
            //Strategy strategy = new Strategy();
            this.connect("127.0.0.1", 7788, broker, "100216519", "Benoxide", "EURUSD", true);
        }catch (Exception e){
            System.out.println("@Drillox Debugs : "+e);
        }
    }
}
