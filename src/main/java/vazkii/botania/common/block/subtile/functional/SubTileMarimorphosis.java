/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 29, 2015, 8:17:55 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.SubTileType;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SubTileMarimorphosis extends SubTileFunctional {

	private static final int COST = 12;
	private static final int RANGE = 8;
	private static final int RANGE_Y = 5;

	private static final int RANGE_MINI = 2;
	private static final int RANGE_Y_MINI = 1;

	private static final Type[] TYPES = new Type[] {
			Type.FOREST,
			Type.PLAINS,
			Type.MOUNTAIN,
			Type.MUSHROOM,
			Type.SWAMP,
			Type.SANDY,
			Type.COLD,
			Type.MESA
	};

	public SubTileMarimorphosis(SubTileType type) {
		super(type);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(supertile.getWorld().isRemote || redstoneSignal > 0)
			return;

		if(mana >= COST && ticksExisted % 2 == 0) {
			BlockPos coords = getCoordsToPut();
			if(coords != null) {
				IBlockState state = getStoneToPut(coords);
				if(state != null) {
					supertile.getWorld().setBlockState(coords, state);
					if(ConfigHandler.COMMON.blockBreakParticles.get())
						supertile.getWorld().playEvent(2001, coords, Block.getStateId(state));

					mana -= COST;
					sync();
				}
			}
		}
	}

	public IBlockState getStoneToPut(BlockPos coords) {
		Set<Type> types = BiomeDictionary.getTypes(supertile.getWorld().getBiome(coords));

		List<Block> values = new ArrayList<>();
		for (Type type : TYPES) {
			int times = 1;
			if (types.contains(type))
				times = 12;

			Block block = biomeTypeToBlock(type);
			for (int j = 0; j < times; j++)
				values.add(block);
		}

		return values.get(supertile.getWorld().rand.nextInt(values.size())).getDefaultState();
	}

	private Block biomeTypeToBlock(Type biomeType) {
		switch(biomeType.getName()) {
			default: throw new IllegalArgumentException("Should have verified type is suitable already: " + biomeType);
			case "FOREST": return ModFluffBlocks.biomeStoneForest;
			case "PLAINS": return ModFluffBlocks.biomeStonePlains;
			case "MOUNTAIN": return ModFluffBlocks.biomeStoneMountain;
			case "MUSHROOM": return ModFluffBlocks.biomeStoneFungal;
			case "SWAMP": return ModFluffBlocks.biomeStoneSwamp;
			case "SANDY": return ModFluffBlocks.biomeStoneDesert;
			case "COLD": return ModFluffBlocks.biomeStoneTaiga;
			case "MESA": return ModFluffBlocks.biomeStoneMesa;
		}
	}

	private BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();

		int range = getRange();
		int rangeY = getRangeY();

		BlockStateMatcher matcher = BlockStateMatcher.forBlock(Blocks.STONE);
		for(BlockPos pos : BlockPos.getAllInBox(getPos().add(-range, -rangeY, -range), getPos().add(range, rangeY, range))) {
			IBlockState state = supertile.getWorld().getBlockState(pos);
			if(state.getBlock().isReplaceableOreGen(state, supertile.getWorld(), pos, matcher))
				possibleCoords.add(pos);
		}

		if(possibleCoords.isEmpty())
			return null;
		return possibleCoords.get(supertile.getWorld().rand.nextInt(possibleCoords.size()));
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), getRange());
	}

	public int getRange() {
		return RANGE;
	}

	public int getRangeY() {
		return RANGE_Y;
	}

	@Override
	public int getColor() {
		return 0x769897;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.marimorphosis;
	}

	public static class Mini extends SubTileMarimorphosis {
		public Mini(SubTileType type) {
			super(type);
		}

		@Override public int getRange() { return RANGE_MINI; }
		@Override public int getRangeY() { return RANGE_Y_MINI; }
	}

}
