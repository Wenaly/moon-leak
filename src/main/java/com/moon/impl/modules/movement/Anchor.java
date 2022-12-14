package com.moon.impl.modules.movement;

import com.moon.api.module.Module;
import com.moon.api.setting.Setting;
import com.moon.api.setting.settings.BooleanSetting;
import com.moon.api.setting.settings.IntegerSetting;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Module.registration(name = "Anchor", description = "", category = Module.Category.Movement)
public class Anchor extends Module {

    public static boolean Anchoring;
    private final Setting< Integer > pitch = new IntegerSetting("Pitch", 60, 0, 90, this);
    private final Setting < Boolean > pull = new BooleanSetting("Pull", true, this);
    int holeblocks;


    public
    boolean isBlockHole ( BlockPos blockPos ) {
        this.holeblocks = 0;
        if ( this.mc.world.getBlockState ( blockPos.add ( 0 , 3 , 0 ) ).getBlock ( ) == Blocks.AIR ) {
            ++ this.holeblocks;
        }
        if ( this.mc.world.getBlockState ( blockPos.add ( 0 , 2 , 0 ) ).getBlock ( ) == Blocks.AIR ) {
            ++ this.holeblocks;
        }
        if ( this.mc.world.getBlockState ( blockPos.add ( 0 , 1 , 0 ) ).getBlock ( ) == Blocks.AIR ) {
            ++ this.holeblocks;
        }
        if ( this.mc.world.getBlockState ( blockPos.add ( 0 , 0 , 0 ) ).getBlock ( ) == Blocks.AIR ) {
            ++ this.holeblocks;
        }
        if ( this.mc.world.getBlockState ( blockPos.add ( 0 , - 1 , 0 ) ).getBlock ( ) == Blocks.OBSIDIAN || this.mc.world.getBlockState ( blockPos.add ( 0 , - 1 , 0 ) ).getBlock ( ) == Blocks.BEDROCK ) {
            ++ this.holeblocks;
        }
        if ( this.mc.world.getBlockState ( blockPos.add ( 1 , 0 , 0 ) ).getBlock ( ) == Blocks.OBSIDIAN || this.mc.world.getBlockState ( blockPos.add ( 1 , 0 , 0 ) ).getBlock ( ) == Blocks.BEDROCK ) {
            ++ this.holeblocks;
        }
        if ( this.mc.world.getBlockState ( blockPos.add ( - 1 , 0 , 0 ) ).getBlock ( ) == Blocks.OBSIDIAN || this.mc.world.getBlockState ( blockPos.add ( - 1 , 0 , 0 ) ).getBlock ( ) == Blocks.BEDROCK ) {
            ++ this.holeblocks;
        }
        if ( this.mc.world.getBlockState ( blockPos.add ( 0 , 0 , 1 ) ).getBlock ( ) == Blocks.OBSIDIAN || this.mc.world.getBlockState ( blockPos.add ( 0 , 0 , 1 ) ).getBlock ( ) == Blocks.BEDROCK ) {
            ++ this.holeblocks;
        }
        if ( this.mc.world.getBlockState ( blockPos.add ( 0 , 0 , - 1 ) ).getBlock ( ) == Blocks.OBSIDIAN || this.mc.world.getBlockState ( blockPos.add ( 0 , 0 , - 1 ) ).getBlock ( ) == Blocks.BEDROCK ) {
            ++ this.holeblocks;
        }
        return this.holeblocks >= 9;
    }

    public Vec3d GetCenter (double d , double d2 , double d3 ) {
        double d4 = Math.floor ( d ) + 0.5;
        double d5 = Math.floor ( d2 );
        double d6 = Math.floor ( d3 ) + 0.5;
        return new Vec3d ( d4 , d5 , d6 );
    }

    @Override
    public
    void onUpdate () {
        if ( this.mc.world == null ) {
            return;
        }
        if ( this.mc.player.rotationPitch >= (float) this.pitch.getValue ( ) ) {
            if ( this.isBlockHole ( this.getPlayerPos ( ).down ( 1 ) ) || this.isBlockHole ( this.getPlayerPos ( ).down ( 2 ) ) || this.isBlockHole ( this.getPlayerPos ( ).down ( 3 ) ) || this.isBlockHole ( this.getPlayerPos ( ).down ( 4 ) ) ) {
                Anchoring = true;
                if ( ! this.pull.getValue ( ) ) {
                    this.mc.player.motionX = 0.0;
                    this.mc.player.motionZ = 0.0;
                } else {
                    Vec3d center = this.GetCenter ( this.mc.player.posX , this.mc.player.posY , this.mc.player.posZ );
                    double d = Math.abs ( center.x - this.mc.player.posX );
                    double d2 = Math.abs ( center.z - this.mc.player.posZ );
                    if ( ! ( d <= 0.1 ) || ! ( d2 <= 0.1 ) ) {
                        double d3 = center.x - this.mc.player.posX;
                        double d4 = center.z - this.mc.player.posZ;
                        this.mc.player.motionX = d3 / 2.0;
                        this.mc.player.motionZ = d4 / 2.0;
                    }
                }
            } else {
                Anchoring = false;
            }
        }
    }

    @Override
    public
    void onDisable ( ) {
        Anchoring = false;
        this.holeblocks = 0;
    }

    public
    BlockPos getPlayerPos ( ) {
        return new BlockPos ( Math.floor ( this.mc.player.posX ) , Math.floor ( this.mc.player.posY ) , Math.floor ( this.mc.player.posZ ) );
    }
}
