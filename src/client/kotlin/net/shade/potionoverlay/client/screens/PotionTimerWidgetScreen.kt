package net.shade.potionoverlay.client.screens

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.shade.potionoverlay.Main
import net.shade.potionoverlay.client.util.PotionOverlayConfig
import org.lwjgl.glfw.GLFW

@Environment(EnvType.CLIENT)
class PotionTimerWidgetScreen : Screen(Text.of("Potion Timer Widget")) {
    private var dragging = false
    private var dragOffsetX = 0
    private var dragOffsetY = 0

    val widgetWidth = 120
        get() = field

    val widgetHeight = 40
        get() = field

    init {
        loadWidgetPosition()
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        if ((PotionOverlayConfig.widgetX - (widgetWidth * 1.5)) > MinecraftClient.getInstance().window.scaledWidth) PotionOverlayConfig.widgetX =
            MinecraftClient.getInstance().window.scaledWidth - widgetWidth
        if ((PotionOverlayConfig.widgetY - (widgetHeight * 1.5)) > MinecraftClient.getInstance().window.scaledHeight) PotionOverlayConfig.widgetY =
            MinecraftClient.getInstance().window.scaledHeight - widgetHeight
        super.render(context, mouseX, mouseY, delta)
        val img1 = Identifier.ofVanilla("textures/mob_effect/absorption.png")
        val img2 = Identifier.ofVanilla("textures/mob_effect/blindness.png")
        val reset_Button = Identifier.of(Main.MOD_ID, "textures/gui/reset_button.png")

        if (context == null) return
        context.fill(
            PotionOverlayConfig.widgetX,
            PotionOverlayConfig.widgetY,
            PotionOverlayConfig.widgetX + widgetWidth,
            PotionOverlayConfig.widgetY + widgetHeight,
            0x80000000.toInt()
        )
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            Text.of("Absorption II"),
            PotionOverlayConfig.widgetX + 20,
            PotionOverlayConfig.widgetY + 2,
            0xFFFFFF,
            false
        )
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            Text.of("Blindness IV"),
            PotionOverlayConfig.widgetX + 20,
            PotionOverlayConfig.widgetY + 22,
            0xFFFFFF,
            false
        )
        context.drawTexture(
            img1,
            PotionOverlayConfig.widgetX + 2,
            PotionOverlayConfig.widgetY + 2,
            0f,
            0f,
            16,
            16,
            16,
            16
        )
        context.drawTexture(
            img2,
            PotionOverlayConfig.widgetX + 2,
            PotionOverlayConfig.widgetY + 22,
            0f,
            0f,
            16,
            16,
            16,
            16
        )
        context.drawTexture(
            reset_Button,
            MinecraftClient.getInstance().window.scaledWidth - 20,
            4,
            0f,
            0f,
            16,
            16,
            16,
            16
        )

        context.fill(
            MinecraftClient.getInstance().window.scaledWidth - 20,
            4,
            MinecraftClient.getInstance().window.scaledWidth - 4,
            20,
            0x80000000.toInt()
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (mouseX >= MinecraftClient.getInstance().window.scaledWidth - 20 && mouseX <= MinecraftClient.getInstance().window.scaledWidth - 4 &&
                mouseY >= 4 && mouseY <= 20
            ) {
                PotionOverlayConfig.widgetX = 0
                PotionOverlayConfig.widgetY = 0
                saveWidgetPosition()
            } else if (mouseX >= PotionOverlayConfig.widgetX && mouseX <= PotionOverlayConfig.widgetX + widgetWidth &&
                mouseY >= PotionOverlayConfig.widgetY && mouseY <= PotionOverlayConfig.widgetY + widgetHeight
            ) {
                dragging = true
                dragOffsetX = (mouseX - PotionOverlayConfig.widgetX).toInt()
                dragOffsetY = (mouseY - PotionOverlayConfig.widgetY).toInt()
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            dragging = false
            saveWidgetPosition()
        }
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (dragging) {
            PotionOverlayConfig.widgetX = (mouseX - dragOffsetX).toInt()
            PotionOverlayConfig.widgetY = (mouseY - dragOffsetY).toInt()
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    private fun saveWidgetPosition() {
        PotionOverlayConfig.widgetX = PotionOverlayConfig.widgetX
        PotionOverlayConfig.widgetY = PotionOverlayConfig.widgetY
        PotionOverlayConfig.HANDLER.save()
    }

    private fun loadWidgetPosition() {
        PotionOverlayConfig.HANDLER.load()
        PotionOverlayConfig.widgetX = PotionOverlayConfig.widgetX
        PotionOverlayConfig.widgetY = PotionOverlayConfig.widgetY
    }

    override fun close() {
        MinecraftClient.getInstance().setScreen(null)
    }
}