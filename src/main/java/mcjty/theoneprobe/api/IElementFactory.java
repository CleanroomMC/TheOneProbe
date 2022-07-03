package mcjty.theoneprobe.api;

import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;

/**
 * A factory for elements
 */
public interface IElementFactory {

    /**
     * Create an element from a network buffer. This should be
     * symmetrical to what IElement.toBytes() creates.
     */
    IElement createElement(@Nonnull ByteBuf buf);
}
