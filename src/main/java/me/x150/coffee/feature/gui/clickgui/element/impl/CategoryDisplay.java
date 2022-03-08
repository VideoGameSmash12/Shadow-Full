package me.x150.coffee.feature.gui.clickgui.element.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import me.x150.coffee.feature.gui.clickgui.ClickGUI;
import me.x150.coffee.feature.gui.clickgui.element.Element;
import me.x150.coffee.feature.gui.clickgui.theme.Theme;
import me.x150.coffee.feature.module.Module;
import me.x150.coffee.feature.module.ModuleRegistry;
import me.x150.coffee.feature.module.ModuleType;
import me.x150.coffee.helper.font.FontRenderers;
import me.x150.coffee.helper.font.adapter.impl.ClientFontRenderer;
import me.x150.coffee.helper.render.ClipStack;
import me.x150.coffee.helper.render.Rectangle;
import me.x150.coffee.helper.render.Renderer;
import me.x150.coffee.helper.util.Transitions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class CategoryDisplay extends Element {
    static final ClientFontRenderer cfr = FontRenderers.getCustomSize(20);
    final List<ModuleDisplay> md = new ArrayList<>();
    final ModuleType mt;
    boolean selected = false;
    double scroll = 0;
    double smoothScroll = 0;

    boolean open = true;
    double openAnim = 1;

    public CategoryDisplay(double x, double y, ModuleType mt) {
        super(x, y, 100, 500);
        this.mt = mt;
        for (Module module : ModuleRegistry.getModules()) {
            if (module.getModuleType() == mt) {
                ModuleDisplay md1 = new ModuleDisplay(0, 0, module);
                md.add(md1);
            }
        }
    }

    @Override
    public boolean clicked(double x, double y, int button) {
        double r = 5;
        if (x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + headerHeight()) {
            if (button == 0) {
                selected = true;
                return true;
            } else if (button == 1) {
                open = !open;
            }
        } else if (x >= this.x && x < this.x + width && y >= this.y + headerHeight() && y < this.y + this.height - r) {
            for (ModuleDisplay moduleDisplay : getModules()) {
                if (moduleDisplay.clicked(x, y - smoothScroll, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean dragged(double x, double y, double deltaX, double deltaY, int button) {
        if (selected) {
            this.x += deltaX;
            this.y += deltaY;
            return true;
        } else {
            for (ModuleDisplay moduleDisplay : getModules()) {
                if (moduleDisplay.dragged(x, y - smoothScroll, deltaX, deltaY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean released() {
        selected = false;
        for (ModuleDisplay moduleDisplay : getModules()) {
            moduleDisplay.released();
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keycode, int modifiers) {
        for (ModuleDisplay moduleDisplay : getModules()) {
            if (moduleDisplay.keyPressed(keycode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    List<ModuleDisplay> getModules() {
        return md.stream().filter(moduleDisplay -> moduleDisplay.module.getName().toLowerCase().startsWith(ClickGUI.instance().searchTerm.toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public boolean scroll(double mouseX, double mouseY, double amount) {

        if (amount == 0 || inBounds(mouseX, mouseY)) {
            scroll += amount * 10;
            double contentHeight = getModules().stream().map(ModuleDisplay::getHeight).reduce(Double::sum).orElse(0d);
            double viewerHeight = Math.min(contentHeight, 200);
            double elScroll = contentHeight - viewerHeight;
            scroll = MathHelper.clamp(scroll, -elScroll, 0);
            smoothScroll = MathHelper.clamp(smoothScroll, -elScroll, 0);
            return true;
        }
        return super.scroll(mouseX, mouseY, amount);
    }

    float headerHeight() {
        float padding = 3;
        return padding + cfr.getFontHeight() + padding;
    }

    @Override
    public void render(MatrixStack matrices, double mouseX, double mouseY, double scrollBeingUsed) {
        scroll(mouseX, mouseY, 0);
        Theme theme = ClickGUI.theme;
        double openAnim = this.openAnim < 0.5
                ? (1 - sqrt(1 - pow(2 * this.openAnim, 2))) / 2
                : (sqrt(1 - pow(-2 * this.openAnim + 2, 2)) + 1) / 2;
        //        Renderer.R2D.fill(matrices, theme.getHeader(), x, y, x + width, y + headerHeight());
        double r = 5;
        double hheight = headerHeight();
        double texPad = 4;
        double texDim = hheight - texPad * 2;

        double modHeight = getModules().stream().map(ModuleDisplay::getHeight).reduce(Double::sum).orElse(0d);
        double modHeightUnclamped = modHeight;
        modHeight = Math.min(modHeight, 200);
        modHeight = Math.min(modHeight, modHeight * openAnim); // looks weird but works

        this.height = headerHeight() + modHeight; // pre calc height
        if (modHeight != 0) {
            height += r * openAnim;
        }
        Renderer.R2D.renderRoundedQuad(matrices, theme.getHeader(), x, y, x + width, y + this.height, r, 20);


        RenderSystem.setShaderTexture(0, mt.getTex());
        Renderer.R2D.renderTexture(matrices, x + texPad, y + texPad, texDim, texDim, 0, 0, texDim, texDim, texDim, texDim);
        //        cfr.drawCenteredString(matrices,mt.getName(),x+texPad+texDim+texPad,y+headerHeight()/2d-cfr.getFontHeight()/2d,0xFFFFFF);
        cfr.drawCenteredString(matrices, mt.getName(), x + width / 2d, y + headerHeight() / 2d - cfr.getFontHeight() / 2d, 0xFFFFFF);
        double ct = 1;
        double cw = 6;
        matrices.push();
        matrices.translate(x + width - texPad - cw, y + headerHeight() / 2d - ct / 2d, 0);
        matrices.push();
        matrices.multiply(new Quaternion(0f, 0f, (float) openAnim * 90f, true));
        Renderer.R2D.renderQuad(matrices, Color.WHITE, -cw / 2d, -ct / 2d, cw / 2d, ct / 2d);
        matrices.pop();
        matrices.multiply(new Quaternion(0f, 0f, (float) openAnim * 180f, true));
        Renderer.R2D.renderQuad(matrices, Color.WHITE, -cw / 2d, -ct / 2d, cw / 2d, ct / 2d);
        matrices.pop();
        if (openAnim != 0) {
            // rounding the height in the final param makes it more smooth, otherwise scissor will do something with the start y and it will rattle like shit
            ClipStack.globalInstance.addWindow(matrices, new Rectangle(x, y + headerHeight(), x + width, y + Math.round(this.height) - (modHeight != 0 ? r : 0)));
            double y = headerHeight();
            matrices.push();
            matrices.translate(0, smoothScroll, 0);
            if (!(mouseX >= x && mouseX < x + width && mouseY >= this.y + headerHeight() && mouseY < this.y + this.height - (modHeight != 0 ? r : 0))) {
                mouseX = -1000;
                mouseY = -1000;
            }
            for (ModuleDisplay moduleDisplay : getModules()) {
                moduleDisplay.setX(this.x);
                moduleDisplay.setY(this.y + y);
                if (moduleDisplay.getY() + smoothScroll > this.y + height) continue;

                moduleDisplay.render(matrices, mouseX, mouseY - smoothScroll, scrollBeingUsed);
                y += moduleDisplay.getHeight();
            }
            matrices.pop();
            ClipStack.globalInstance.popWindow();
            if (modHeightUnclamped > 200) {
                double elScroll = modHeightUnclamped - modHeight;
                double scrollIndex = (smoothScroll * -1) / elScroll;

                double ratio = modHeight / modHeightUnclamped;
                double scrollbarHeight = modHeight - 2;
                double scrollerHeight = ratio * scrollbarHeight;
                double sbW = 2;
                Renderer.R2D.renderRoundedQuad(matrices, new Color(20, 20, 20, 150), x + width - 1 - sbW, this.y + headerHeight() + 1, x + width - 1, this.y + headerHeight() + 1 + scrollbarHeight, sbW / 2d, 20);
                double scrollerStartY = MathHelper.lerp(scrollIndex, this.y + headerHeight() + 1, this.y + headerHeight() + 1 + scrollbarHeight - scrollerHeight);
                Renderer.R2D.renderRoundedQuad(matrices, new Color(40, 40, 40, 200), x + width - 1 - sbW, scrollerStartY, x + width - 1, scrollerStartY + scrollerHeight, sbW / 2d, 20);
            }
        }
        //        FontRenderers.getRenderer().drawCenteredString(matrices, getModules().size() + " modules", this.x + this.width / 2d, this.y + this.height - 1 - FontRenderers.getRenderer().getMarginHeight(), 0xFFFFFF);
    }

    @Override
    public void tickAnim() {
        double oaDelta = 0.02;
        if (!open) oaDelta *= -1;
        openAnim += oaDelta;
        openAnim = MathHelper.clamp(openAnim, 0, 1);
        for (ModuleDisplay moduleDisplay : getModules()) {
            moduleDisplay.tickAnim();
        }
        smoothScroll = Transitions.transition(smoothScroll, scroll, 7, 0);
    }

    @Override
    public boolean charTyped(char c, int mods) {
        for (ModuleDisplay moduleDisplay : getModules()) {
            if (moduleDisplay.charTyped(c, mods)) {
                return true;
            }
        }
        return false;
    }
}
