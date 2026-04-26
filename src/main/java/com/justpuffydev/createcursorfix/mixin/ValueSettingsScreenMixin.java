package com.justpuffydev.createcursorfix.mixin;

import com.mojang.blaze3d.platform.Window;
import java.nio.DoubleBuffer;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsScreen", remap = false)
public class ValueSettingsScreenMixin {
    @ModifyVariable(method = "renderWindow(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int createCursorFix$useVisibleCursorXForRender(int mouseX) {
        return visibleCursorX();
    }

    @ModifyVariable(method = "renderWindow(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private int createCursorFix$useVisibleCursorYForRender(int mouseY) {
        return visibleCursorY();
    }

    @ModifyVariable(method = "saveAndClose(DD)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private double createCursorFix$useVisibleCursorXForSave(double mouseX) {
        return visibleCursorX();
    }

    @ModifyVariable(method = "saveAndClose(DD)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private double createCursorFix$useVisibleCursorYForSave(double mouseY) {
        return visibleCursorY();
    }

    private static int visibleCursorX() {
        Minecraft minecraft = Minecraft.getInstance();

        Window window = minecraft.getWindow();
        double physicalX = visibleCursorPosition()[0];
        
        int newPhysicalX = (int) (physicalX * window.getGuiScaledWidth() / window.getScreenWidth());

        return newPhysicalX;
    }

    private static int visibleCursorY() {
        Minecraft minecraft = Minecraft.getInstance();

        Window window = minecraft.getWindow();
        double physicalY = visibleCursorPosition()[1];

        int newPhysicalY = (int) (physicalY * window.getGuiScaledHeight() / window.getScreenHeight());

        return newPhysicalY;
    }

    private static double[] visibleCursorPosition() {
        long windowHandle = Minecraft.getInstance().getWindow().getWindow();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer x = stack.mallocDouble(1);
            DoubleBuffer y = stack.mallocDouble(1);

            GLFW.glfwGetCursorPos(windowHandle, x, y);

            double[] resultAsVector = {x.get(0), y.get(0)};

            return resultAsVector;
        }
    }
}
