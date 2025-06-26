package net.shade.potionoverlay.client.util

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.potion.Potion
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.shade.potionoverlay.client.MainClient
var ticktimer : Int = 0
class CustomHudRenderer {
    fun render() {
        HudRenderCallback.EVENT.register { drawContext: DrawContext, _: RenderTickCounter ->
            ticktimer++
            val player = MinecraftClient.getInstance().player
            player?.statusEffects?.forEachIndexed { index, effect ->
                //Check if person is in F1 mode
                if(MinecraftClient.getInstance().options.hudHidden) return@forEachIndexed

                if(PotionOverlayConfig.hideHUD) return@forEachIndexed

                if (effect.effectType.key.isEmpty) return@forEachIndexed
                val effectTypeKey = effect.effectType.key.get()
                val img =
                    Identifier.ofVanilla("textures/${effectTypeKey.registry.path}/${effectTypeKey.value.path}.png")
                val text = Text.translatable(effect.translationKey)
                if (effect.duration == StatusEffectInstance.INFINITE) {
                    renderTimer(
                        drawContext,
                        Text.of("∞"),
                        index,
                        false
                    )
                } else if (PotionOverlayConfig.blinkWhenUnderATime && (effect.duration <= (20 * PotionOverlayConfig.timeWhenStartBlinking))) {
                        if (ticktimer % 80 > 40) {
                            renderTimer(effect,drawContext,index)
                        }
                } else {
                    renderTimer(effect,drawContext,index)
                }
                if(PotionOverlayConfig.widgetY >= (MinecraftClient.getInstance().window.scaledHeight / 2) ) {
                    drawContext.drawText(
                        MinecraftClient.getInstance().textRenderer,
                        text.append(" ").append(getRomanticNumbers(effect.amplifier + 1)),
                        PotionOverlayConfig.widgetX + 20,
                        ((PotionOverlayConfig.widgetY + MainClient.widgetScreen.widgetHeight) - 16) - (2 + (index * 20)),
                        PotionOverlayConfig.textColor.rgb,
                        PotionOverlayConfig.renderShadow
                    )

                    drawContext.drawTexture(
                        img,
                        PotionOverlayConfig.widgetX + 2,
                        ((PotionOverlayConfig.widgetY + MainClient.widgetScreen.widgetHeight) - 16) - (2 + (index * 20)),
                        0f, 0f, 16, 16, 16, 16
                    )
                }
                else
                {
                    drawContext.drawText(
                        MinecraftClient.getInstance().textRenderer,
                        text.append(" ").append(getRomanticNumbers(effect.amplifier + 1)),
                        PotionOverlayConfig.widgetX + 20,
                        PotionOverlayConfig.widgetY + ((index * 20)),
                        PotionOverlayConfig.textColor.rgb,
                        PotionOverlayConfig.renderShadow
                    )

                    drawContext.drawTexture(
                        img,
                        PotionOverlayConfig.widgetX + 2,
                        PotionOverlayConfig.widgetY + ((index * 20)),
                        0f, 0f, 16, 16, 16, 16
                    )
                }
            }
        }
    }

    private fun renderTimer(effect: StatusEffectInstance,drawContext: DrawContext, index: Int)
    {
        if(effect.duration <= 20 * PotionOverlayConfig.timeWhenChangeColor){
            renderTimer(
                drawContext,
                Text.of(
                    String.format(
                        "%02d:%02d:%02d",
                        effect.duration / 20 / 60 / 60,
                        effect.duration / 20 / 60 % 60,
                        effect.duration / 20 % 60
                    )
                ),
                index,
                true
            )
        }
        else {
            renderTimer(
                drawContext,
                Text.of(
                    String.format(
                        "%02d:%02d:%02d",
                        effect.duration / 20 / 60 / 60,
                        effect.duration / 20 / 60 % 60,
                        effect.duration / 20 % 60
                    )
                ),
                index,
                false
            )
        }

    }

    private fun renderTimer(drawContext: DrawContext, text: Text, index: Int, changeColor: Boolean) {
        var color: Int;
        if(PotionOverlayConfig.changeColor && changeColor)
        {
            color = PotionOverlayConfig.timerColor.rgb
        }
        else {
            color = 0xFFFFFF;
        }


        if(PotionOverlayConfig.widgetY >= (MinecraftClient.getInstance().window.scaledHeight / 2) )
        {
            drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                text,
                PotionOverlayConfig.widgetX  + 20,
                ((PotionOverlayConfig.widgetY + MainClient.widgetScreen.widgetHeight) - 16) - (-6 + (index * 20)),
                color,
                PotionOverlayConfig.renderShadow
            )
        }
        else
        {
            drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                text,
                PotionOverlayConfig.widgetX  + 20,
                PotionOverlayConfig.widgetY + (8 + (index * 20)),
                color ,
                PotionOverlayConfig.renderShadow
            )
        }
    }

    private fun getRomanticNumbers(number: Int): String {
        when (number) {
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