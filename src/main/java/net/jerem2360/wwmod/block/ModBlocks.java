package net.jerem2360.wwmod.block;

import net.jerem2360.wwmod.WWMod;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WWMod.MOD_ID);

    private static void OnBlockDestroyed(BlockEvent.BreakEvent e) {
        e.setCanceled(
                DisableWoodBreakingByHand(e.getPlayer(), e.getState(), e.getLevel(), e.getPos(), e.isCanceled())
        );
    }
    private static boolean DisableWoodBreakingByHand(Player player, BlockState blockstate, LevelAccessor level, BlockPos pos, boolean isCanceled) {
        if (isCanceled) return true;
        if (player.isSpectator() || player.isCreative()) return false;
        boolean isBlockWood = blockstate.is(BlockTags.LOGS) || blockstate.is(BlockTags.PLANKS);
        boolean isToolTypeAxe = player.getMainHandItem().getItem() instanceof AxeItem;
        if (isBlockWood && !isToolTypeAxe) {
            FluidState fluidState = blockstate.getFluidState();
            level.setBlock(pos, fluidState.createLegacyBlock(), 1);
            return true;  // return true to cancel event
        }
        return false;
    }

    public static void registerListeners() {
        MinecraftForge.EVENT_BUS.addListener(ModBlocks::OnBlockDestroyed);
    }
}
