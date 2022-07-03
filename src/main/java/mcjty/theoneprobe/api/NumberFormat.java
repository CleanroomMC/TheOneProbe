package mcjty.theoneprobe.api;

/**
 * <ul>{@link NumberFormat#FULL} - Full format</ul>
 * <ul>{@link NumberFormat#COMPACT} - Compact format (like 3.5M)</ul>
 * <ul>{@link NumberFormat#COMMAS} - Language dependent comma separated format</ul>
 * <ul>{@link NumberFormat#NONE} - No output (empty string)</ul>
 */
public enum NumberFormat {
    FULL,
    COMPACT,
    COMMAS,
    NONE
}
