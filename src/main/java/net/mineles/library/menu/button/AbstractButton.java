package net.mineles.library.menu.button;

import com.cryptomorin.xseries.XSound;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.node.Node;
import net.mineles.library.property.AttributeMap;
import net.mineles.library.utils.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractButton implements Button {
    protected final @NotNull AttributeMap attributes;
    protected final @NotNull ClickHandler clickHandler;

    protected AbstractButton(@NotNull AttributeMap attributes,
                             @NotNull ClickHandler clickHandler) {
        this.attributes = attributes;
        this.clickHandler = clickHandler;
    }

    @Override
    public @NotNull ClickResult click(@NotNull ClickContext context) {
        return this.clickHandler.click(context);
    }

    @Override
    public @NotNull AttributeMap getAttributes() {
        return this.attributes;
    }

    @Override
    public <T> @Nullable T getAttribute(@NotNull ButtonAttributes attribute) {
        return this.attributes.getValue(attribute);
    }

    @Override
    public <T> @Nullable T getAttribute(@NotNull ButtonAttributes attribute, @NotNull Class<T> type) {
        return this.attributes.getValue(attribute, type);
    }

    @Override
    public @NotNull Node getNode() {
        return this.attributes.getValue(ButtonAttributes.NODE);
    }

    @Override
    public @NotNull String getName() {
        return this.attributes.getValue(ButtonAttributes.NAME);
    }

    @Override
    public @NotNull ButtonType getType() {
        return this.attributes.getValue(ButtonAttributes.TYPE);
    }

    @Override
    public int[] getSlots() {
        return this.attributes.getValue(ButtonAttributes.SLOTS);
    }

    @Override
    public boolean hasSlot(int slot) {
        int[] slots = getSlots();
        return ArrayUtils.contains(slots, slot);
    }

    @Override
    public @Nullable XSound getClickSound() {
        return this.attributes.getValue(ButtonAttributes.CLICK_SOUND);
    }
}
