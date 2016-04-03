package com.ymdrech.testandroidprodge;

import com.ymdrech.testandroidprodge.model.FriendMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by e4t on 31/03/16.
 */
public class StubFriendService implements FriendService {
    @Override
    public List<FriendMessage> getMessages() {
        final FriendMessage message = new FriendMessage();
        message.setDateSent(new Date());
        message.setMessage("test message");
        return new ArrayList<FriendMessage>() {{
            add(message);
        }};
    }
}
