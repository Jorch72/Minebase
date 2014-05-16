package me.planetguy.minebase;

import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockMinebase extends Block implements ITileEntityProvider{

	public static final int META_LAUNCHER=0;
	public static final int META_ASSEMBLER=1;
	public static final int META_PLATFORM=2;
	public static final int META_TRAIL=3;

	protected BlockMinebase() {
		super(Material.iron);
		this.setCreativeTab(Minebase.creativeTab);
		LanguageRegistry.instance().addNameForObject(this, "en_US", "Minebase Block");
	}

	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int side, float i, float d, float k){
		int myMeta=w.getBlockMetadata(x, y, z);
		
		switch(myMeta){
		case META_LAUNCHER:
			TileEntityMinebase te=(TileEntityMinebase) w.getTileEntity(x, y, z);
			double dx=player.posX-x;
			double dz=player.posZ-z;
			launchProjectile(te, dx, dz);
			return true;

		case META_ASSEMBLER:
			te=(TileEntityMinebase) w.getTileEntity(x, y+1, z);
			if(te==null)return false;
			ItemStack item=player.getHeldItem();
			te.setStoredProjectileType(getProjectile(item));
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if(meta==META_LAUNCHER)
			return new TileEntityMinebase();
		if(meta==META_TRAIL)
			return new TileEntityTrail();
		return new TileEntity();
	}

	public void launchProjectile(TileEntityMinebase te, double xPower, double zPower){
		if(te.getStoredProjectileType()!=null){
			EntityProjectile projectile=new EntityProjectile(te);
			te.setStoredProjectileType(null);
			te.getWorldObj().spawnEntityInWorld(projectile);
			projectile.motionX=-xPower* getPowerFactor();
			projectile.motionY=MathHelper.sqrt_double(xPower*xPower+zPower*xPower) * getPowerFactor();
			projectile.motionZ=-zPower* getPowerFactor();
		}
	}

	public double getPowerFactor(){
		return 1;
	}

	public ProjectileType getProjectile(ItemStack is){
		if(is==null)return null;
		if(is.getItem().equals(Items.arrow)){
			return ProjectileType.BOMB;
		}else if(is.getItem().equals(Item.getItemFromBlock(Blocks.chest))){
			return ProjectileType.HUB;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List items){
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}
	
	public IIcon getIcon(int side, int meta){
		if(meta==META_TRAIL)
			return Blocks.lapis_block.getIcon(0, 0);
		else if(meta==META_PLATFORM)
			return Blocks.iron_block.getIcon(0,0);
		else if(meta==META_LAUNCHER)
			return Blocks.diamond_block.getIcon(0, 0);
		else
			return Blocks.gold_block.getIcon(0, 0);
		
	}

}
