package drillox.autotrader.bot;


import java.util.ArrayList;

@Drillox(Description = "Class responsible for the creation and running of the strategies")
public class Strategy extends CandlesChangeListener{
    public static CandleStick bufcandleStick; //buffer candlestick for the newly created candlesticks
    public static boolean NofirstCandle = true;


    @Override
    public void perform(CandleStick candleStick) {
        System.out.println("new Candle Close from strategy class: "+candleStick.getClose());
    }

    //All the strategies to be followed by the bot\\
    @Drillox(Description = "The nested TrendStrategy class for for the TrendStrategy logic")
    public static class TrendStrategy extends CandlesChangeListener{
        @Drillox (Description = "private utility members/fields of the strategyClass")
        private Order order = new Order();
        private boolean EnoughCandles;
        private boolean isFirst = true;
        private boolean HH_isGot = false;
        private boolean LL_isGot = false;
        private boolean hl_isGot = false;
        private boolean lh_isGot = false;
        private double ll = 0;
        private double lh = 0;
        private double hh = 0;
        private double hl = 0;
        private int currentSentOrderId;
        private boolean currentOrderIsBuy = true;

        private CandleStick hlCandle, lhCandle;

        @Drillox (Description = "StrategyLogic execution triggerd by the creation of new CandleStick")
        @Override
        public void perform(CandleStick candleStick) {
            if (!CandleStick.getCandles().isEmpty())
                EnoughCandles = CandleStick.getCandles().size()> 20;
            else EnoughCandles = false;
            if (EnoughCandles){ //check for the availability of the candles first
                ArrayList<CandleStick> Bars = CandleStick.getCandles();

                if (DrilloxEMA.getEMADifference(DrilloxEMA.CurrrentIndex())>0){//for uptrends and buy positions

                    if (isFirst){
                        for (int i = 1; i<Bars.size();i++){
                            if (Bars.get(i).getHigh()<Bars.get(i-1).getHigh()){
                                if (!HH_isGot){
                                    hh = Bars.get(i-1).getHigh();
                                    HH_isGot = true;
                                    hl_isGot = false;
                                }else {
                                    if (Bars.get(i-1).getHigh()>hh){
                                        hh = Bars.get(i-1).getHigh();
                                        hl_isGot = false;
                                    }
                                }
                                //to look for hl
                                for (int _i = i+1; _i<Bars.size();_i++){
                                    if (Bars.get(_i).getLow()>Bars.get(_i-1).getLow()){
                                        if (!hl_isGot){
                                            hl = Bars.get(_i-1).getLow();
                                            hlCandle = Bars.get(_i-1);
                                            hl_isGot = true;
                                        }else {
                                            if (Bars.get(_i-1).getLow()<hl){
                                                hl = Bars.get(_i-1).getLow();
                                                hlCandle = Bars.get(_i-1);
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        isFirst = false;
                    }else {
                        int current = Bars.size()-1;
                        if (Bars.get(current).getHigh()<Bars.get(current-1).getHigh()){
                            if (!HH_isGot){
                                hh= Bars.get(current-1).getHigh();
                                hl_isGot = false;
                                HH_isGot = true;
                            }else {
                                if (Bars.get(current-1).getHigh()>hh){
                                    hh = Bars.get(current-1).getHigh();
                                    hl_isGot = false;
                                    HH_isGot = true;
                                }
                            }
                        }else if (HH_isGot){
                            if (Bars.get(current).getLow()>Bars.get(current-1).getLow()){
                                if (!hl_isGot){
                                    hl = Bars.get(current-1).getLow();
                                    hl_isGot = true;
                                    hlCandle = Bars.get(current-1);
                                }else {
                                    if (Bars.get(current-1).getLow()<hl){
                                        hl = Bars.get(current-1).getLow();
                                        hl_isGot = true;
                                        hlCandle = Bars.get(current-1);
                                    }
                                }
                            }
                        }
                    }
                    //buy order molding here
                    if (HH_isGot && hl_isGot){
                        if (!currentOrderIsBuy)
                            order = new Order();
                        order.setStrategy("TrendStrategy");
                        order.setOrderType(OrderType.LONG);
                        order.setEntryPrice(hh+0.01);
                        order.setSL(hl-0.01);
                        order.setTP((hh+(2*(hh-hl)))-0.01);
                        currentSentOrderId = new Order().sendOrder(order);
                        currentOrderIsBuy = true;
                    }

                }
                else if (DrilloxEMA.getEMADifference(DrilloxEMA.CurrrentIndex())<0){//for downtrends and sell operations

                    if (isFirst){
                        for (int i = 1; i<Bars.size();i++){
                            if (Bars.get(i).getLow()>Bars.get(i-1).getLow()){
                                if (!LL_isGot){
                                    ll = Bars.get(i-1).getLow();
                                    LL_isGot = true;
                                    lh_isGot = false;
                                }else {
                                    if (Bars.get(i-1).getLow()<ll){
                                        ll = Bars.get(i-1).getLow();
                                        lh_isGot = false;
                                    }
                                }
                                //to look for lh
                                for (int _i = i+1; _i<Bars.size();_i++){
                                    if (Bars.get(_i).getHigh()<Bars.get(_i-1).getHigh()){
                                        if (!lh_isGot){
                                            lh = Bars.get(_i-1).getHigh();
                                            lhCandle = Bars.get(_i-1);
                                            lh_isGot = true;
                                        }else {
                                            if (Bars.get(_i-1).getHigh()>lh){
                                                lh = Bars.get(_i-1).getHigh();
                                                lhCandle = Bars.get(_i-1);
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        isFirst = false;
                    }else {
                        int current = Bars.size()-1;
                        if (Bars.get(current).getLow()>Bars.get(current-1).getLow()){
                            if (!LL_isGot){
                                ll = Bars.get(current-1).getLow();
                                lh_isGot = false;
                                LL_isGot = true;
                            }else {
                                if (Bars.get(current-1).getLow()<ll){
                                    ll = Bars.get(current-1).getLow();
                                    lh_isGot = false;
                                    LL_isGot = true;
                                }
                            }
                        }else if (LL_isGot){
                            if (Bars.get(current).getHigh()<Bars.get(current-1).getHigh()){
                                if (!lh_isGot){
                                    lh = Bars.get(current-1).getHigh();
                                    lh_isGot = true;
                                    lhCandle = Bars.get(current-1);
                                }else {
                                    if (Bars.get(current-1).getHigh()>lh){
                                        lh = Bars.get(current-1).getHigh();
                                        lh_isGot = true;
                                        lhCandle = Bars.get(current-1);
                                    }
                                }
                            }
                        }
                    }
                    //sell order molding here
                    if (LL_isGot && lh_isGot){
                        if (currentOrderIsBuy)
                            order = new Order();
                        order.setStrategy("TrendStrategy");
                        order.setOrderType(OrderType.SHORT);
                        order.setEntryPrice(ll-0.01);
                        order.setSL(lh+0.01);
                        order.setTP((ll-(2*(lh-ll)))+0.01);  //2:1 Reward : Risk ratio
                        currentSentOrderId = new Order().sendOrder(order);
                        currentOrderIsBuy = false;
                    }

                }
            }else System.out.println("Waiting for more candles , total Now : "+CandleStick.getCandles().size());

        }

        public void killEntry(double entryPrice,OrderType type){
            switch (type){
                case LONG:
                    hh = 0;
                    hl = 0;
                    PriceActionStrategy.handlePosition(hlCandle,"HL");  //for creating and handling of the order
                    HH_isGot = false;
                    order = null;
                    order = new Order();

                    //creat a new price action demand zone order

                case SHORT:
                    ll = 0;
                    lh = 0;
                    PriceActionStrategy.handlePosition(lhCandle,"LH"); //CandleStick for the supply zone details
                    LL_isGot = false;
                    order = null;
                    order = new Order();

                    //creat a new price action supply zone order

            }
        }
    }

    @Drillox(Description = "The nested PriceActionStrategy class for for the PriceActionStrategy logic")
    public static class PriceActionStrategy{

        @Drillox (Description = "method to handle priceAction strategy order execution called by the trendStrategy class")
        public static void handlePosition(CandleStick candleStick, String qualifier){
            if (qualifier.equals("HL")){  //demand zone
                Order order = new Order(OrderType.LONG,"PriceActionStrategy");
                order.setEntryPrice(Math.max(candleStick.getClose(),candleStick.getOpen()));
                order.setSL(candleStick.getLow());
                order.setTP((order.getEntryPrice()- order.getSL())*2);  //through using the 1:2 risk : reward ratio
                order.sendOrder(order);
            }else if (qualifier.equals("LH")){  //supply zone
                Order order = new Order(OrderType.SHORT,"PriceActionStrategy");
                order.setEntryPrice(Math.min(candleStick.getClose(),candleStick.getOpen()));
                order.setSL(candleStick.getHigh());
                order.setTP(order.getEntryPrice()-(2*(order.getSL()-order.getEntryPrice())));
                order.sendOrder(order);
            }
        }

    }

    @Drillox(Description = "The nested StatArbitrayStrategy class for for the StatArbitrayStrategy logic")
    public static class StatArbitrayStrategy extends PriceChangeListener{
        @Override
        public void perform(double newPrice) {
            //code to execut incase of price change
            System.out.println("new Price from StatArbitray : "+newPrice);
        }
    }

    @Drillox(Description = "The nested EMAStrategy class for for the EMAStrategy logic")
    public static class EMAStrategy extends CandlesChangeListener{
        @Override
        public void perform(CandleStick candleStick) {
            System.out.println("new Candle Close from EMAstrategy class: "+candleStick.getClose());
        }
    }

}
