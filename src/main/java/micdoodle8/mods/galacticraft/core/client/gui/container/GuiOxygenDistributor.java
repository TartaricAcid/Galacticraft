package micdoodle8.mods.galacticraft.core.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenDistributor;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLLog;



public class GuiOxygenDistributor extends GuiContainerGC implements ICheckBoxCallback
{
	private static final ResourceLocation distributorTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/oxygen.png");

	private final TileEntityOxygenDistributor distributor;

	private GuiElementInfoRegion oxygenInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 24, 56, 9, new ArrayList<String>(), this.width, this.height);
	private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 37, 56, 9, new ArrayList<String>(), this.width, this.height);

	private GuiElementCheckbox checkboxRenderBubble;
	
	public GuiOxygenDistributor(InventoryPlayer par1InventoryPlayer, TileEntityOxygenDistributor par2TileEntityAirDistributor)
	{
		super(new ContainerOxygenDistributor(par1InventoryPlayer, par2TileEntityAirDistributor));
		this.distributor = par2TileEntityAirDistributor;
		this.ySize = 180;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		List<String> batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.add("Distributor battery slot, place battery here");
		batterySlotDesc.add("if not using a connected power source");
		this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 31, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height));
		List<String> oxygenDesc = new ArrayList<String>();
		oxygenDesc.add("Oxygen Storage");
		oxygenDesc.add(EnumColor.YELLOW + "Oxygen: " + ((int) Math.floor(this.distributor.storedOxygen) + " / " + (int) Math.floor(this.distributor.maxOxygen)));
		this.oxygenInfoRegion.tooltipStrings = oxygenDesc;
		this.oxygenInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
		this.oxygenInfoRegion.yPosition = (this.height - this.ySize) / 2 + 24;
		this.oxygenInfoRegion.parentWidth = this.width;
		this.oxygenInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.oxygenInfoRegion);
		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add("Electrical Storage");
		electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.distributor.getEnergyStoredGC()) + " / " + (int) Math.floor(this.distributor.getMaxEnergyStoredGC())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;
		this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
		this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 37;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
		this.checkboxRenderBubble = new GuiElementCheckbox(0, this, var5 + 85, var6 + 87, "Bubble Visible");
		this.buttonList.add(this.checkboxRenderBubble);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(this.distributor.getInventoryName(), 8, 10, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.message.in.name") + ":", 87, 26, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.message.in.name") + ":", 87, 38, 4210752);
		String status = StatCollector.translateToLocal("gui.message.status.name") + ": " + this.getStatus();
		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 50, 4210752);
		status = "Oxygen Use: " + this.distributor.oxygenPerTick * 20 + "/s";
		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 60, 4210752);
//		status = ElectricityDisplay.getDisplay(this.distributor.ueWattsPerTick * 20, ElectricUnit.WATT);
//		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 70, 4210752);
//		status = ElectricityDisplay.getDisplay(this.distributor.getVoltage(), ElectricUnit.VOLTAGE);
//		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 80, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 90 + 3, 4210752);
	}

	private String getStatus()
	{
		if (this.distributor.getEnergyStoredGC() == 0)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.missingpower.name");
		}

		if (this.distributor.storedOxygen < this.distributor.oxygenPerTick)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.missingoxygen.name");
		}

		return EnumColor.DARK_GREEN + StatCollector.translateToLocal("gui.status.active.name");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiOxygenDistributor.distributorTexture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, 181);

		if (this.distributor != null)
		{
			int scale = this.distributor.getCappedScaledOxygenLevel(54);
			this.drawTexturedModalRect(var5 + 113, var6 + 25, 197, 7, Math.min(scale, 54), 7);
			scale = this.distributor.getScaledElecticalLevel(54);
			this.drawTexturedModalRect(var5 + 113, var6 + 38, 197, 0, Math.min(scale, 54), 7);

			if (this.distributor.getEnergyStoredGC() > 0)
			{
				this.drawTexturedModalRect(var5 + 99, var6 + 37, 176, 0, 11, 10);
			}

			if (this.distributor.storedOxygen > 0)
			{
				this.drawTexturedModalRect(var5 + 100, var6 + 24, 187, 0, 10, 10);
			}

			List<String> oxygenDesc = new ArrayList<String>();
			oxygenDesc.add("Oxygen Storage");
			oxygenDesc.add(EnumColor.YELLOW + "Oxygen: " + ((int) Math.floor(this.distributor.storedOxygen) + " / " + (int) Math.floor(this.distributor.maxOxygen)));
			this.oxygenInfoRegion.tooltipStrings = oxygenDesc;

			List<String> electricityDesc = new ArrayList<String>();
			electricityDesc.add("Electrical Storage");
			electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.distributor.getEnergyStoredGC()) + " / " + (int) Math.floor(this.distributor.getMaxEnergyStoredGC())));
			this.electricInfoRegion.tooltipStrings = electricityDesc;

			this.checkboxRenderBubble.isSelected = this.distributor.oxygenBubble.shouldRender();
		}
	}

	@Override
	public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
	{
		this.distributor.oxygenBubble.setShouldRender(newSelected);
		GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 6, this.distributor.xCoord, this.distributor.yCoord, this.distributor.zCoord, newSelected ? 1 : 0 }));
	}

	@Override
	public boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean getInitiallySelected(GuiElementCheckbox checkbox)
	{
		return this.distributor.oxygenBubble.shouldRender();
	}

	@Override
	public void onIntruderInteraction()
	{
		;
	}
}
