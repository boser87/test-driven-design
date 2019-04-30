package auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class AuctionMessageTranslator implements MessageListener {
    private AuctionEventListener listener;

    public AuctionMessageTranslator(AuctionEventListener listener) {
        this.listener = listener;
    }

    public void processMessage(Chat chat, Message message) {
        Map<String, String> event = unpackEventFrom(message);
        String type = event.get("Event");
        if ("CLOSE".equals(type)) {
            listener.auctionClosed();
        } else if ("PRICE".equals(type)) {
            listener.currentPrice(Integer.parseInt(event.get("CurrentPrice")),
                    Integer.parseInt(event.get("Increment")));
        }
    }

    private Map<String, String> unpackEventFrom(Message message) {
        Map<String, String> event =
                Arrays.stream(message.getBody().split(";"))
                        .map(element -> element.split(":"))
                        .collect(Collectors.toMap(p -> p[0].trim(), p -> p[1].trim()));
        return event;
    }
}
