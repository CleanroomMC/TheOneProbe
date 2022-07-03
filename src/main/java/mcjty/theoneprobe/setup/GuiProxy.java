package mcjty.theoneprobe.setup;

import mcjty.theoneprobe.gui.GuiConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiProxy implements IGuiHandler {


    public static int GUI_CONFIG = 2;

    @Nullable
    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        if (guiId == GUI_CONFIG) {
            return new GuiConfig();
        } else {
            return null;
        }
    }
}
