/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 18, 2015, 8:41:10 PM (GMT)]
 */
package vazkii.botania.common.block.decor.walls;

import net.minecraft.block.state.BlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.FutureStoneVariant;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.List;

public class Block18StoneWall extends BlockModWall {

	public Block18StoneWall() {
		super(ModFluffBlocks.stone, 4);
		setHardness(1.5F);
		setResistance(10F);
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, UP, NORTH, SOUTH, WEST, EAST, BotaniaStateProps.FUTURESTONEWALL_VARIANT);
	}

	@Override
	protected void specifyDefaultState() {
		setDefaultState(blockState.getBaseState()
				.withProperty(UP, false).withProperty(NORTH, false).withProperty(SOUTH, false)
				.withProperty(WEST, false).withProperty(EAST, false)
				.withProperty(BotaniaStateProps.FUTURESTONEWALL_VARIANT, FutureStoneVariant.ANDESITE)
		);
	}

	@Override
	public void register(String name) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, name);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		for(int i = 0; i < 4; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.stoneAlchemy;
	}

}
