package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;
import org.bukkit.TreeType; // CraftBukkit

public abstract class WorldGenTreeProvider {

    public WorldGenTreeProvider() {}

    @Nullable
    protected abstract WorldGenFeatureConfigured<WorldGenFeatureSmallTreeConfigurationConfiguration, ?> b(Random random);

    public boolean a(GeneratorAccess generatoraccess, ChunkGenerator<?> chunkgenerator, BlockPosition blockposition, IBlockData iblockdata, Random random) {
        WorldGenFeatureConfigured<WorldGenFeatureSmallTreeConfigurationConfiguration, ?> worldgenfeatureconfigured = this.b(random);

        if (worldgenfeatureconfigured == null) {
            return false;
        } else {
            setTreeType(worldgenfeatureconfigured); // CraftBukkit
            generatoraccess.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 4);
            ((WorldGenFeatureSmallTreeConfigurationConfiguration) worldgenfeatureconfigured.c).a();
            if (worldgenfeatureconfigured.a(generatoraccess, chunkgenerator, random, blockposition)) {
                return true;
            } else {
                generatoraccess.setTypeAndData(blockposition, iblockdata, 4);
                return false;
            }
        }
    }

    // CraftBukkit start
    protected void setTreeType(WorldGenFeatureConfigured<?, ?> worldgentreeabstract) {
        if (worldgentreeabstract.c == BiomeDecoratorGroups.NORMAL_TREE) {
            BlockSapling.treeType = TreeType.TREE;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.HUGE_RED_MUSHROOM) {
            BlockSapling.treeType = TreeType.RED_MUSHROOM;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.HUGE_BROWN_MUSHROOM) {
            BlockSapling.treeType = TreeType.BROWN_MUSHROOM;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.JUNGLE_TREE) {
            BlockSapling.treeType = TreeType.COCOA_TREE;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.JUNGLE_TREE_NOVINE) {
            BlockSapling.treeType = TreeType.SMALL_JUNGLE;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.PINE_TREE) {
            BlockSapling.treeType = TreeType.TALL_REDWOOD;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.SPRUCE_TREE) {
            BlockSapling.treeType = TreeType.REDWOOD;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.ACACIA_TREE) {
            BlockSapling.treeType = TreeType.ACACIA;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.BIRCH_TREE) {
            BlockSapling.treeType = TreeType.BIRCH;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.TALL_BIRCH_TREE) {
            BlockSapling.treeType = TreeType.TALL_BIRCH;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.SWAMP_TREE) {
            BlockSapling.treeType = TreeType.SWAMP;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.FANCY_TREE) {
            BlockSapling.treeType = TreeType.BIG_TREE;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.JUNGLE_BUSH) {
            BlockSapling.treeType = TreeType.JUNGLE_BUSH;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.DARK_OAK_TREE) {
            BlockSapling.treeType = TreeType.DARK_OAK;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.MEGA_SPRUCE_TREE) {
            BlockSapling.treeType = TreeType.MEGA_REDWOOD;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.MEGA_PINE_TREE) {
            BlockSapling.treeType = TreeType.MEGA_REDWOOD;
        } else if (worldgentreeabstract.c == BiomeDecoratorGroups.MEGA_JUNGLE_TREE) {
            BlockSapling.treeType = TreeType.JUNGLE;
        } else {
            throw new IllegalArgumentException("Unknown tree generator " + worldgentreeabstract);
        }
    }
    // CraftBukkit end
}
