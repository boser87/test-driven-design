package auctionsniper;

public class AuctionSniper implements AuctionEventListener {

    private final Auction auction;
    private SniperListener sniperListener;

    public AuctionSniper(SniperListener sniperListener, Auction auction) {
        this.sniperListener = sniperListener;
        this.auction = auction;
    }

    @Override
    public void auctionClosed() {
        sniperListener.sniperLost();
    }

    @Override
    public void currentPrice(int currentPrice, int increment) {
        auction.bid(currentPrice + increment);
        sniperListener.sniperBidding();
    }
}
