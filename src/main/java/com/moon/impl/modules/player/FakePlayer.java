package com.moon.impl.modules.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.moon.api.module.Module;
import com.moon.api.utils.misc.MessageUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Module.registration(name = "FakePlayer", description = "SAMUEL OMAR TABARES", category = Module.Category.Player)
public class FakePlayer extends Module {

    private final String name = "SSLWasRatted";
    private EntityOtherPlayerMP _fakePlayer;

    public static String getUuid(String name) {
        JsonParser parser = new JsonParser();
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try {
            String UUIDJson = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
            if (UUIDJson.isEmpty()) {
                return "invalid name";
            }
            JsonObject UUIDObject = (JsonObject) parser.parse(UUIDJson);
            return FakePlayer.reformatUuid(UUIDObject.get("id").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private static String reformatUuid(String uuid) {
        String longUuid = "";
        longUuid = longUuid + uuid.substring(1, 9) + "-";
        longUuid = longUuid + uuid.substring(9, 13) + "-";
        longUuid = longUuid + uuid.substring(13, 17) + "-";
        longUuid = longUuid + uuid.substring(17, 21) + "-";
        longUuid = longUuid + uuid.substring(21, 33);
        return longUuid;
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            this.disable();
            return;
        }
        this._fakePlayer = null;
        if (FakePlayer.mc.player != null) {
            try {
                this._fakePlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, new GameProfile(UUID.fromString(FakePlayer.getUuid(this.name)), this.name));
            } catch (Exception e) {
                this._fakePlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, new GameProfile(UUID.fromString("83218330-93bd-441f-a49f-b2a28f4a2cf2"), this.name));
                MessageUtils.sendMessage("Failed to load uuid, setting another one.");
            }
            MessageUtils.sendMessage(String.format("%s has been spawned.", this.name));
            this._fakePlayer.copyLocationAndAnglesFrom(FakePlayer.mc.player);
            this._fakePlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
            FakePlayer.mc.world.addEntityToWorld(-100, this._fakePlayer);
        }
    }

    @Override
    public void onDisable() {
        if (FakePlayer.mc.world != null && FakePlayer.mc.player != null) {
            super.onDisable();
            FakePlayer.mc.world.removeEntity(this._fakePlayer);
        }
    }
}
