package dmillerw.sound.core.block;

import com.google.common.collect.Lists;
import dmillerw.sound.api.EventSoundMufflerTile;
import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.api.SoundEntry;
import dmillerw.sound.client.sound.SoundHandler;
import dmillerw.sound.client.sound.SoundMuffled;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

import java.util.Iterator;
import java.util.List;

/**
 * @author dmillerw
 */
public class TileSoundMuffler extends TileEntity implements ITileSoundMuffler {

    private static final int RANGE = 32;

    private List<SoundEntry> soundEntryList = Lists.newArrayList();

    private void register() {
        if (registered) return;
        MinecraftForge.EVENT_BUS.post(new EventSoundMufflerTile.Register(this));
        registered = true;
    }

    private void unregister() {
        if (!registered) return;
        MinecraftForge.EVENT_BUS.post(new EventSoundMufflerTile.Unregister(this));
        registered = false;
    }

    private boolean registered = false;

    @Override
    public void updateEntity() {
        // Sound mufflers are only "registered" on the client
        if (worldObj.isRemote) {
            if (!registered)
                register();
        }
    }

    public void onBlockBreak() {
        unregister();
    }

    @Override
    public void invalidate() {
        super.invalidate();

        unregister();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();

        unregister();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        NBTTagList nbtTagList = new NBTTagList();
        for (SoundEntry soundEntry : soundEntryList) {
            NBTTagCompound entryTag = new NBTTagCompound();
            soundEntry.writeToNBT(entryTag);
            nbtTagList.appendTag(entryTag);
        }
        nbtTagCompound.setTag("entries", nbtTagList);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        soundEntryList = Lists.newArrayList();
        NBTTagList nbtTagList = nbtTagCompound.getTagList("entries", Constants.NBT.TAG_COMPOUND);
        for (int i=0; i<nbtTagList.tagCount(); i++) {
            soundEntryList.add(SoundEntry.readFromNBT(nbtTagList.getCompoundTagAt(i)));
        }
    }

    // PACKET HANDLING
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    /* ISOUNDMUFFLER */
    @Override
    public int getRange() {
        return RANGE;
    }

    @Override
    public int getDimension() {
        return getWorldObj().provider.dimensionId;
    }

    @Override
    public int getX() {
        return xCoord;
    }

    @Override
    public int getY() {
        return yCoord;
    }

    @Override
    public int getZ() {
        return zCoord;
    }

    @Override
    public ISound getMuffledSound(String name, ISound sound, SoundCategory soundCategory) {
        for (SoundEntry soundEntry : soundEntryList) {
            if (SoundHandler.soundMatches(name, soundEntry))
                return new SoundMuffled(sound, soundEntry.volumeModifier);
        }
        return sound;
    }

    @Override
    public List<SoundEntry> getSoundEntries() {
        return soundEntryList;
    }

    @Override
    public void addSoundEntry(SoundEntry soundEntry) {
        soundEntryList.add(soundEntry);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void removeSoundEntry(SoundEntry soundEntry) {
        boolean removed = false;
        Iterator<SoundEntry> iterator = soundEntryList.iterator();
        while (iterator.hasNext()) {
            SoundEntry soundEntry1 = iterator.next();
            if (!removed && SoundHandler.soundMatches(soundEntry.name, soundEntry1)) {
                iterator.remove();
                removed = true;
            }
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
