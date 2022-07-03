package mcjty.theoneprobe.api;

import javax.annotation.Nonnull;

/**
 * Represent a style for text. This style is configurable by the user and used server-side.
 * Use it like you would use a TextFormatting in your strings. i.e.:
 * `probeInfo.text(TextStyleClass.ERROR + "Error! World will explode in 5 seconds!");`
 *
 * <ul> {@link TextStyleClass#MODNAME} - Name of the mod </ul>
 * <ul> {@link TextStyleClass#NAME} - Name of the block or entity </ul>
 * <ul> {@link TextStyleClass#INFO} - General info, neutral </ul>
 * <ul> {@link TextStyleClass#INFOIMP} - General info, important </ul>
 * <ul> {@link TextStyleClass#WARNING} - Warning, something is not ready (not mature), or missing stuff </ul>
 * <ul> {@link TextStyleClass#ERROR} - Error, bad situation, out of power, things like that </ul>
 * <ul> {@link TextStyleClass#OBSOLETE} - Obsolete, deprecated, old information </ul>
 * <ul> {@link TextStyleClass#LABEL} - A label, use the 'context' code to set the same as the style that follows </ul>
 * <ul> {@link TextStyleClass#OK} - Status OK </ul>
 * <ul> {@link TextStyleClass#PROGRESS} - Progress rendering in case the bar is not used </ul>
 */
public enum TextStyleClass {
    MODNAME("m", "ModName"),
    NAME("n", "Name"),
    INFO("i", "Info"),
    INFOIMP("I", "InfoImportant"),
    WARNING("w", "Warning"),
    ERROR("e", "Error"),
    OBSOLETE("O", "Obsolete"),
    LABEL("l", "Label"),
    OK("o", "Ok"),
    PROGRESS("p", "Progress");

    private final String code;
    private final String readableName;

    TextStyleClass(@Nonnull String code, @Nonnull String readableName) {
        this.code = code;
        this.readableName = readableName;
    }

    @Nonnull
    public String getCode() {
        return code;
    }

    @Nonnull
    public String getReadableName() {
        return readableName;
    }

    @Nonnull
    @Override
    public String toString() {
        return "{=" + code + "=}";
    }
}
