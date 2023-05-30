package drillox.autotrader.bot;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;

public class UIpane extends JPanel {
    private final static BorderLayout layout = new BorderLayout();
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }

    public UIpane() throws MalformedURLException {
        super(layout);
        creatUI();
    }

    public void creatUI() throws MalformedURLException {
        JPanel DrilloxLogo = new JPanel(new BorderLayout());
        JPanel BotOptions = new JPanel(new GridLayout(1,2,0,0));
        JPanel liveTrades = new JPanel(new BorderLayout());
        JPanel liveTradesTab = new JPanel(new GridLayout(1,7,5,5));
        JPanel AllTrades = new JPanel();
        JTabbedPane accountInfo = new JTabbedPane();
        JMenuBar topBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem ext = new JMenuItem("Exit");
        JMenuItem oth = new JMenuItem("Others");
        JCheckBox isEAMode = new JCheckBox("EA Mode");

        JPanel data = new JPanel(new GridLayout(1,2,5,5));
        JPanel cont = new JPanel(new BorderLayout());


        liveTradesTab.add(new JLabel("Oredr ID"));
        liveTradesTab.add(new JLabel("Order Type"));
        liveTradesTab.add(new JLabel("Entry Price"));
        liveTradesTab.add(new JLabel("Take Profit"));
        liveTradesTab.add(new JLabel("Stop Loss"));
        liveTradesTab.add(new JLabel("Price"));
        liveTradesTab.add(new JLabel("Profit/Loss"));

        fileMenu.add(oth);
        fileMenu.add(ext);

        //topBar.add(fileMenu);
        topBar.add(new JLabel("Drillox Hacktory Working Group"));
        topBar.add(isEAMode);

        //creating the Bot options entry
        JPanel dataRow = new JPanel(new GridLayout(5,2,5,5));
        JTextField servername = new JTextField();
        JTextField login = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField symbol = new JTextField();
        JButton startBot = new JButton("Start Bot");
        JButton stopBot = new JButton("Stop Bot");
        JLabel label = new JLabel("Bot Options");
        dataRow.add(new JLabel("Symbol"));
        dataRow.add(symbol);
        dataRow.add(new JLabel("ServerName"));
        dataRow.add(servername);
        dataRow.add(new JLabel("Login"));
        dataRow.add(login);
        dataRow.add(new JLabel("Password"));
        dataRow.add(password);
        dataRow.add(startBot);
        dataRow.add(stopBot);
        label.setLocation(20,0);


        BotOptions.add(label);
        BotOptions.add(dataRow);

        liveTrades.add(liveTradesTab,BorderLayout.NORTH);
        liveTrades.add(AllTrades,BorderLayout.CENTER);
        liveTrades.add(accountInfo,BorderLayout.SOUTH);

        data.add(DrilloxLogo);
        data.add(BotOptions);

        cont.add(data,BorderLayout.NORTH);
        cont.add(liveTrades,BorderLayout.CENTER);

        this.add(topBar,BorderLayout.NORTH);
        this.add(cont,BorderLayout.CENTER);
    }
}
