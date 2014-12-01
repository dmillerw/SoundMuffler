package dmillerw.sound.core.item;

import com.google.common.collect.Sets;
import dmillerw.sound.api.EnumListType;
import dmillerw.sound.api.IMagicalEarmuffs;
import dmillerw.sound.api.SoundEntry;
import dmillerw.sound.client.sound.SoundMuffled;
import net.minecraft.client.audio.ISound;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.Set;

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
            if (soundEntry.name.equalsIgnoreCase(name))
                return new SoundMuffled(sound, soundEntry.volumeModifier);
        }

        return sound;
    }

    @Override
    public EnumListType getListType(ItemStack itemStack) {
        if (!itemStack.hasTagCompound())
            return EnumListType.BLACKLIST;

        NBTTagCompound nbtTagCompound = itemStack.getTagCompound();

        return EnumListType.values()[nbtTagCompound.getByte("listType")];
    }

    @Override
    public Set<SoundEntry> getSoundEntries(ItemStack itemStack) {
        Set<SoundEntry> soundEntries = Sets.newHashSet();

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
    public void setListType(ItemStack itemStack, EnumListType listType) {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());

        NBTTagCompound nbtTagCompound = itemStack.getTagCompound();

        nbtTagCompound.setByte("listType", (byte) listType.ordinal());
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
    public void removeSoundEntry(ItemStack itemStack, String name) {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());

        NBTTagCompound nbtTagCompound = itemStack.getTagCompound();

        if (!nbtTagCompound.hasKey("entries"))
            nbtTagCompound.setTag("entries", new NBTTagList());

        NBTTagList nbtTagList = nbtTagCompound.getTagList("entries", Constants.NBT.TAG_COMPOUND);
        NBTTagList newNBTTagList = new NBTTagList();

        for (int i=0; i<nbtTagList.tagCount(); i++) {
            SoundEntry soundEntry = SoundEntry.readFromNBT(nbtTagList.getCompoundTagAt(i));
            if (!soundEntry.name.equalsIgnoreCase(name))
                newNBTTagList.appendTag(nbtTagList.getCompoundTagAt(i));
        }

        nbtTagCompound.setTag("entries", newNBTTagList);
    }
}
