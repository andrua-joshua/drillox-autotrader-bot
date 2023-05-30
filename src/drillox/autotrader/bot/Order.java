package drillox.autotrader.bot;

import cloud.metaapi.sdk.clients.meta_api.models.PendingTradeOptions;
import com.jfx.*;

import java.util.Map;
import java.util.TreeMap;

@Drillox(Description = "For managing and representation of the Scheduled orders")
public class Order {
    private int OrderID;
    private double SL;
    private double TP;
    private double EntryPrice;
    private String Strategy;
    private OrderStatus orderStatus;
    private OrderType orderType;
    private final Map<Integer , Order> AllOrders = new TreeMap<>();
    private final Map<Integer, Order> AllRunningOrders = new TreeMap<>();
    private final Map<Integer, Order> AllExecutedOrders = new TreeMap<>();
    private final Map<Integer, Order> AllNonExecutedOrders = new TreeMap<>();

    @Drillox(Description = "Constructor to allow for dynamic order creation")
    public Order(OrderType type,String strategy){
        this.OrderID =  this.hashCode();
        this.SL = -1;
        this.TP = -1;
        this.EntryPrice = -1;
        this.orderType = type;
        this.Strategy = strategy;
        this.orderStatus = OrderStatus.NOT_EXECUTED;
    }

    @Drillox(Description = "Constructor to allow for completely full order creation")
    public Order(double EntryPrice, double TP, double SL, String strategy, OrderType type,OrderStatus status){
        this.OrderID = this.hashCode();
        this.EntryPrice = EntryPrice;
        this.TP = TP;
        this.SL = SL;
        this.Strategy = strategy;
        this.orderType = type;
        this.orderStatus = status;
    }

    @Drillox(Description = "Simple open constructor for api call to cover static limitations")
    public Order(){
        this.OrderID =  this.hashCode();
        this.SL = -1;
        this.TP = -1;
        this.EntryPrice = -1;
        this.orderType = null;
        this.Strategy = null;
        this.orderStatus = OrderStatus.NOT_EXECUTED;
    }

    public int  getOrderID(){
        int al = this.OrderID;
        return al;
    }
    public double getSL(){
        double al = this.SL;
        return al;
    }
    public double getTP(){
        double al = this.TP;
        return al;
    }
    public double getEntryPrice(){
        double al = this.EntryPrice;
        return al;
    }
    public String getStrategy(){
        String al = this.Strategy;
        return al;
    }
    public OrderStatus getOrderStatus(){
        OrderStatus al = this.orderStatus;
        return al;
    }
    public OrderType getOrderType(){
        OrderType al = this.orderType;
        return al;
    }
    public Map<Integer,Order> getAllOrders(){
        Map<Integer,Order> al = this.AllOrders;
        return al;
    }
    public Map<Integer,Order> getAllRunningOrders(){
        return this.AllRunningOrders;
    }
    public Map<Integer,Order> getAllExecutedOrders(){
        return this.AllExecutedOrders;
    }
    public Map<Integer,Order> getAllNonExecutedOrders(){
        return this.AllNonExecutedOrders;
    }

    public void setSL(double SL){this.SL = SL;}
    public void setTP(double TP){this.TP = TP;}
    public void setEntryPrice(double EntryPrice){this.EntryPrice = EntryPrice;}
    public void setStrategy(String strategy){this.Strategy = strategy;}
    public void setOrderStatus(OrderStatus orderStatus){this.orderStatus = orderStatus;}
    public void setOrderType(OrderType type){this.orderType = type;}

    @Drillox(Description = "Utility method for place the order in the orders chain waiting for execution")
    public int sendOrder(Order order) {
        boolean added = false;
        if (order.getEntryPrice()!=-1){
            AllOrders.put(order.getOrderID(),order);
            AllNonExecutedOrders.put(order.getOrderID(),order);
            try {
                switch (order.getOrderType()) {
                    case LONG:
                        //execute the buy order and if order was success, remove order from nonexec to exec
                        switch (MarketView.getTradingMode()) {
                            case BOT:
                                MarketViewBot.connection.createLimitBuyOrder(MarketViewBot.symbol, Bot.lot, order.getEntryPrice(), order.getSL(), order.getTP(),
                                        new PendingTradeOptions());
                            case EA:
                                MarketViewEA ea = new MarketViewEA();
                                ea.orderSend(ea.getSymbol(), TradeOperation.OP_BUYLIMIT, Bot.lot, order.getEntryPrice(), 0, order.getSL(),
                                        order.getTP(), "@Dillox Bot Trade", 1003, null);
                            default:
                                MarketViewBot.connection.createLimitBuyOrder(MarketViewBot.symbol, Bot.lot, order.getEntryPrice(), order.getSL(), order.getTP(),
                                        new PendingTradeOptions());
                        }
                    case SHORT:
                        //execute the sell order and if order was success, remove order from nonexec to exec
                        switch (MarketView.getTradingMode()) {
                            case BOT:
                                MarketViewBot.connection.createLimitSellOrder(MarketViewBot.symbol, Bot.lot, order.getEntryPrice(), order.getSL(), order.getTP(),
                                        new PendingTradeOptions());
                            case EA:
                                MarketViewEA ea = new MarketViewEA();
                                ea.orderSend(ea.getSymbol(), TradeOperation.OP_SELLLIMIT, Bot.lot, order.getEntryPrice(), 0, order.getSL(), order.getTP(),
                                        "@DRillox Bot Trades", 24, null);
                            default:
                                MarketViewBot.connection.createLimitSellOrder(MarketViewBot.symbol, Bot.lot, order.getEntryPrice(), order.getSL(), order.getTP(),
                                        new PendingTradeOptions());
                        }
                }
            } catch (Exception e) {
                System.out.println("@Drillox Exception IN making order{{Order.class}} : "+e);
            }
        }
        return order.getOrderID();
    }
}
