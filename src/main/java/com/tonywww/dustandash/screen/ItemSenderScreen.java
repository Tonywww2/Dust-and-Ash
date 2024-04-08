package com.tonywww.dustandash.screen;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.network.ItemSenderSavePacket;
import com.tonywww.dustandash.network.PacketHandler;
import com.tonywww.dustandash.menu.ItemSenderContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

import static java.lang.Integer.parseInt;

public class ItemSenderScreen extends AbstractContainerScreen<ItemSenderContainerMenu> {

    private final ResourceLocation GUI = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/item_sender_gui.png");

    private final Component SLOT_ID = Component.translatable("screen.dustandash.item_sender_slot_id");
    private final Component SAVE_TEXT = Component.translatable("screen.dustandash.item_sender_save");

    private static final String ANY_SLOT_VALUE = "-1";

    private Button save;
    private EditBox targetSlot1;
    private EditBox targetSlot2;
    private EditBox targetSlot3;
    private EditBox targetSlot4;
    private EditBox[] targetSlots;

    public ItemSenderScreen(ItemSenderContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.targetSlot1.tick();
        this.targetSlot2.tick();
        this.targetSlot3.tick();
        this.targetSlot4.tick();
    }

    @Override
    protected void init() {
        super.init();
        this.subInit();
    }

    protected static final Predicate<String> NUMBER = str -> {
        if (str.isEmpty() || str.equals(ANY_SLOT_VALUE)) {
            return true;
        }
        if (StringUtils.isNumeric(str)) {
            int val = Integer.parseInt(str);
            return val >= -128 && val <= 127;
        }
        return false;

    };

    protected void subInit() {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        this.targetSlot1 = new EditBox(this.font, i + 30, j + 26, 32, 8, Component.translatable("container.item_sender"));
        this.targetSlot2 = new EditBox(this.font, i + 100, j + 26, 32, 8, Component.translatable("container.item_sender"));
        this.targetSlot3 = new EditBox(this.font, i + 30, j + 55, 32, 8, Component.translatable("container.item_sender"));
        this.targetSlot4 = new EditBox(this.font, i + 100, j + 55, 32, 8, Component.translatable("container.item_sender"));

        this.targetSlots = new EditBox[]{
                this.targetSlot1,
                this.targetSlot2,
                this.targetSlot3,
                this.targetSlot4
        };

        setUpEditBox(targetSlot1, 0);
        setUpEditBox(targetSlot2, 1);
        setUpEditBox(targetSlot3, 2);
        setUpEditBox(targetSlot4, 3);

        this.addWidget(this.targetSlot1);
        this.addWidget(this.targetSlot2);
        this.addWidget(this.targetSlot3);
        this.addWidget(this.targetSlot4);

        this.save = addRenderableWidget(
                Button.builder(
                        SAVE_TEXT,
                        this::save
                )
                        .bounds(i + 9, j + 67, 36, 12)
                        .build()
        );

    }

    private void save(Button button) {

        byte[] temp = new byte[4];

        for (int i = 0; i < 4; i++) {
            if (targetSlots[i].getValue().isEmpty() || targetSlots[i].getValue().equals(ANY_SLOT_VALUE)) {
                temp[i] = -1;

            } else {
                temp[i] = Byte.parseByte(targetSlots[i].getValue());

            }
        }

        PacketHandler.sendToServer(new ItemSenderSavePacket(this.menu.getTileEntity().getBlockPos(), temp));
        this.menu.getTileEntity().setTargetSlots(temp);

    }

    private void setUpEditBox(EditBox editBox, int index) {
        editBox.setCanLoseFocus(false);
        editBox.setMaxLength(4);
        editBox.setValue(Integer.toString(this.menu.getTileEntity().getTargetSlots()[index]));
        editBox.setEditable(true);
        editBox.setTextColor(-1);
        editBox.setFilter(NUMBER);
        editBox.setCanLoseFocus(true);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(guiGraphics, pMouseX, pMouseY);
        this.renderFg(guiGraphics, pMouseX, pMouseY, pPartialTicks);

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTicks, int pX, int pY) {
        if (this.minecraft == null) return;

        ScreenUtils.init(GUI);

        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(GUI, i, j, 0, 0, this.imageWidth, this.imageHeight + 6);

    }

    public void renderFg(GuiGraphics guiGraphics, int pX, int pY, float pPartialTicks) {
        this.targetSlot1.render(guiGraphics, pX, pY, pPartialTicks);
        this.targetSlot2.render(guiGraphics, pX, pY, pPartialTicks);
        this.targetSlot3.render(guiGraphics, pX, pY, pPartialTicks);
        this.targetSlot4.render(guiGraphics, pX, pY, pPartialTicks);

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int pX, int pY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY + 8, 4210752, false);

        guiGraphics.drawString(this.font, SLOT_ID, 30, 16, 4210752, false);
        guiGraphics.drawString(this.font, SLOT_ID, 100, 16, 4210752, false);
        guiGraphics.drawString(this.font, SLOT_ID, 30, 45, 4210752, false);
        guiGraphics.drawString(this.font, SLOT_ID, 100, 45, 4210752, false);

        guiGraphics.drawString(this.font, this.menu.getTargetX() != Integer.MIN_VALUE ? Integer.toString(this.menu.getTargetX()) : "null", 145, 30, 4210752, false);
        guiGraphics.drawString(this.font, this.menu.getTargetY() != Integer.MIN_VALUE ? Integer.toString(this.menu.getTargetY()) : "null", 145, 40, 4210752, false);
        guiGraphics.drawString(this.font, this.menu.getTargetZ() != Integer.MIN_VALUE ? Integer.toString(this.menu.getTargetZ()) : "null", 145, 50, 4210752, false);


    }
}
