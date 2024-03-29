package net.minecraft.server;

import javax.annotation.Nullable;
// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.player.PlayerShearEntityEvent;
// CraftBukkit end

public class EntitySnowman extends EntityGolem implements IRangedEntity {

    private static final DataWatcherObject<Byte> b = DataWatcher.a(EntitySnowman.class, DataWatcherRegistry.a);

    public EntitySnowman(EntityTypes<? extends EntitySnowman> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.25D, 20, 10.0F));
        this.goalSelector.a(2, new PathfinderGoalRandomStrollLand(this, 1.0D, 1.0000001E-5F));
        this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityInsentient.class, 10, true, false, (entityliving) -> {
            return entityliving instanceof IMonster;
        }));
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(4.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.20000000298023224D);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntitySnowman.b, (byte) 16);
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Pumpkin", this.hasPumpkin());
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKey("Pumpkin")) {
            this.setHasPumpkin(nbttagcompound.getBoolean("Pumpkin"));
        }

    }

    @Override
    public void movementTick() {
        super.movementTick();
        if (!this.world.isClientSide) {
            int i = MathHelper.floor(this.locX());
            int j = MathHelper.floor(this.locY());
            int k = MathHelper.floor(this.locZ());

            if (this.ay()) {
                this.damageEntity(DamageSource.DROWN, 1.0F);
            }

            if (this.world.getBiome(new BlockPosition(i, 0, k)).getAdjustedTemperature(new BlockPosition(i, j, k)) > 1.0F) {
                this.damageEntity(CraftEventFactory.MELTING, 1.0F); // CraftBukkit - DamageSource.BURN -> CraftEventFactory.MELTING
            }

            if (!this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
                return;
            }

            IBlockData iblockdata = Blocks.SNOW.getBlockData();

            for (int l = 0; l < 4; ++l) {
                i = MathHelper.floor(this.locX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                j = MathHelper.floor(this.locY());
                k = MathHelper.floor(this.locZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
                BlockPosition blockposition = new BlockPosition(i, j, k);

                if (this.world.getType(blockposition).isAir() && this.world.getBiome(blockposition).getAdjustedTemperature(blockposition) < 0.8F && iblockdata.canPlace(this.world, blockposition)) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(this.world, blockposition, iblockdata, this); // CraftBukkit
                }
            }
        }

    }

    @Override
    public void a(EntityLiving entityliving, float f) {
        EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
        double d0 = entityliving.getHeadY() - 1.100000023841858D;
        double d1 = entityliving.locX() - this.locX();
        double d2 = d0 - entitysnowball.locY();
        double d3 = entityliving.locZ() - this.locZ();
        float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;

        entitysnowball.shoot(d1, d2 + (double) f1, d3, 1.6F, 12.0F);
        this.a(SoundEffects.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(entitysnowball);
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return 1.7F;
    }

    @Override
    protected boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.getItem() == Items.SHEARS && this.hasPumpkin()) {
            if (!this.world.isClientSide) {
                // CraftBukkit start
                PlayerShearEntityEvent event = new PlayerShearEntityEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), this.getBukkitEntity());
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }
                // CraftBukkit end
                this.setHasPumpkin(false);
                itemstack.damage(1, entityhuman, (entityhuman1) -> {
                    entityhuman1.d(enumhand);
                });
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean hasPumpkin() {
        return ((Byte) this.datawatcher.get(EntitySnowman.b) & 16) != 0;
    }

    public void setHasPumpkin(boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(EntitySnowman.b);

        if (flag) {
            this.datawatcher.set(EntitySnowman.b, (byte) (b0 | 16));
        } else {
            this.datawatcher.set(EntitySnowman.b, (byte) (b0 & -17));
        }

    }

    @Nullable
    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_SNOW_GOLEM_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_SNOW_GOLEM_HURT;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_SNOW_GOLEM_DEATH;
    }
}
