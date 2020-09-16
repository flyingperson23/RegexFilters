package flyingperson.regexfilters;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class RegexContainerGUI extends GuiContainer {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private GuiTextField expression;
    private boolean flagError = false;
    private int flagErrorCounter = 0;

    RegexTileEntity te;

    private static final ResourceLocation backgroundOverlay = new ResourceLocation(RegexFilters.MODID, "textures/gui/gui_base_overlay.png");
    private static final ResourceLocation overlayChangeAlpha = new ResourceLocation(RegexFilters.MODID, "textures/gui/gui_change_alpha.png");
    private static final ResourceLocation greenSquare = new ResourceLocation(RegexFilters.MODID, "textures/gui/green.png");
    private static final ResourceLocation redSquare = new ResourceLocation(RegexFilters.MODID, "textures/gui/red.png");

    public RegexContainerGUI(RegexTileEntity tileEntity, RegexContainer container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
        this.te = tileEntity;

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (te.exp.equals(RegexFilters.DUMMY_STRING)) RegexFilters.INSTANCE.sendToServer(new MessageRequestUpdate(te.getPos()));

        this.inventorySlots.inventorySlots.forEach((slot -> {
            if (slot != null) {
                if (slot.getHasStack()) {
                    if (!slot.getStack().isEmpty()) {
                        ItemStack stack = slot.getStack();
                        AtomicBoolean flag = new AtomicBoolean(false);
                        RegexTileEntity.getNames(stack).forEach((i) -> {
                            try {
                                if (Pattern.compile(te.exp).matcher(i).find()) flag.set(true);
                            } catch(Exception ignored) {
                                RegexFilters.logger.log(Level.ERROR, "Pattern invalid");
                                te.exp = RegexFilters.DUMMY_STRING;
                            }
                        });
                        GlStateManager.pushMatrix();
                        GlStateManager.enableBlend();
                        GlStateManager.enableAlpha();
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
                        if (flag.get()) {
                            mc.getTextureManager().bindTexture(greenSquare);
                        } else {
                            mc.getTextureManager().bindTexture(redSquare);
                        }
                        drawTexturedModalRect(guiLeft+slot.xPos, guiTop+slot.yPos, 0, 0, 16 ,16);
                        GlStateManager.popMatrix();
                    }
                }
            }
        }));

        if (flagError) {
            float alpha = (float) ((-1*((double) 1/(double) 400))*Math.pow((flagErrorCounter+0.1)*0.1, 2)+1);
            if (alpha <= 0) {
                flagError = false;
                flagErrorCounter = 0;
            } else {
                GlStateManager.color(0.66666666f, 0, 0, alpha);
                fontRenderer.posX = guiLeft+60;
                fontRenderer.posY = guiTop+28;
                fontRenderer.renderStringAtPos("Invalid String", false);
                flagErrorCounter++;
            }
        }
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(backgroundOverlay);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        GlStateManager.color(1.0f, 1.0f, 1.0f, (float) (0.25*Math.sin(te.counter*0.05)+0.5));
        mc.getTextureManager().bindTexture(overlayChangeAlpha);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        expression.drawTextBox();
        
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        expression.updateCursorCounter();
        if (te.exp.equals(RegexFilters.DUMMY_STRING)) RegexFilters.INSTANCE.sendToServer(new MessageRequestUpdate(te.getPos()));
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);

        expression.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public void initGui() {
        super.initGui();
        expression = new GuiTextField(1, fontRenderer, guiLeft + 10, guiTop + 40, 160, 12) {
            @Override
            public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
                boolean flag = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
                if (!flag) {

                    try {
                        Pattern.compile(expression.getText());
                        expression.setFocused(false);
                        if (expression.getText().equals("")) te.exp = RegexFilters.DUMMY_STRING;
                        else te.exp = expression.getText();
                    } catch(Exception ignored) {
                        expression.setFocused(false);
                        expression.setText("");
                        te.exp = RegexFilters.DUMMY_STRING;
                        flagErrorCounter = 0;
                        flagError = true;
                    }

                    RegexFilters.INSTANCE.sendToServer(new MessageSetExpression(te.exp, te.getPos()));
                }
                if (mouseButton == 1 && flag) {
                    te.exp = RegexFilters.DUMMY_STRING;
                    expression.setText("");
                    RegexFilters.INSTANCE.sendToServer(new MessageSetExpression(te.exp, te.getPos()));
                    return super.mouseClicked(mouseX, mouseY, 0);
                } else return super.mouseClicked(mouseX, mouseY, mouseButton);
            }
        };

        expression.setText(te.exp.equals(RegexFilters.DUMMY_STRING) ? "" : te.exp);
        RegexFilters.INSTANCE.sendToServer(new MessageRequestUpdate(te.getPos()));

        expression.setMaxStringLength(200);
    }

    @Override
    public void keyTyped(char c, int i) throws IOException {
        if (!expression.textboxKeyTyped(c, i)) super.keyTyped(c, i);
        if (i == Keyboard.KEY_ESCAPE || i == Keyboard.KEY_NUMPADENTER || i == Keyboard.KEY_RETURN) {
            try {
                Pattern.compile(expression.getText());
                expression.setFocused(false);
                if (expression.getText().equals("")) te.exp = RegexFilters.DUMMY_STRING;
                else te.exp = expression.getText();
            } catch(Exception ignored) {
                expression.setFocused(false);
                expression.setText("");
                te.exp = RegexFilters.DUMMY_STRING;
                flagErrorCounter = 0;
                flagError = true;
            }
        }
        if (i == Keyboard.KEY_TAB) expression.setFocused(true);

        RegexFilters.INSTANCE.sendToServer(new MessageSetExpression(te.exp, te.getPos()));
    }

    @Override
    protected void renderToolTip(ItemStack stack, int x, int y) {

        AtomicBoolean flag = new AtomicBoolean(false);
        RegexTileEntity.getNames(stack).forEach((i) -> {
            try {
                if (Pattern.compile(te.exp).matcher(i).find()) flag.set(true);
            } catch(Exception ignored) {
                RegexFilters.logger.log(Level.ERROR, "Pattern invalid");
                te.exp = RegexFilters.DUMMY_STRING;
            }
        });


        FontRenderer font = stack.getItem().getFontRenderer(stack);
        net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
        List<String> tip = this.getItemToolTip(stack);
        tip.set(0, tip.get(0).concat(" - "+ (flag.get() ? "Valid" : "Invalid")));
        tip.add("");
        tip.add("Item's searchable names:");
        tip.addAll(RegexTileEntity.getNames(stack));
        this.drawHoveringText(tip, x, y, (font == null ? fontRenderer : font));
        net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();

    }

}
