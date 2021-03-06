package me.planetguy.minebase;

import me.planetguy.minebase.util.Tree;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMinebase extends TileEntity implements ITrailDependent{

	private Tree record;

	private String nodeID;

	private ProjectileType storedProjectileType;

	private String owner="__NULL__";

	public TileEntityMinebase(){

	}

	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		if(tag.hasKey("network"))
			setNodeID(tag.getString("network"));
		else
			setNodeID("");
		setOwner(tag.getString("owningPlayer"));
	}

	public void writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		if(getNodeID()!=null&&!getNodeID().equals(""))
			tag.setString("network", getNodeID());
		tag.setString("owningPlayer", getOwner());
		record=Tree.map.inverse().get(nodeID);
	}

	private String getNodeID() {
		return nodeID;
	}

	private void setNodeID(String containingNetwork) {
		this.nodeID = containingNetwork;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public ProjectileType getStoredProjectileType() {
		return storedProjectileType;
	}

	public void setStoredProjectileType(ProjectileType storedProjectileType) {
		this.storedProjectileType = storedProjectileType;
	}

	@Override
	public void setParent(TileEntity parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onParentExplode() {
		worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air);
	}

	@Override
	public void onChildExplode(int x, int y, int z) {
		
	}

	@Override
	public void onPlaceChild(int x, int y, int z) {
		
	}

}
