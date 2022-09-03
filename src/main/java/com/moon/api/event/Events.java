package com.moon.api.event;

import com.google.common.base.Strings;
import com.moon.Moon;
import com.moon.api.event.handler.EventHandler;
import com.moon.api.managers.RotationManager;
import com.moon.api.module.Module;
import com.moon.api.priorities.Priorities;
import com.moon.api.utils.IMinecraft;
import com.moon.api.utils.NullUtils;
import com.moon.api.event.events.*;
import com.moon.api.utils.entity.RotationUtils;
import com.moon.api.utils.misc.TimerUtils;
import com.moon.api.utils.render.GLUPUtils;
import com.moon.impl.modules.misc.KillEffect;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;
import java.util.UUID;

public class Events implements NullUtils, IMinecraft {

    private final TimerUtils logoutTimer = new TimerUtils();
    private long time = -1;

    public Events() {
        MinecraftForge.EVENT_BUS.register(this);
        Moon.getEventProcessor().addEventListener(this);
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister(this);
        Moon.getEventProcessor().removeEventListener(this);
    }

    @SubscribeEvent
    public void onMousePress(InputEvent.MouseInputEvent event){
        int button = new MouseEvent().getButton();
        if (System.currentTimeMillis() - time < 200) {
            time = System.currentTimeMillis();
            return;
        }
        time = System.currentTimeMillis();
        for (Module module : Moon.Modules.getModules()) {
            if (module.getBind() >= -1 || module.getBind() == Keyboard.KEY_NONE) continue;
            if (button == 0 && module.getBind() == -2) {
                module.toggle();
            } else if (button == 1 && module.getBind()  == -3) {
                module.toggle();
            } else if (button == 2 && module.getBind() == -4) {
                module.toggle();
            } else if (button == 3 && module.getBind()  == -5) {
                module.toggle();
            } else if (button == 4 && module.getBind() == -6) {
                module.toggle();
            }
        }
    }


    @EventHandler
    public void onEntityDeath(DeathEvent event) {
        // MUNT AND SHIT
    }
    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!nullCheck() && event.getEntity().getEntityWorld().isRemote && event.getEntityLiving().equals(mc.player)) {
            Moon.Modules.onUpdate();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!nullCheck()) {
            Moon.Modules.onTick();
            Moon.getRotationManager().onTick();
        }
    }

    @EventHandler(priority = Priorities.Highest)
    public void onUpdateWalkingPlayerPost(UpdateWalkingPlayerEvent event) {
        if (nullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            //
        }
        if (event.getStage() == 1) {
            // update pos
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {
            ScaledResolution resolution = new ScaledResolution(mc);
            Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            Moon.Modules.onRender2D(render2DEvent);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        mc.profiler.startSection("moon");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);
        Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        GLUPUtils projection = GLUPUtils.getInstance();
        IntBuffer viewPort = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer projectionPort = GLAllocation.createDirectFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projectionPort);
        GL11.glGetInteger(2978, viewPort);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        projection.updateMatrices(viewPort, modelView, projectionPort, (double) scaledResolution.getScaledWidth() / (double) mc.displayWidth, (double) scaledResolution.getScaledHeight() / (double) mc.displayHeight);
        Moon.Modules.onRender3D(render3dEvent);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        mc.profiler.endSection();
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        Moon.Modules.onLogout();
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        Moon.Modules.onLogin();
        if(mc.world == null || !mc.world.isRemote) return;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() != 0) {
            return;
        }

        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = event.getPacket();
            try {
                if (packet.getOpCode() == 0x23 && packet.getEntity(mc.world) instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) packet.getEntity(mc.world);
                    Moon.getEventProcessor().postEvent(new TotemPopEvent(player));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.getPacket() instanceof SPacketPlayerListItem && !nullCheck() && this.logoutTimer.passedMs(1000)) {
            SPacketPlayerListItem packet = event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals(packet.getAction()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals(packet.getAction())) {
                return;
            }
            packet.getEntries().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty(data.getProfile().getName()) || data.getProfile().getId() != null).forEach(data -> {
                UUID id = data.getProfile().getId();
                switch (packet.getAction()) {
                    case ADD_PLAYER: {
                        String name = data.getProfile().getName();
                        Moon.getEventProcessor().postEvent(new ConnectionEvent(0, id, name));
                        break;
                    }
                    case REMOVE_PLAYER: {
                        EntityPlayer entity = mc.world.getPlayerEntityByUUID(id);
                        if (entity != null) {
                            String logoutName = entity.getName();
                            Moon.getEventProcessor().postEvent(new ConnectionEvent(1, entity, id, logoutName));
                            break;
                        }
                        Moon.getEventProcessor().postEvent(new ConnectionEvent(2, id, null));
                    }
                    default:
                        break;
                }
            });
        }
    }

}
