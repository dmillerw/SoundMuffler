package dmillerw.sound.core.handler;

import dmillerw.sound.core.item.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author dmillerw
 */
public class PlayerHandler {

    @SubscribeEvent
    public void onPlayerStruckByLightning(EntityStruckByLightningEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer) event.getEntity();
        ItemStack itemStack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

        if (itemStack != null && itemStack.getItem() == ModItems.magical_earplugs)
            player.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.mysterious_earplugs));
    }
}
