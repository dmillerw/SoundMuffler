package dmillerw.sound.core.item;

import com.google.common.collect.Lists;
import dmillerw.sound.api.IMagicalEarmuffs;
import dmillerw.sound.api.SoundEntry;
import dmillerw.sound.client.sound.SoundHandler;
import dmillerw.sound.client.sound.SoundMuffled;
import dmillerw.sound.core.handler.InternalHandler;
import net.minecraft.client.audio.ISound;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

/**
 * @author dmillerw
 */
public class ItemMagicalEarmuffs extends Item implements IMagicalEarmuffs {

    public ItemMagicalEarmuffs() {
        super();

        setCreativeTab(CreativeTabs.tabCombat);
        setMaxStackSize(1);
        setMaxDamage(0);
        setUnlocalizedName("magicalEarmuffs");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (world.isRemote && entityPlayer.isSneaking()) {
            InternalHandler.openConfigurationGUI(entityPlayer);
        }
        return itemStack;
    }

    @Override
    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
        return armorType == 0;
    }

    /* IMAGICALEARMUFFS */
    @Override
    public ISound getMuffledSound(ItemStack itemStack, String name, ISound sound) {
        if (!itemStack.hasTagCompound())
            return sound;

        NBTTagCompound nbtTagCompound = itemStack.getTagCompound();

        if (!nbtTagCompound.hasKey("entries"))
            nbtTagCompound.setTag("entries", new NBTTagList());

        NBTTagList nbtTagList = nbtTagCompound.getTagList("entries", Constants.NBT.TAG_COMPOUND);

        for (int i=0; i<nbtTagList.tagCount(); i++) {
            SoundEntry soundEntry = SoundEntry.readFromNBT(nbtTagList.getCompoundTagAt(i));
            if (SoundHandler.soundMatches(name, soundEntry)) {
                return new SoundMuffled(sound, soundEntry.volumeModifier);
            }
        }

        return sound;
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

        for (int i=0; i<nbtTagList.tagCount(); i++) {
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
        for (int i=0; i<nbtTagList.tagCount(); i++) {
            SoundEntry listEntry = SoundEntry.readFromNBT(nbtTagList.getCompoundTagAt(i));
            if (removed || !soundEntry.equals(listEntry)) {
                newNBTTagList.appendTag(nbtTagList.getCompoundTagAt(i));
                removed = true;
            }
        }

        nbtTagCompound.setTag("entries", newNBTTagList);
    }
}
