package mcjty.theoneprobe.api;

import javax.annotation.Nonnull;

/**
 * A mode that indicates what kind of information we want to display.
 * In your IProbeInfoAccessor or IProbeInfoProvider you can use this mode
 * to show different information.
 *
 * <ul> {@link ProbeMode#NORMAL} - Normal display. What a user expects to see </ul>
 * <ul> {@link ProbeMode#EXTENDED} - Extended. This is used when the player is sneaking </ul>
 * <ul> {@link ProbeMode#DEBUG} - Creative only. This is used when the player holds a creative probe </ul>
 */
public enum ProbeMode {
    NORMAL,
    EXTENDED,
    DEBUG;

    @Nonnull
    public static final ProbeMode[] VALUES = new ProbeMode[]{NORMAL, EXTENDED, DEBUG};
}
