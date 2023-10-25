package com.zyramc.ojvzinn.parkour.nms.entity;

import com.zyramc.ojvzinn.parkour.nms.NMS;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class EntityTopAmorStand extends EntityArmorStand {

    public EntityTopAmorStand(Location location, Integer position, ItemStack head) {
        super(((CraftWorld) location.getWorld()).getHandle());

        Location spawnLocation = location.clone();
        this.setPosition(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ());

        NMS.clearPathfinderGoal(this.getBukkitEntity());
        NMS.look(this.getBukkitEntity(), location.getYaw(), location.getPitch());

        this.setArms(true);
        this.setSmall(true);

        String boots = position == 1 ? "313" : position == 2 ? "317" : position == 3 ? "309" : position == 4 ? "305" : "301";
        String legs = position == 1 ? "312" : position == 2 ? "316" : position == 3 ? "308" : position == 4 ? "304" : "300";
        String chestplate = position == 1 ? "311" : position == 2 ? "315" : position == 3 ? "307" : position == 4 ? "303" : "299";

        this.setEquipment(1, CraftItemStack.asNMSCopy(BukkitUtils.deserializeItemStack(boots)));
        this.setEquipment(2, CraftItemStack.asNMSCopy(BukkitUtils.deserializeItemStack(legs)));
        this.setEquipment(3, CraftItemStack.asNMSCopy(BukkitUtils.deserializeItemStack(chestplate)));
        this.setEquipment(4, CraftItemStack.asNMSCopy(head));
    }

    @Override
    public void makeSound(String s, float f, float f1) {}

    @Override
    public void move(double d0, double d1, double d2) {}

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    public void kill() {
        this.dead = true;
    }

    public void a(NBTTagCompound nbttagcompound) {
    }

    public void b(NBTTagCompound nbttagcompound) {
    }

    public boolean c(NBTTagCompound nbttagcompound) {
        return false;
    }

    public boolean d(NBTTagCompound nbttagcompound) {
        return false;
    }

    public void e(NBTTagCompound nbttagcompound) {
    }

    public void f(NBTTagCompound nbttagcompound) {
    }

    @Override
    protected void dropDeathLoot(boolean flag, int i) {}


    @Override
    public void die() {
    }

    @Override
    public void setCustomName(String s) {
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
    }
}
