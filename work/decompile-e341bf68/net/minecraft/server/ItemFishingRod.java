package net.minecraft.server;

public class ItemFishingRod extends Item {

    public ItemFishingRod(Item.Info item_info) {
        super(item_info);
        this.a(new MinecraftKey("cast"), (itemstack, world, entityliving) -> {
            if (entityliving == null) {
                return 0.0F;
            } else {
                boolean flag = entityliving.getItemInMainHand() == itemstack;
                boolean flag1 = entityliving.getItemInOffHand() == itemstack;

                if (entityliving.getItemInMainHand().getItem() instanceof ItemFishingRod) {
                    flag1 = false;
                }

                return (flag || flag1) && entityliving instanceof EntityHuman && ((EntityHuman) entityliving).hookedFish != null ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        int i;

        if (entityhuman.hookedFish != null) {
            if (!world.isClientSide) {
                i = entityhuman.hookedFish.b(itemstack);
                itemstack.damage(i, entityhuman, (entityhuman1) -> {
                    entityhuman1.d(enumhand);
                });
            }

            world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (ItemFishingRod.i.nextFloat() * 0.4F + 0.8F));
        } else {
            world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemFishingRod.i.nextFloat() * 0.4F + 0.8F));
            if (!world.isClientSide) {
                i = EnchantmentManager.c(itemstack);
                int j = EnchantmentManager.b(itemstack);

                world.addEntity(new EntityFishingHook(entityhuman, world, j, i));
            }

            entityhuman.b(StatisticList.ITEM_USED.b(this));
        }

        return InteractionResultWrapper.a(itemstack);
    }

    @Override
    public int c() {
        return 1;
    }
}
