package org.phantomapi.chromatic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.InvalidConfigurationException;
import org.phantomapi.async.A;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;
import org.phantomapi.sync.S;
import org.phantomapi.util.C;
import org.phantomapi.util.D;
import org.phantomapi.util.F;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.W;

public class ChromaticReference
{
	private DataCluster mapping;
	private GMap<MaterialBlock, ChromaticBlock> colors;
	private Boolean avalible;
	private D d;
	
	public ChromaticReference() throws IOException, InvalidConfigurationException
	{
		d = new D("Chromatic");
		avalible = false;
		colors = new GMap<MaterialBlock, ChromaticBlock>();
		mapping = ChromaUtils.getMapping();
	}
	
	public void start()
	{
		new A()
		{
			@Override
			public void async()
			{
				d.v("== Building Chromatic Reference ==");
				
				GSet<String> keys = new GSet<String>();
				GSet<String> images = new GSet<String>();
				GMap<String, Color> colors = new GMap<String, Color>();
				GMap<MaterialBlock, ChromaticBlock> blocks = new GMap<MaterialBlock, ChromaticBlock>();
				
				for(String i : mapping.keys())
				{
					if(i.contains("."))
					{
						keys.add(i.split("\\.")[1]);
					}
				}
				
				d.s("> Processing " + F.f(keys.size()) + " References");
				
				for(String i : new GList<String>(keys))
				{
					DataCluster cc = mapping.crop("map." + i);
					String k = cc.getString("default");
					String a = cc.getString("top");
					String b = cc.getString("bottom");
					String c = cc.getString("side");
					
					if(k != null)
					{
						images.add(k);
						
						if(a != null)
						{
							images.add(a);
						}
						
						if(b != null)
						{
							images.add(b);
						}
						
						if(c != null)
						{
							images.add(c);
						}
					}
					
					else
					{
						d.f("Failed to parse " + C.YELLOW + "map." + i + C.RED + " No texture " + C.YELLOW + "null");
						keys.remove(i);
					}
				}
				
				d.s("> Processing " + F.f(images.size()) + " Textures");
				
				for(String i : images)
				{
					try
					{
						BufferedImage bu = ChromaUtils.getTexture(i);
						Color c = ChromaUtils.getProminentColor(bu);
						colors.put(i, c);
					}
					
					catch(Exception e)
					{
						d.f("Invalid Texture: " + C.YELLOW + i + ".png");
					}
				}
				
				d.s("> Multi-Blending " + F.f(keys.size()) + " Colors");
				
				for(String i : keys)
				{
					try
					{
						DataCluster cc = mapping.crop("map." + i);
						String k = cc.getString("default");
						String a = cc.getString("top");
						String b = cc.getString("bottom");
						String c = cc.getString("side");
						
						if(k != null)
						{
							MaterialBlock mb = W.getMaterialBlock(i.replaceAll("-", ":"));
							ChromaticBlock cb = new ChromaticBlock(mb);
							cb.getColor().put(BlockFace.UP, colors.get(k));
							cb.getColor().put(BlockFace.DOWN, colors.get(k));
							cb.getColor().put(BlockFace.NORTH, colors.get(k));
							cb.getColor().put(BlockFace.SOUTH, colors.get(k));
							cb.getColor().put(BlockFace.WEST, colors.get(k));
							cb.getColor().put(BlockFace.EAST, colors.get(k));
							
							if(a != null)
							{
								cb.getColor().put(BlockFace.UP, colors.get(a));
							}
							
							if(b != null)
							{
								cb.getColor().put(BlockFace.DOWN, colors.get(b));
							}
							
							if(c != null)
							{
								cb.getColor().put(BlockFace.NORTH, colors.get(c));
								cb.getColor().put(BlockFace.SOUTH, colors.get(c));
								cb.getColor().put(BlockFace.WEST, colors.get(c));
								cb.getColor().put(BlockFace.EAST, colors.get(c));
							}
							
							if(cc.contains("transparency.default"))
							{
								Double dd = cc.getDouble("transparency.default");
								Double e = cc.getDouble("transparency.top");
								Double f = cc.getDouble("transparency.bottom");
								Double g = cc.getDouble("transparency.side");
								
								if(dd != null)
								{
									cb.getTransparency().put(BlockFace.UP, dd);
									cb.getTransparency().put(BlockFace.DOWN, dd);
									cb.getTransparency().put(BlockFace.NORTH, dd);
									cb.getTransparency().put(BlockFace.SOUTH, dd);
									cb.getTransparency().put(BlockFace.WEST, dd);
									cb.getTransparency().put(BlockFace.EAST, dd);
									
									if(e != null)
									{
										cb.getTransparency().put(BlockFace.UP, e);
									}
									
									if(f != null)
									{
										cb.getTransparency().put(BlockFace.DOWN, f);
									}
									
									if(g != null)
									{
										cb.getTransparency().put(BlockFace.NORTH, g);
										cb.getTransparency().put(BlockFace.SOUTH, g);
										cb.getTransparency().put(BlockFace.WEST, g);
										cb.getTransparency().put(BlockFace.EAST, g);
									}
								}
								
								else if(e != null || f != null || g != null)
								{
									d.f("Invalid Transparency: " + C.YELLOW + i);
								}
							}
							
							blocks.put(mb, cb);
						}
					}
					
					catch(Exception e)
					{
						d.f("Failed to blend: " + C.YELLOW + i);
					}
				}
				
				d.s("> Rebasing for merge");
				
				new S()
				{
					@Override
					public void sync()
					{
						d.s("> Complete. Created " + F.f(blocks.size()) + " Chromatic Blocks");
						ChromaticReference.this.colors = blocks;
						avalible = true;
					}
				};
			}
		};
	}
	
	public DataCluster getMapping()
	{
		return mapping;
	}
	
	public GMap<MaterialBlock, ChromaticBlock> getColors()
	{
		return colors;
	}
	
	public Boolean getAvalible()
	{
		return avalible;
	}
	
	public ChromaticBlock getBlock(MaterialBlock mb)
	{
		return colors.get(mb);
	}
	
	public boolean hasBlock(MaterialBlock mb)
	{
		return getBlock(mb) != null;
	}
}