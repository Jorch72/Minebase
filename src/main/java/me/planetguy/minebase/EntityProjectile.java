package me.planetguy.minebase;

import java.lang.reflect.Field;

import me.planetguy.minebase.multiblock.PatternHub;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityProjectile extends EntityArrow {

	private String network;

	private ProjectileType projectileType;

	final Field inGround;
	
	public EntityProjectile(World w) {
		super(w);
		projectileType=null;
		try{
			inGround=EntityArrow.class.getDeclaredField("inGround");
			inGround.setAccessible(true);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public EntityProjectile(TileEntityMinebase te){
		this(te.getWorldObj());
		String network=te.getOwner();
		ProjectileType type=te.getStoredProjectileType();
		this.network=network;
		this.projectileType=type;
		this.setSize(0.5F, 0.5F);
		this.posX=te.xCoord+0.5;
		this.posY=te.yCoord+1.5;
		this.posZ=te.zCoord+0.5;
	}

	/*
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		projectileType=ProjectileType.valueOf(tag.getString("type"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("type", projectileType.toString());
	}
	*/
	
	public void onUpdate(){
		super.onUpdate();
		try {
			if(inGround.getBoolean(this))
				onImpact();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public void onImpact() {
		System.out.println("Projectile of type "+this.projectileType+" landed");
		if(projectileType!=null){
			switch(this.projectileType){
			case ANTI_AIR:
				break;
			case BALLOON:
				break;
			case BOMB:
				this.worldObj.createExplosion(this, posX, posY, posZ, 4.0f, true);
				break;
			case BRIDGE:
				break;
			case CLUSTER:
				break;
			case CRAWLER:
				break;
			case EMP:
				break;
			case ENERGY:
				break;
			case HUB:
				PatternHub.build(worldObj, posX, posY, posZ, network);
				break;
			case MINE:
				break;
			case MISSILE:
				break;
			case OFFENSE:
				break;
			case RECLAIM:
				break;
			case REPAIR:
				break;
			case SHIELD:
				break;
			case SPIKE:
				break;
			case TOWER:
				break;
			case VIRUS:
				break;
			default:
				break;
			}
		}
		this.setDead();
	}

}
