package com.luowenxuan.cementmod.item;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemHammer extends Item {
    // 锤子的基本属性
    private final float attackDamage;
    private final float attackSpeed;

    public ItemHammer() {
        this(6.0F, -3.0F);
        this.setUnlocalizedName(CementMod.MODID + ".hammer");
        this.setRegistryName("hammer");
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }

    public ItemHammer(float attackDamage, float attackSpeed) {
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;

        this.setMaxStackSize(1);
        this.setMaxDamage(256);
        this.setNoRepair();
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        // 仅当锤子未完全损坏时返回容器物品
        return stack.getItemDamage() < stack.getMaxDamage();
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        // 关键修复：确保不返回null
        if (itemStack.isEmpty() || itemStack.getItemDamage() >= itemStack.getMaxDamage()) {
            return ItemStack.EMPTY;
        }

        // 创建耐久度减1的新锤子
        ItemStack result = itemStack.copy();
        result.setItemDamage(itemStack.getItemDamage() + 1);

        return result;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote && player.isSneaking()) {
            player.getCooldownTracker().setCooldown(this, 40);
            stack.damageItem(3, player);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public void addInformation(ItemStack stack, World world, java.util.List<String> tooltip, net.minecraft.client.util.ITooltipFlag flag) {
        tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.hammer.damage", attackDamage));
        tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.hammer.attack_speed", attackSpeed));
        tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.hammer.durability_left",
                stack.getMaxDamage() - stack.getItemDamage(),
                stack.getMaxDamage()));

        if (stack.getItemDamage() >= stack.getMaxDamage() - 5) {
            tooltip.add(net.minecraft.client.resources.I18n.format("tooltip.hammer.warning"));
        }
    }

    @Override
    public boolean getShareTag() {
        return true;
    }
}