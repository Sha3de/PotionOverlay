package net.shade.potionoverlay.client.util

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class CustomHudRenderer {
    fun render() {
        HudRenderCallback.EVENT.register{ drawContext: DrawContext, renderTickCounter: RenderTickCounter ->
            val player = MinecraftClient.getInstance().player
            player?.statusEffects?.forEach { effect ->
                if(effect.effectType.key.isEmpty) return@forEach
                val imgName = effect.effectType.key.get().value.path
                val subDirName = effect.effectType.key.get().registry.path
                val img = Identifier.ofVanilla("textures/$subDirName/$imgName.png")
                val text = Text.translatable(effect.translationKey)

                drawContext.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    text.append(" ").append(getRomanicNumbers(effect.amplifier + 1)),
                    90,
                    90,
                    0xFFFFFF,
                    false)
                drawContext.drawTexture(img, 90, 90, 0f, 0f, 16, 16, 16, 16)
            }
        }
    }
    fun getRomanicNumbers(number: Int) : String
    {
        when(number)
        {
            1 -> return "I"
            2 -> return "II"
            3 -> return "III"
            4 -> return "IV"
            5 -> return "V"
            6 -> return "VI"
            7 -> return "VII"
            8 -> return "VIII"
            9 -> return "IX"
            10 -> return "X"
        }
        return ""
    }
}