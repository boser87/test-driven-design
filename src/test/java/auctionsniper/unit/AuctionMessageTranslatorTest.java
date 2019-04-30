package auctionsniper.unit;

import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionMessageTranslator;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;

public class AuctionMessageTranslatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private AuctionEventListener auctionEventListener;
    public static final Chat UNUSED_CHAT = null;

    @Test
    public void
    notifiesAuctionClosedWhenCloseMessageReceived() {
        AuctionMessageTranslator translator = new AuctionMessageTranslator(auctionEventListener);
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: CLOSE;");
        translator.processMessage(UNUSED_CHAT, message);
        verify(auctionEventListener).auctionClosed();
    }

    @Test
    public void
    notifiesBidDetailsWhenCurrentPriceMessageReceived() {
        AuctionMessageTranslator translator = new AuctionMessageTranslator(auctionEventListener);
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
        translator.processMessage(UNUSED_CHAT, message);
        verify(auctionEventListener).currentPrice(192, 7);
    }
}
