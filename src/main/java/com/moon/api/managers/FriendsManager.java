package com.moon.api.managers;

import com.moon.api.utils.IMinecraft;
import com.moon.api.utils.entity.FriendUtils;

import java.util.ArrayList;
import java.util.List;

public class FriendsManager implements IMinecraft {

    private List<FriendUtils> friends;

    public FriendsManager() {
        this.friends = new ArrayList<>();
    }

    public void addFriend(String name) {
        if (!this.isFriend(name)) {
            this.friends.add(new FriendUtils(name));
        }
    }

    public void removeFriend(String name) {
        this.friends.removeIf(player -> player.getName().equalsIgnoreCase(name));
    }

    public boolean isFriend(String name) {
        for (FriendUtils player : this.friends) {
            if (player.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFriends() {
        return !this.friends.isEmpty();
    }

    public List<FriendUtils> getFriends() {
        return this.friends;
    }

    public void toggleFriend(String name) {
        if (this.isFriend(name)) {
            this.removeFriend(name);
        } else {
            this.addFriend(name);
        }
    }

    public void clear() {
        this.friends.clear();
    }

    public void setFriends(List<FriendUtils> list) {
        this.friends = list;
    }

}
