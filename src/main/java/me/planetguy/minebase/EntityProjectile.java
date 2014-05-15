package me.planetguy.minebase;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityProjectile extends Entity{

	private String network;
	
	private final ProjectileType projectileType;
	
	public EntityProjectile(World par1World, String network, ProjectileType type) {
		super(par1World);
		this.network=network;
		this.projectileType=type;
	}

	@Override
	protected void entityInit() {
		//Don't see anything critical to do here...
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		network=tag.getString("network");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("network", network);
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

}
