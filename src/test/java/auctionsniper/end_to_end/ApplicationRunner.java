package auctionsniper.end_to_end;

import auctionsniper.Main;

import static auctionsniper.MainWindow.*;
import static auctionsniper.end_to_end.FakeAuctionServer.*;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper" + "@" + XMPP_HOSTNAME.toLowerCase() + "/" + AUCTION_RESOURCE;
    // sniper@stefanos-macbook-pro.local/Auction
    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, Integer.toString(XMPP_PORT), XMPP_SERVICE_NAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.showsSniperStatus(STATUS_JOINING);

    }

    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(STATUS_LOST);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding() {
        driver.showsSniperStatus(STATUS_BIDDING);
    }
}
