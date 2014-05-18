package me.planetguy.minebase;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
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
	public static final int META_AA_FULL = 4;
	public static final int META_AA_EMPTY=5;
	public static final int META_SHIELD=6;
	
	IIcon[][] iconSideMetaMap;
	String[][] iconNameMap=new String[][]{
		new String[]{"blank","blank","hub","hub","hub","hub"},
		new String[]{"basicBlock","basicBlock","inserter","inserter","inserter","inserter"},
		new String[]{"ground","ground","crossFrame","crossFrame","crossFrame","crossFrame"},
		new String[]{"basicBlock","basicBlock","basicBlock","basicBlock","basicBlock","basicBlock"},
		new String[]{"antiAir","crossFrame","crossFrame","crossFrame","crossFrame","crossFrame"},
		new String[]{"antiAirEmpty","crossFrame","crossFrame","crossFrame","crossFrame","crossFrame"},
		new String[]{"shield","crossFrame","crossFrame","crossFrame","crossFrame","crossFrame"}
	};

	protected BlockMinebase() {
		super(Material.iron);
		this.setCreativeTab(Minebase.creativeTab);
		LanguageRegistry.instance().addNameForObject(this, "en_US", "Minebase Block");
		this.setHardness(9001).setResistance(9001);//Not supposed to be broken or blown up.
	}

	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int side, float i, float d, float k){
		int myMeta=w.getBlockMetadata(x, y, z);
		printIcons();
		switch(myMeta){
		case META_LAUNCHER:
			TileEntityMinebase te=(TileEntityMinebase) w.getTileEntity(x, y, z);
			double dx=player.posX-x-.5;
			double dz=player.posZ-z-.5;
			System.out.println(">>>("+dx+","+dz+")");
			launchProjectile(te, dx, dz);
			return true;

		case META_ASSEMBLER:
			te=(TileEntityMinebase) w.getTileEntity(x, y+1, z);
			if(te==null)return false;
			ItemStack item=player.getHeldItem();
			te.setStoredProjectileType(getProjectile(item));
			return true;
		case META_TRAIL:
			TileEntity tileent=w.getTileEntity(x, y, z);
			if(tileent instanceof TileEntityTrail){
				TileEntityTrail trail=(TileEntityTrail)tileent;
				player.moveEntity(trail.destX, trail.destY, trail.destZ);
			}else{
				//player.moveEntity(x, y+1, z);
			}
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
			projectile.motionY=MathHelper.sqrt_double(xPower*xPower+zPower*zPower) * getPowerFactor();
			projectile.motionZ=-zPower* getPowerFactor();
		}
	}

	public double getPowerFactor(){
		return 0.5;
	}

	public ProjectileType getProjectile(ItemStack is){
		if(is==null)return null;
		if(is.getItem().equals(Items.arrow)){
			return ProjectileType.BOMB;
		}else if(is.getItem().equals(Item.getItemFromBlock(Blocks.chest))){
			return ProjectileType.HUB;
		}else if(is.getItem().equals(Items.ender_eye)){
			return ProjectileType.TOWER;
		}else if(is.getItem().equals(Item.getItemFromBlock(Blocks.wool))){
			return ProjectileType.BRIDGE;
		}else if(is.getItem().equals(Item.getItemFromBlock(Blocks.obsidian))){
			return ProjectileType.ANTI_AIR;
		}else if(is.getItem().equals(Item.getItemFromBlock(Blocks.diamond_block))){
			return ProjectileType.SHIELD;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List items){
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}

	public IIcon getIcon(int side, int meta){
		try{
			return iconSideMetaMap[meta][side];
		}catch(Exception e){
			e.printStackTrace();
			return Blocks.gold_block.getIcon(side, meta);
		}
	}
	
	public void updateTick(World w, int x, int y, int z, Random r) {
		if(w.getBlockMetadata(x, y, z)==META_AA_EMPTY)
			w.setBlockMetadataWithNotify(x, y, z, META_AA_FULL, 3);
	}
	//>>>(0.6095776839028417,-0.30000001192092896) in -z

	//>>>(-0.3005073737585917,0.6314316392137016) in -x dir

	//>>>(1.300000011920929,0.660749122683228) in +x dir
	
	public void registerBlockIcons(IIconRegister ir){
		iconSideMetaMap=new IIcon[iconNameMap.length][];
		for(int x=0; x<iconNameMap.length; x++){
			IIcon[] row=new IIcon[iconNameMap[x].length];
			for(int y=0; y<row.length; y++){
				row[y]=ir.registerIcon(iconNameMap[x][y]);
			}
			iconSideMetaMap[x]=row;
		}
	}
	
	public void printIcons(){
		for(IIcon[] icons:iconSideMetaMap){
			for(IIcon icon:icons){
				System.out.print(icon.getIconName()+",");
			}System.out.println();
		}
	}
}
