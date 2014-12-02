package dmillerw.sound.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dmillerw.sound.core.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;

/**
 * @author dmillerw
 */
public class PlayerHandler {

    @SubscribeEvent
    public void onPlayerStruckByLightning(EntityStruckByLightningEvent event) {
        if (!(event.entity instanceof EntityPlayer))
            return;

        ItemStack itemStack = ((EntityPlayer) event.entity).getCurrentArmor(3);

        if (itemStack != null && itemStack.getItem() == CommonProxy.magicalEarmuffs)
            event.entity.setCurrentItemOrArmor(1, new ItemStack(CommonProxy.brokenEarmuffs));
    }
}
