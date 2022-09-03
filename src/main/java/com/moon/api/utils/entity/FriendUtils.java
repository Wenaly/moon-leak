package com.moon.api.utils.entity;

import com.moon.api.utils.IMinecraft;
import com.moon.api.utils.player.PlayerUtils;

import java.util.UUID;

public class FriendUtils implements IMinecraft {

    private final String name;
    private String nickName;

    public FriendUtils(String name) {
        this.name = name;
        PlayerUtils.getUUIDFromName(name);
    }

    public FriendUtils(String name, UUID uuid) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String name) {
        this.nickName = name;
    }

}
