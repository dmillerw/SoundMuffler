package dmillerw.sound.core.item;

import com.google.common.collect.Lists;
import dmillerw.sound.api.IItemSoundMuffler;
import dmillerw.sound.api.SoundEntry;
import dmillerw.sound.client.sound.SoundMuffled;
import dmillerw.sound.core.TabSoundMuffler;
import dmillerw.sound.core.handler.InternalHandler;
import dmillerw.sound.core.lib.ModInfo;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author dmillerw
 */
public class ItemMagicalEarplugs extends Item implements IItemSoundMuffler {

    public ItemMagicalEarplugs() {
        super();

        setMaxDamage(0);
        setMaxStackSize(1);
        setCreativeTab(TabSoundMuffler.TAB);

        setUnlocalizedName(ModInfo.MOD_ID + ":magical_earplugs");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (playerIn.isSneaking()) {
            InternalHandler.openConfigurationGUI(playerIn, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        } else {
            EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemStackIn);
            ItemStack itemstack = playerIn.getItemStackFromSlot(entityequipmentslot);

            if (itemstack == null) {
                playerIn.setItemStackToSlot(entityequipmentslot, itemStackIn.copy());
                itemStackIn.stackSize = 0;
                return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
            } else {
                return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
            }
        }
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return armorType == EntityEquipmentSlot.HEAD;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ISound getMuffledSound(ItemStack itemStack, String name, ISound sound, SoundCategory soundCategory) {
        if (!itemStack.hasTagCompound())
            return sound;

        NBTTagCompound nbtTagCompound = itemStack.getTagCompound();

        if (!nbtTagCompound.hasKey("entries"))
            nbtTagCompound.setTag("entries", new NBTTagList());

        NBTTagList nbtTagList = nbtTagCompound.getTagList("entries", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            SoundEntry soundEntry = SoundEntry.readFromNBT(nbtTagList.getCompoundTagAt(i));
            if (soundEntry.nameMatches(name)) {
                return new SoundMuffled(sound, soundEntry.volumeModifier);
            }
        }

        return null;
    }

    @Override
    public List<SoundEntry> getSoundEntries(ItemStack itemStack) {
        List<SoundEntry> soundEntries = Lists.newArrayList();

        if (!itemStack.hasTagCompound())
            return soundEntries;

        NBTTagCompound nbtTagCompound = itemStack.getTagCompound();

        if (!nbtTagCompound.hasKey("entries"))
            nbtTagCompound.setTag("entries", new NBTTagList());

        NBTTagList nbtTagList = nbtTagCompound.getTagList("entries", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            soundEntries.add(SoundEntry.readFromNBT(nbtTagList.getCompoundTagAt(i)));
        }

        return soundEntries;
    }

    @Override
    public void addSoundEntry(ItemStack itemStack, SoundEntry soundEntry) {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());

        NBTTagCompound nbtTagCompound = itemStack.getTagCompound();

        if (!nbtTagCompound.hasKey("entries"))
            nbtTagCompound.setTag("entries", new NBTTagList());

        NBTTagList nbtTagList = nbtTagCompound.getTagList("entries", Constants.NBT.TAG_COMPOUND);

        NBTTagCompound entryTag = new NBTTagCompound();
        soundEntry.writeToNBT(entryTag);
        nbtTagList.appendTag(entryTag);

        nbtTagCompound.setTag("entries", nbtTagList);
    }

    @Override
    public void removeSoundEntry(ItemStack itemStack, SoundEntry soundEntry) {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());

        NBTTagCompound nbtTagCompound = itemStack.getTagCompound();

        if (!nbtTagCompound.hasKey("entries"))
            nbtTagCompound.setTag("entries", new NBTTagList());

        NBTTagList nbtTagList = nbtTagCompound.getTagList("entries", Constants.NBT.TAG_COMPOUND);
        NBTTagList newNBTTagList = new NBTTagList();

        boolean removed = false;
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            SoundEntry listEntry = SoundEntry.readFromNBT(nbtTagList.getCompoundTagAt(i));
            if (removed || !soundEntry.equals(listEntry)) {
                newNBTTagList.appendTag(nbtTagList.getCompoundTagAt(i));
                removed = true;
            }
        }

        nbtTagCompound.setTag("entries", newNBTTagList);
    }
}
