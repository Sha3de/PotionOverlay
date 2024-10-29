package net.shade.potionoverlay.client.screens

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

class PotionTimerWidgetScreen : Screen(Text.of("Potion Timer Widget")) {
    private var dragging = false
    private var dragOffsetX = 0
    private var dragOffsetY = 0
    private var widgetX = 100
    private var widgetY = 100
    private val widgetWidth = 100
    private val widgetHeight = 50

    init {
        loadWidgetPosition()
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)
        val img1 = Identifier.ofVanilla("textures/mob_effect/absorption.png")
        val img2 = Identifier.ofVanilla("textures/mob_effect/blindness.png")

        if (context == null) return
        context.fill(widgetX, widgetY, widgetX + widgetWidth, widgetY + widgetHeight, 0x80000000.toInt())
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            Text.of("Absorption II"),
            widgetX + 20,
            widgetY + 2,
            0xFFFFFF,
            false
        )
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            Text.of("Absorption II"),
            widgetX + 20,
            widgetY + 22,
            0xFFFFFF,
            false
        )
        context.drawTexture(img1, widgetX + 2, widgetY + 2, 0f, 0f, 16, 16, 16, 16)
        context.drawTexture(img2, widgetX + 2, widgetY + 22, 0f, 0f, 16, 16, 16, 16)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (mouseX >= widgetX && mouseX <= widgetX + widgetWidth &&
                mouseY >= widgetY && mouseY <= widgetY + widgetHeight
            ) {
                dragging = true
                dragOffsetX = (mouseX - widgetX).toInt()
                dragOffsetY = (mouseY - widgetY).toInt()
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            dragging = false
            saveWidgetPosition()  // Save position when dragging stops
        }
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (dragging) {
            widgetX = (mouseX - dragOffsetX).toInt()
            widgetY = (mouseY - dragOffsetY).toInt()
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    private fun saveWidgetPosition() {
        val player = MinecraftClient.getInstance().player ?: return
        val nbt = NbtCompound()
        nbt.putInt("widgetX", widgetX)
        nbt.putInt("widgetY", widgetY)
        player.saveNbt(nbt)  // Save the position to the player's NBT
    }

    private fun loadWidgetPosition() {
        val player = MinecraftClient.getInstance().player ?: return
        val nbtWidgetX = NbtCompound().getCompound("widgetX")
        val nbtWidgetY = NbtCompound().getCompound("widgetY")
        if (nbtWidgetX != null && nbtWidgetY != null) {
            widgetX = NbtCompound().getInt("widgetX")
            widgetY = NbtCompound().getInt("widgetY")
        }
    }

    override fun close() {
        MinecraftClient.getInstance().setScreen(null)  // Close the screen
    }
}