package dmillerw.sound.core.block;

import com.google.common.collect.Lists;
import dmillerw.sound.api.EventSoundMufflerTile;
import dmillerw.sound.api.ITileSoundMuffler;
import dmillerw.sound.api.SoundEntry;
import dmillerw.sound.client.sound.SoundMuffled;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.ISound;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

/**
 * @author dmillerw
 */
public class TileSoundMuffler extends TileEntity implements ITileSoundMuffler, ITickable {

    private static final int RANGE = 64;

    private List<SoundEntry> soundEntryList = Lists.newArrayList();
    private boolean registered = false;

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

    @Override
    public void update() {
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
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        NBTTagList nbtTagList = new NBTTagList();
        for (SoundEntry soundEntry : soundEntryList) {
            NBTTagCompound entryTag = new NBTTagCompound();
            soundEntry.writeToNBT(entryTag);
            nbtTagList.appendTag(entryTag);
        }
        nbtTagCompound.setTag("entries", nbtTagList);

        return nbtTagCompound;
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

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return new SPacketUpdateTileEntity(pos, 0, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    /* ISOUNDMUFFLER */
    @Override
    public int getRange() {
        return RANGE;
    }

    @Override
    public int getDimension() {
        return getWorld().provider.getDimension();
    }

    @Override
    public BlockPos getPosition() {
        return pos;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ISound getMuffledSound(String name, ISound sound, SoundCategory soundCategory) {
        for (SoundEntry soundEntry : soundEntryList) {
            if (soundEntry.nameMatches(name))
                return new SoundMuffled(sound, soundEntry.volumeModifier);
        }
        return null;
    }

    @Override
    public List<SoundEntry> getSoundEntries() {
        return soundEntryList;
    }

    @Override
    public void addSoundEntry(SoundEntry soundEntry) {
        soundEntryList.add(soundEntry);
        markDirtyAndNotify();
    }

    @Override
    public void removeSoundEntry(SoundEntry soundEntry) {
        boolean removed = false;
        Iterator<SoundEntry> iterator = soundEntryList.iterator();
        while (iterator.hasNext()) {
            SoundEntry soundEntry1 = iterator.next();
            if (!removed && soundEntry1.nameMatches(soundEntry.name)) {
                iterator.remove();
                removed = true;
            }
        }
        markDirtyAndNotify();
    }

    /* INTERNAL METHODS */
    public void markDirtyAndNotify() {
        markDirty();

        IBlockState state = worldObj.getBlockState(pos);
        worldObj.notifyBlockUpdate(pos, state, state, 3);
    }
}
