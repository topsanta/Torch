package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class BlockCrops extends BlockPlant implements IBlockFragilePlantElement {

    public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 7);
    private static final AxisAlignedBB[] a = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    protected BlockCrops() {
        this.y(this.blockStateList.getBlockData().set(this.e(), Integer.valueOf(0)));
        this.a(true);
        this.a((CreativeModeTab) null);
        this.c(0.0F);
        this.a(SoundEffectType.c);
        this.p();
    }

    @Override
    public AxisAlignedBB b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return BlockCrops.a[iblockdata.get(this.e()).intValue()];
    }

    @Override
    protected boolean i(IBlockData iblockdata) {
        return iblockdata.getBlock() == Blocks.FARMLAND;
    }

    protected BlockStateInteger e() {
        return BlockCrops.AGE;
    }

    public int g() {
        return 7;
    }

    protected int z(IBlockData iblockdata) {
        return iblockdata.get(this.e()).intValue();
    }

    public IBlockData setAge(int i) {
        return this.getBlockData().set(this.e(), Integer.valueOf(i));
    }

    public boolean A(IBlockData iblockdata) {
        return iblockdata.get(this.e()).intValue() >= this.g();
    }

    @Override
    public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {
        super.b(world, blockposition, iblockdata, random);
        if (world.isLightLevel(blockposition.up(), 9)) { // Paper
            int i = this.z(iblockdata);

            if (i < this.g()) {
                float f = a(this, world, blockposition);

                if (random.nextInt((int) ((100.0F / world.spigotConfig.wheatModifier) * (25.0F / f)) + 1) == 0) { // Spigot
                    // CraftBukkit start
                    IBlockData data = this.setAge(i + 1);
                    CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, toLegacyData(data));
                    // CraftBukkit end
                }
            }
        }

    }

    public void g(World world, BlockPosition blockposition, IBlockData iblockdata) {
        int i = this.z(iblockdata) + this.b(world);
        int j = this.g();

        if (i > j) {
            i = j;
        }

        // CraftBukkit start
        IBlockData data = this.setAge(i);
        CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, toLegacyData(data));
        // CraftBukkit end
    }

    protected int b(World world) {
        return MathHelper.nextInt(world.random, 2, 5);
    }

    protected static float a(Block block, World world, BlockPosition blockposition) {
        float f = 1.0F;
        BlockPosition blockposition1 = blockposition.down();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                IBlockData iblockdata = world.getType(blockposition1.a(i, 0, j));

                if (iblockdata.getBlock() == Blocks.FARMLAND) {
                    f1 = 1.0F;
                    if (iblockdata.get(BlockSoil.MOISTURE).intValue() > 0) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPosition blockposition2 = blockposition.north();
        BlockPosition blockposition3 = blockposition.south();
        BlockPosition blockposition4 = blockposition.west();
        BlockPosition blockposition5 = blockposition.east();
        boolean flag = block == world.getType(blockposition4).getBlock() || block == world.getType(blockposition5).getBlock();
        boolean flag1 = block == world.getType(blockposition2).getBlock() || block == world.getType(blockposition3).getBlock();

        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = block == world.getType(blockposition4.north()).getBlock() || block == world.getType(blockposition5.north()).getBlock() || block == world.getType(blockposition5.south()).getBlock() || block == world.getType(blockposition4.south()).getBlock();

            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    @Override
    public boolean f(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return (world.j(blockposition) >= 8 || world.h(blockposition)) && this.i(world.getType(blockposition.down()));
    }

    protected Item h() {
        return Items.WHEAT_SEEDS;
    }

    protected Item i() {
        return Items.WHEAT;
    }

    @Override
    public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i) {
        super.dropNaturally(world, blockposition, iblockdata, f, 0);
        
        int j = this.z(iblockdata);

        if (j >= this.g()) {
            int k = 3 + i;

            for (int l = 0; l < k; ++l) {
                if (world.random.nextInt(2 * this.g()) <= j) {
                    a(world, blockposition, new ItemStack(this.h()));
                }
            }
        }
    }

    @Override
    public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return this.A(iblockdata) ? this.i() : this.h();
    }

    @Override
    public ItemStack a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return new ItemStack(this.h());
    }

    @Override
    public boolean a(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        return !this.A(iblockdata);
    }

    @Override
    public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        return true;
    }

    @Override
    public void b(World world, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        this.g(world, blockposition, iblockdata);
    }

    @Override
    public IBlockData fromLegacyData(int i) {
        return this.setAge(i);
    }

    @Override
    public int toLegacyData(IBlockData iblockdata) {
        return this.z(iblockdata);
    }

    @Override
    protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockCrops.AGE});
    }
}
