package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class NetworkTools {

    @Nullable
    public static NBTTagCompound readNBT(@Nonnull ByteBuf dataIn) {
        try {
            return new PacketBuffer(dataIn).readCompoundTag();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeNBT(@Nonnull ByteBuf dataOut, @Nonnull NBTTagCompound nbt) {
        try {
            new PacketBuffer(dataOut).writeCompoundTag(nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function supports ItemStacks with more then 64 items.
     */
    @Nonnull
    public static ItemStack readItemStack(@Nonnull ByteBuf dataIn) {
        PacketBuffer buf = new PacketBuffer(dataIn);
        try {
            NBTTagCompound nbt = buf.readCompoundTag();
            if (nbt == null) return ItemStack.EMPTY;

            ItemStack stack = new ItemStack(nbt);
            stack.setCount(buf.readInt());
            return stack;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ItemStack.EMPTY;
    }

    /**
     * This function supports ItemStacks with more then 64 items.
     */
    public static void writeItemStack(@Nonnull ByteBuf dataOut, @Nonnull ItemStack itemStack) {
        PacketBuffer buf = new PacketBuffer(dataOut);
        NBTTagCompound nbt = new NBTTagCompound();
        itemStack.writeToNBT(nbt);
        try {
            buf.writeCompoundTag(nbt);
            buf.writeInt(itemStack.getCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static String readString(@Nonnull ByteBuf dataIn) {
        int s = dataIn.readInt();
        if (s == -1) return null;
        if (s == 0) return "";

        byte[] dst = new byte[s];
        dataIn.readBytes(dst);
        return new String(dst);
    }

    public static void writeString(@Nonnull ByteBuf dataOut, @Nullable String str) {
        if (str == null) {
            dataOut.writeInt(-1);
            return;
        }
        byte[] bytes = str.getBytes();
        dataOut.writeInt(bytes.length);
        if (bytes.length > 0) {
            dataOut.writeBytes(bytes);
        }
    }

    @Nullable
    public static String readStringUTF8(@Nonnull ByteBuf dataIn) {
        int s = dataIn.readInt();
        if (s == -1) return null;
        if (s == 0) return "";
        byte[] dst = new byte[s];
        dataIn.readBytes(dst);
        return new String(dst, StandardCharsets.UTF_8);
    }

    public static void writeStringUTF8(@Nonnull ByteBuf dataOut, @Nullable String str) {
        if (str == null) {
            dataOut.writeInt(-1);
            return;
        }
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        dataOut.writeInt(bytes.length);
        if (bytes.length > 0) {
            dataOut.writeBytes(bytes);
        }
    }

    @Nonnull
    public static BlockPos readPos(@Nonnull ByteBuf dataIn) {
        return new BlockPos(dataIn.readInt(), dataIn.readInt(), dataIn.readInt());
    }

    public static void writePos(@Nonnull ByteBuf dataOut, @Nonnull BlockPos pos) {
        dataOut.writeInt(pos.getX());
        dataOut.writeInt(pos.getY());
        dataOut.writeInt(pos.getZ());
    }

    public static <T extends Enum<T>> void writeEnum(@Nonnull ByteBuf buf, @Nullable T value, @Nonnull T nullValue) {
        if (value == null) {
            buf.writeInt(nullValue.ordinal());
        } else {
            buf.writeInt(value.ordinal());
        }
    }

    @Nullable
    public static <T extends Enum<T>> T readEnum(@Nonnull ByteBuf buf, @Nonnull T[] values) {
        return values[buf.readInt()];
    }

    public static <T extends Enum<T>> void writeEnumCollection(@Nonnull ByteBuf buf, @Nonnull Collection<T> collection) {
        buf.writeInt(collection.size());
        for (T type : collection) {
            buf.writeInt(type.ordinal());
        }
    }

    public static <T extends Enum<T>> void readEnumCollection(@Nonnull ByteBuf buf, @Nonnull Collection<T> collection, @Nonnull T[] values) {
        collection.clear();
        int size = buf.readInt();
        for (int i = 0 ; i < size ; i++) {
            collection.add(values[buf.readInt()]);
        }
    }

    public static void writeFloat(@Nonnull ByteBuf buf, @Nullable Float f) {
        if (f != null) {
            buf.writeBoolean(true);
            buf.writeFloat(f);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Nullable
    public static Float readFloat(@Nonnull ByteBuf buf) {
        if (buf.readBoolean()) {
            return buf.readFloat();
        } else {
            return null;
        }
    }
}
