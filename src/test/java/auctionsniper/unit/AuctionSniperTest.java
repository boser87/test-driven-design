package auctionsniper.unit;

import auctionsniper.Auction;
import auctionsniper.AuctionSniper;
import auctionsniper.SniperListener;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

public class AuctionSniperTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SniperListener sniperListener;

    @Mock
    private Auction auction;

    @Test
    public void reportsLostWhenAuctionCloses() {
        final AuctionSniper auctionSniper = new AuctionSniper(sniperListener, auction);
        auctionSniper.auctionClosed();
        verify(sniperListener).sniperLost();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int currentPrice = 1001;
        final int increment = 25;
        final AuctionSniper auctionSniper = new AuctionSniper(sniperListener, auction);
        auctionSniper.currentPrice(currentPrice, increment);
        verify(auction).bid(currentPrice + increment);
        verify(sniperListener, atLeast(1)).sniperBidding();
    }
}
