package io.github.boid.oldBabies.config;

import io.github.boid.oldBabies.OldBabies;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class ConfigScreen extends Screen {

    private final Screen parentScreen;
    private final Config config;

    private HeaderAndFooterLayout layout;

    protected ConfigScreen(Screen parent) {
        super(Component.literal("Old Babies configuration"));
        this.parentScreen = parent;
        this.config = new Config(OldBabies.getResolvedConfigPath());
        this.layout = new HeaderAndFooterLayout(this);
    }

    @Override
    protected void init() {
        this.layout = new HeaderAndFooterLayout(this);
        Button saveButton = Button.builder(Component.translatable("selectWorld.edit.save"), _ -> {
            if (!OldBabies.getConfig().matches(this.config)) {
                OldBabies.getConfig().setFrom(this.config);
                OldBabies.getConfig().saveToPath(OldBabies.getResolvedConfigPath());
                this.minecraft.reloadResourcePacks().thenAccept(_ -> {});
            }
            this.onClose();
        }).build();
        Button discardButton = Button.builder(CommonComponents.GUI_CANCEL, _ -> this.onClose()).build();
        layout.addTitleHeader(this.title, this.font);
        layout.addToContents(createCheckboxes());
        GridLayout footerLayout = new GridLayout();
        footerLayout.spacing(8);
        GridLayout.RowHelper rowHelper = footerLayout.createRowHelper(2);
        rowHelper.addChild(saveButton); rowHelper.addChild(discardButton);
        footerLayout.arrangeElements();
        layout.addToFooter(footerLayout);
        layout.arrangeElements();
        layout.visitWidgets(this::addRenderableWidget);
    }

    private Tooltip createTooltip(EntityType<?> type) {
        Set<EntityType<?>> additionalTypes = OldBabies.getAdditionalTypes(type);
        MutableComponent component = Component.literal("Restore the original baby model for ");
        component.append(type.getDescription());
        if (!additionalTypes.isEmpty()) {
            int typesRemaining = additionalTypes.size();
            for (EntityType<?> additionalType : additionalTypes) {
                if (typesRemaining == 1) {
                    component.append(" and ");
                } else {
                    component.append(", ");
                }
                component.append(additionalType.getDescription());
                typesRemaining -= 1;
            }
        }
        return Tooltip.create(component);
    }

    private ObjectSelectionList<CheckboxEntry> createCheckboxes() {
        Set<EntityType<?>> unsortedEntities = Config.getConfigurableEntities();
        List<EntityType<?>> entities = new ArrayList<>(unsortedEntities);
        entities.sort(Comparator.comparing(o -> o.getDescription().getString()));
        List<Checkbox> checkboxes = new ArrayList<>();
        for (EntityType<?> type : entities) {
            Checkbox checkbox = Checkbox.builder(type.getDescription(), this.getFont())
                    .selected(config.isEntityEnabled(type))
                    .tooltip(createTooltip(type))
                    .onValueChange(new UpdateCheckbox(type))
                    .build();
            checkboxes.add(checkbox);
        }
        return new CheckboxList(this.minecraft, this, this.layout.getContentHeight(), this.layout.getHeaderHeight(), checkboxes);
    }

    private static class CheckboxList extends ObjectSelectionList<CheckboxEntry> {

        public CheckboxList(Minecraft minecraft, ConfigScreen screen, int height, int y, List<Checkbox> checkboxes) {
            super(minecraft, screen.width, height, y, 24);
            for (Checkbox checkbox : checkboxes) {
                CheckboxEntry entry = new CheckboxEntry(this, checkbox);
                this.addEntry(entry);
            }
        }

        @Override
        protected void extractItem(final @NonNull GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a, final CheckboxEntry entry) {
            entry.checkbox.setFocused(this.entriesCanBeSelected() && this.getSelected() == entry);
            entry.extractContent(graphics, mouseX, mouseY, Objects.equals(this.getHovered(), entry), a);
        }

        @Override
        public int getRowWidth() {
            return 305;
        }

    }

    private static class CheckboxEntry extends ObjectSelectionList.Entry<CheckboxEntry> {

        private final CheckboxList parent;
        private final Checkbox checkbox;

        public CheckboxEntry(CheckboxList parent, Checkbox checkbox) {
            this.parent = parent;
            this.checkbox = checkbox;
            updatePosition();
        }

        private void updatePosition() {
            this.checkbox.setPosition(this.getContentX(), this.getContentY());
        }

        @Override @NonNull
        public Component getNarration() {
            return this.checkbox.getMessage();
        }

        @Override
        public void extractContent(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            updatePosition();
            this.checkbox.extractRenderState(graphics, mouseX, mouseY, a);
        }

        @Override
        public @NonNull ScreenRectangle getRectangle() {
            return this.checkbox.getRectangle();
        }

        @Override
        public boolean mouseClicked(final @NonNull MouseButtonEvent event, boolean doubleClick) {
            updatePosition();
            return onPress(event, this.checkbox.mouseClicked(event, doubleClick));
        }

        @Override
        public boolean mouseReleased(final @NonNull MouseButtonEvent event) {
            updatePosition();
            return this.checkbox.mouseReleased(event);
        }

        @Override
        public boolean keyPressed(final @NonNull KeyEvent event) {
            updatePosition();
            return onPress(event, this.checkbox.keyPressed(event));
        }

        @Override
        public boolean isMouseOver(final double mx, final double my) {
            this.updatePosition();
            return super.isMouseOver(mx, my);
        }

        private boolean onPress(InputWithModifiers input, boolean result) {
            if (result && input.hasShiftDown()) {
                boolean value = this.checkbox.selected();
                for (CheckboxEntry entry : this.parent.children()) {
                    if (entry.checkbox.selected() != value) entry.checkbox.onPress(input);
                }
            }
            return result;
        }
        
    }

    private class UpdateCheckbox implements Checkbox.OnValueChange {

        private final EntityType<?> entityType;

        protected UpdateCheckbox(EntityType<?> type) {
            this.entityType = type;
        }

        @Override
        public void onValueChange(@NonNull Checkbox checkbox, boolean value) {
            if (value) config.enableEntity(this.entityType);
            else config.disableEntity(this.entityType);
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parentScreen);
    }
}
