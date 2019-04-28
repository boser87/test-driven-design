package end_to_end;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class FakeAuctionServer {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String AUCTION_PASSWORD = "auction";

    public static final String XMPP_HOSTNAME = "192.168.1.207";
    public static final int XMPP_PORT = 5222;
    public static final String XMPP_SERVICE_NAME = "localhost";

    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(XMPP_HOSTNAME, XMPP_PORT, XMPP_SERVICE_NAME);
        connectionConfiguration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        this.connection = new XMPPConnection(connectionConfiguration);
    }

    private final SingleMessageListener messageListener = new SingleMessageListener();

    public void startSellingItem() throws XMPPException {
        System.out.println("Auction server is starting to sell an item. Connecting...");
        connection.connect();
        System.out.println("Auction server is logging in...");
        connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
        System.out.println("Auction server is listening...");
        connection.getChatManager().addChatListener(new ChatManagerListener() {
            public void chatCreated(Chat chat, boolean createdLocally) {
                currentChat = chat;
                chat.addMessageListener(messageListener);
            }
        });
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        System.out.println("Auction server is expecting to receive join request from sniper within 5 seconds...");
        messageListener.receivesAMessage();
    }

    public void announceClosed() throws XMPPException {
        System.out.println("Auction server is sending a message to announce that bid is closed");
        currentChat.sendMessage(new Message());
    }

    public void stop() {
        connection.disconnect();
    }

    public String getItemId() {
        return itemId;
    }

    public class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

        public void processMessage(Chat chat, Message message) {
            messages.add(message);
        }

        public void receivesAMessage() throws InterruptedException {
            assertThat("Message", messages.poll(5, SECONDS), is(notNullValue()));
            System.out.println("Auction server has received joining message");
        }
    }
}