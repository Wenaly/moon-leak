package com.moon.impl.modules.misc;

import com.moon.api.event.events.PacketEvent;
import com.moon.api.event.handler.EventHandler;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import com.moon.api.setting.settings.EnumSetting;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.Arrays;

@Module.registration(name = "Chat", description = "Modifies the chat :)", category = Module.Category.Misc)
public class Chat extends Module {


    public Setting<String> suffix = new EnumSetting("Suffix", "Moon", Arrays.asList("Moon", "OctoHack", "JorgitoHack"), this);

    @EventHandler
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = event.getPacket();
            String s = packet.getMessage();
            if (s.startsWith("/")) {
                return;
            }
            switch (this.suffix.getValue()) {
                case "Moon": {
                    s = s + " \u23d0 \u1d0d\u1d0f\u1d0f\u0274";
                    break;
                }
                case "OctoHack": {
                    s = s + " \u23d0 \u039e\uff2f\u1d04\u1d1b\u0e4f\u0266\u039b\u1d04\u13e6\u039e";
                    break;
                }
                case "JorgitoHack": {
                    s = s + " \u23d0 \u1d0a\u1d0f\u0280\u0262\u026a\u1d1b\u1d0f\u029c\u1d00\u1d04\u1d0b";
                    break;
                }
            }
            if (s.length() >= 256) {
                s = s.substring(0, 256);
            }
            packet.message = s;
        }
    }

}
