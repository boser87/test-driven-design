package auctionsniper;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import javax.swing.*;

public class Main implements SniperListener {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String SNIPER_STATUS_NAME = "sniper status";

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_PORT = 1;
    private static final int ARG_SERVICE_NAME = 2; // TODO: what is service name by the way?
    private static final int ARG_USERNAME = 3;
    private static final int ARG_PASSWORD = 4;
    private static final int ARG_ITEM_ID = 5;

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private MainWindow ui;

    public Main() throws Exception {
        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.joinAuction(args[ARG_HOSTNAME], args[ARG_PORT], args[ARG_SERVICE_NAME], args[ARG_USERNAME], args[ARG_PASSWORD], args[ARG_ITEM_ID]);
    }

    private void joinAuction(String hostname, String port, String serviceName, String username, String password, String itemId) throws XMPPException {
        ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(hostname, Integer.valueOf(port), serviceName);
        connectionConfiguration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        XMPPConnection connection = new XMPPConnection(connectionConfiguration);

        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);

        System.out.println("Sniper is creating a chat...");
        Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection),
                new AuctionMessageTranslator(new AuctionSniper(this, new Auction())));
        System.out.println("Sniper is sending an empty message to join the bid...");
        chat.sendMessage(new Message());
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                ui = new MainWindow();
            }
        });
    }

    @Override
    public void sniperLost() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Sniper has received auction closed message");
            ui.showsLabel("Lost");
        });
    }

    @Override
    public void sniperBidding() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Sniper has sent a bidding message");
            ui.showsLabel("Bidding");
        });
    }
}
