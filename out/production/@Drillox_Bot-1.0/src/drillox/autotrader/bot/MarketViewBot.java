package drillox.autotrader.bot;

import cloud.metaapi.sdk.clients.meta_api.SynchronizationListener;
import cloud.metaapi.sdk.clients.meta_api.models.*;
import cloud.metaapi.sdk.meta_api.*;
import cloud.metaapi.sdk.meta_api.MetaApi;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

@Drillox(Description = "This the class used for getting and execution of orders thru' the meta api")
public class MarketViewBot extends Thread {
    private static final String token = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiJhYW" +
            "VmY2QwM2RhMDU4ODJkYWJjMTU1YzNlOWFkY2NmMiIsInBlcm1pc3Npb25zIjpbXSwidG9r" +
            "ZW5JZCI6IjIwMjEwMjEzIiwiaWF0IjoxNjYxMTc1MTk1LCJyZWFsVXNlcklkIjoiYWFlZmNk" +
            "MDNkYTA1ODgyZGFiYzE1NWMzZTlhZGNjZjIifQ.MrSQwRXVb9ymDHuNqlH-1ZY0bkCqT_FypRk" +
            "THiBDgJoXezZrdsBrRJ0PGIa-0rg0Rq7MJ2biswlZFN3dW_yFFd_tbsewWLmJ2HvEqzfEnW" +
            "HfmdBaTHkruIQiB9TR08hQ8Pt3Qcnbh5eMPyhrv3RKuJauzuq2uYIjfCvALkJGSJIkqCem3JjwR" +
            "7k5WjGu3n_mOoI9duFB-A1fsa3k5U5ZjhbFRxE1eEbe_IuQ1geyvOZ05c2-ZusAyYm0E5SGdO1i57u" +
            "sq_6IWsuYsoWmdNGSCCAfbW6Ix6gQ2hh6A_T_gJbRvq1eZPSPQVM_dt-To9HpPE865Wd8AXaj_txlYzdo" +
            "1E85EF942lXTB-9c07snPIChc2FY3O6CXwVH8Bn6TE_7FUe7XCAMX7lCyY8_0PWVU5-aYayrurKaTD8DFU8x" +
            "Hwvgi4KwuFBoVlslHB7LG3oqDobWsHvsgrMTR5qE-RVUaz8VZ3aZiT-MsgwypBBHUrZRYjTcfSIIDKDQaxs8" +
            "x06V4OllPQpjciWVBlDdZn24YdjtJqu7J30V771jQFPnC3xlmhpc_-kOW3YYFUlVp6NbybmIbAHAk9Hqk_1dWhV" +
            "PWi556XPvdD_BsFQFHzb1fa5cLsBfOK-LGhtFCRmy8GwLagDrMIxpQH-N_TO3MfpGg3dfXMKtMbur5B5TvFKX-jg";
    public static String symbol = "EURUSD";
    private static String ServerName = "Exness-MT5Trial9";
    private static String Login = "100216519";
    private static String Password = "Benoxide43";

    //public fields for public usage
    public static MetaApi api;
    public static MetatraderAccount account;
    public static MetaApiConnection connection;
    public static TerminalState terminalState;
    private static String profileId = "2b88d071-b8e6-47ef-a1a2-52a150f25500";

    public MarketViewBot(String ThreadName){
        this.setName(ThreadName);
    }

    @Drillox(Description = "start point of the all execution")
    @Override
    public void run() {
        try {
            api = new MetaApi(token);
        } catch (Exception e) {
            System.out.println("@Drillox Exceptions : "+e);
        }
        List<ProvisioningProfile> provisioningProfiles = api.getProvisioningProfileApi()
                .getProvisioningProfiles(null, null).join();
        ProvisioningProfile profile = api.getProvisioningProfileApi().getProvisioningProfile("profileId").join();
        /*
        ProvisioningProfile profile = api.getProvisioningProfileApi().createProvisioningProfile(new NewProvisioningProfileDto(){{
            name = ServerName;
            version = 5;
            brokerTimezone = "EET";
            brokerDSTSwitchTimezone = "EET";
        }}).join();

        profile.uploadFile("servers.dat","/home/drillox/Documents/DrilloxTraderBot-v1.0/src/test/resources/servers.dat");
        */
        account = api.getMetatraderAccountApi().createAccount(new NewMetatraderAccountDto(){{
            name = "Drillox-TraderBot-Account #1";
            type = "cloud";
            server = ServerName;
            login = Login;
            password = Password;
            provisioningProfileId = profile.getId();
            application = "MetaApi";
            magic = 123456;
            quoteStreamingIntervalInSeconds = 0.0;
            reliability = "regular";

        }}).join();

        System.out.println("Deploying the account");
        account.deploy().join();
        connection = account.connect().join();
        connection.waitSynchronized().join();
        terminalState = connection.getTerminalState();

        connection.addSynchronizationListener(new SynchronizationListener() {
            @Override
            public CompletableFuture<Void> onSymbolPriceUpdated(String instanceIndex, MetatraderSymbolPrice price) {
                if (price.symbol.equals(symbol))
                    if (MarketView.getTradingMode()==Mode.BOT) {
                        try {
                            PriceChangeListener.NotifyAll(price.ask);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                return super.onSymbolPriceUpdated(instanceIndex, price);
            }

            @Override
            public CompletableFuture<Void> onConnected(String instanceIndex, int replicas) {
                return super.onConnected(instanceIndex, replicas);
            }

            @Override
            public CompletableFuture<Void> onHealthStatus(String instanceIndex, HealthStatus status) {
                return super.onHealthStatus(instanceIndex, status);
            }

            @Override
            public CompletableFuture<Void> onDisconnected(String instanceIndex) {
                return super.onDisconnected(instanceIndex);
            }

            @Override
            public CompletableFuture<Void> onBrokerConnectionStatusChanged(String instanceIndex, boolean connected) {
                return super.onBrokerConnectionStatusChanged(instanceIndex, connected);
            }

            @Override
            public CompletableFuture<Void> onSymbolPricesUpdated(String instanceIndex, List<MetatraderSymbolPrice> prices, Double equity, Double margin, Double freeMargin, Double marginLevel, Double accountCurrencyExchangeRate) {
                return super.onSymbolPricesUpdated(instanceIndex, prices, equity, margin, freeMargin, marginLevel, accountCurrencyExchangeRate);
            }

            @Override
            public CompletableFuture<Void> onCandlesUpdated(String instanceIndex, List<MetatraderCandle> candles, Double equity, Double margin, Double freeMargin, Double marginLevel, Double accountCurrencyExchangeRate) {
                return super.onCandlesUpdated(instanceIndex, candles, equity, margin, freeMargin, marginLevel, accountCurrencyExchangeRate);
            }

            @Override
            public CompletableFuture<Void> onTicksUpdated(String instanceIndex, List<MetatraderTick> ticks, Double equity, Double margin, Double freeMargin, Double marginLevel, Double accountCurrencyExchangeRate) {
                return super.onTicksUpdated(instanceIndex, ticks, equity, margin, freeMargin, marginLevel, accountCurrencyExchangeRate);
            }

        });

    }

    @Drillox (Description = "for setting the symbol to trade")
    public static void setSymbol(String _symbol){
        symbol = _symbol;
    }

    @Drillox (Description = "for setting the broker server name for executing of trades")
    public static void setServerName(String _serverName){
        ServerName = _serverName;
    }

    @Drillox (Description = "for setting the login into the broker server")
    public static void setLogin(String _login){
        Login = _login;
    }

    @Drillox (Description = "for setting the password of the account")
    public static void setPassword(String _password){
        Password = _password;
    }

}
