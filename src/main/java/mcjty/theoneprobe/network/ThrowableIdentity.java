package mcjty.theoneprobe.network;

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import mcjty.theoneprobe.TheOneProbe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * This class is used by TOP to prevent log spam from erroring providers
 */
public class ThrowableIdentity {

    private final String identifier;

    private static final Object2LongOpenHashMap<ThrowableIdentity> caughtThrowables = new Object2LongOpenHashMap<>();

    public ThrowableIdentity(@Nonnull Throwable e) {
        final String message = e.getMessage();
        final StringBuilder builder = new StringBuilder(message == null ? "<null>" : message);
        final StackTraceElement[] st = e.getStackTrace();
        for (int i = 0; i < Math.min(3, st.length); i++) {
            builder.append(st[i].getClassName())
                    .append(st[i].getFileName())
                    .append(st[i].getMethodName())
                    .append(st[i].getLineNumber());
        }
        this.identifier = builder.toString();
    }

    public static void registerThrowable(@Nonnull Throwable e) {
        ThrowableIdentity identity = new ThrowableIdentity(e);
        long curTime = System.currentTimeMillis();
        if (caughtThrowables.containsKey(identity)) {
            long lastTime = caughtThrowables.getLong(identity);
            // only log every 30 seconds
            if (curTime - lastTime < 30_000) {
                return;
            }
        }
        caughtThrowables.put(identity, curTime);
        TheOneProbe.setup.getLogger().debug("The One Probe caught the following error:");
        e.printStackTrace();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        return Objects.equals(identifier, ((ThrowableIdentity) o).identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
