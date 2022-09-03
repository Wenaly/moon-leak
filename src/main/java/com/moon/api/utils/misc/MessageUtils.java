package com.moon.api.utils.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.moon.Moon;
import com.moon.api.module.Module;
import com.moon.api.utils.IMinecraft;
import com.moon.impl.modules.client.Notifications;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils implements IMinecraft {

    public static final String opener() {
        return "[" + Moon.ModName + "] ";
    }

    public static void sendNotificationMessage(Module module, boolean enabled) {
        if (mc.world != null && mc.player != null) {
            if (Moon.Modules.isModuleEnabled("Notifications") && Notifications.Instance.modules.getValue()) {
                if (module.getName().equalsIgnoreCase("ClickGui")) return;
                ChatFormatting color = enabled ? ChatFormatting.GREEN : ChatFormatting.RED;
                if (color == ChatFormatting.GREEN) sendMessage(ChatFormatting.RESET + module.getName() + color + " on", Moon.class.hashCode());
                if (color == ChatFormatting.RED) sendMessage(ChatFormatting.RESET + module.getName() + color + " off", Moon.class.hashCode());
            }
        }
    }  // weird shit motherfuckin', iJese

    public static void sendMessage(String message) {
        if (mc.player != null) {
            mc.player.sendMessage(new ChatMessage(message));
        }
    }

    public static void sendOverwriteMessage(String message, int i) {
        if (mc.player != null) {
            final TextComponentString itc = new TextComponentString(opener() + message);
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(itc, i); // 5936
        }
    }

    public static class ChatMessage extends TextComponentBase {
        String messageInput;

        public ChatMessage(String message) {
            Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher m = p.matcher(message);
            StringBuffer sb = new StringBuffer();

            while (m.find()) {
                String replacement = "\u00A7" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }

            m.appendTail(sb);
            this.messageInput = sb.toString();
        }

        public String getUnformattedComponentText() {
            return this.messageInput;
        }

        @Override
        public ITextComponent createCopy() {
            return new ChatMessage(this.messageInput);
        }
    }

    public static void sendMessage(String message, int i){
        if (mc.player == null) return;
        try {
            TextComponentString component = new TextComponentString(opener() + message);
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, i);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
