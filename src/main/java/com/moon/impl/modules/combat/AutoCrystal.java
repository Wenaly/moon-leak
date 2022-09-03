package com.moon.impl.modules.combat;

import com.moon.api.event.events.PacketEvent;
import com.moon.api.event.handler.EventHandler;
import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.EnumSetting;
import com.moon.api.setting.settings.FloatSetting;
import com.moon.api.utils.math.MathUtils;
import com.moon.api.utils.misc.PacketUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.stream.Collectors;

@Module.registration(name = "AutoCrystal", description = "", category = Module.Category.Combat)
public class AutoCrystal extends Module {

    /** @TODO Im going to over comment all of the code
     * @author DriftyDev
     **/
    /*

    private ArrayList<>;

    private final Setting<Float> range = new FloatSetting("Range", 5.5f, 0f, 6f, this);

    private final Setting<String> swingMode = new EnumSetting("SwingMode", "Mainhand", Arrays.asList("Mainhand", "Offhand"), this);
    private final Setting<String> swing = new EnumSetting("Swing", "Full", Arrays.asList("Full", "Packet"), this);

    @Override
    public void onEnable() {

    }

    @Override
    public void onUpdate() {

    }

    @EventHandler
    public void onPacket(PacketEvent.Send event) {
        if (event.getStage() != 1) return;

        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            CPacketPlayerTryUseItemOnBlock packet = event.getPacket();

            for (Entity entity : mc.world.getLoadedEntityList()) {
                if (!(entity instanceof EntityEnderCrystal)) continue;

                if (entity.getDistance(mc.player) <= range.getValue()) {
                    PacketUtils.sendPacketNoEvent(new CPacketUseEntity());
                    swing();
                }
            }
        }
    }

    private void place() {
        for (Entity entity : mc.world.getLoadedEntityList()) {

        }
    }

    private void swing() {
        switch (swingMode.getValue().toLowerCase()) {
            case "mainhand":
                switch (swing.getValue().toLowerCase()) {
                    case "full":
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    case "packet":
                        PacketUtils.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                }
            case "offhand":
                switch (swing.getValue().toLowerCase()) {
                    case "full":
                        mc.player.swingArm(EnumHand.OFF_HAND);
                    case "packet":
                        PacketUtils.sendPacket(new CPacketAnimation(EnumHand.OFF_HAND));
                }
        }
    }

    private EntityEnderCrystal getEndCrystal() {
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (!(entity instanceof EntityEnderCrystal)) continue;


        }
    }

    private BlockPos getPos() {
        HashMap<BlockPos, Double> positions = new HashMap<>();

        for (EntityPlayer player : mc.world.playerEntities) {
            if (mc.player.getDistanceSq(player) > MathUtils..square(targetRange.getValue().floatValue())) continue;
            if (entityPredict.getValue()) {
                float f = player.width / 2.0F, f1 = player.height;
                player.setEntityBoundingBox(new AxisAlignedBB(player.posX - (double) f, player.posY, player.posZ - (double) f, player.posX + (double) f, player.posY + (double) f1, player.posZ + (double) f));
                Entity y = CrystalUtil.getPredictedPosition(player, predictedTicks.getValue());
                player.setEntityBoundingBox(y.getEntityBoundingBox());
            }
            for (BlockPos blockPos : CrystalUtil.possiblePlacePositions(this.placeRange.getValue().floatValue(), true, this.thirteen.getValue())) {
                double damage = isBlockGood(blockPos, player);
                if (damage <= 0) continue;
                positions.put(blockPos, damage);
            }
        }

        // Sorting all positions && getting the best one.
        Map<BlockPos, Double> result = positions.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList(
                Map.Entry::getKey,
                (oldValue, newValue) -> oldValue, ArrayList::new));

        return finalArrayList;
    }
    */

}
