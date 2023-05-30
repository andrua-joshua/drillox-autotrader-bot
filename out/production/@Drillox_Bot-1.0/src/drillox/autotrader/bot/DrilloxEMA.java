package drillox.autotrader.bot;

import java.util.ArrayList;

@Drillox (Description = "DrilloxEMA class for calculating the EMA")
public class DrilloxEMA extends CandlesChangeListener{
    private final int shortPeriod = 10;
    private final int longPeriod = 20;
    private final double shortMultiplier = 2/(shortPeriod+1);
    private final double longMultiplier = 2/(longPeriod+1);
    public static final ArrayList<Double> EMADiff = new ArrayList<>();
    private static double SMA, shortEMAPrev, longEMAPrev;

    @Override
    public void perform(CandleStick candleStick) {
        if (CandleStick.getCandles().size()>=longPeriod){
            if (EMADiff.isEmpty()){
                shortEMAPrev  =
                        (candleStick.getClose() - (getCloses(shortPeriod)/shortPeriod))*shortMultiplier+(getCloses(shortPeriod)/shortPeriod);
                longEMAPrev =
                        (candleStick.getClose() - (getCloses(longPeriod)/longPeriod))*longMultiplier+(getCloses(longPeriod)/longPeriod);
                double diff = shortEMAPrev - longEMAPrev;
                EMADiff.add(diff);
                System.out.println("<><><><><><><><><><><><><><><><USING SMA><><><><><><><><><><><><><><><><><><>");
            }else {
                shortEMAPrev  =
                        (candleStick.getClose() - shortEMAPrev)*shortMultiplier+shortEMAPrev;
                longEMAPrev =
                        (candleStick.getClose() - longEMAPrev)*longMultiplier+longEMAPrev;
                double diff = shortEMAPrev - longEMAPrev;
                EMADiff.add(diff);System.out.println("<?<?<?<?<?<?<?<?<<?<??<?<?<?<?<?<?<<<<<USING PREVEMA<<<<<<<<<<<<<<<<?<<?<?<?<?<?<<<<<?<?");
            } System.out.println("==================> "+EMADiff.get(EMADiff.size()-1));
        }
    }

    @Drillox (Description = "Utility private method for calculating the some of closes in a given period")
    private double getCloses(int period){
        int startIndex = CandleStick.getCandles().size() - (period);
        double sum = 0.0;
        for (int i = startIndex; i<CandleStick.getCandles().size();i++){
            sum+=CandleStick.getCandles().get(i).getClose();
        }

        return sum;
    }
    @Drillox (Description = "Access method for accessing the EMADifference at differnt points in time")
    public static double getEMADifference(int index){
        return EMADiff.get(index);
    }
    @Drillox (Description = "Access method to get the index of the previous EMADiff")
    public static int CurrrentIndex(){
        return EMADiff.size()-1;
    }
}
