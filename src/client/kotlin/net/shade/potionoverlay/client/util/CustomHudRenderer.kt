package net.shade.potionoverlay.client.util

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.shade.potionoverlay.client.MainClient

class CustomHudRenderer {
    private var tickTimer: Int = 0
    fun render() {
        HudRenderCallback.EVENT.register { drawContext: DrawContext, _: RenderTickCounter ->
            tickTimer++
            val player = MinecraftClient.getInstance().player
            player?.statusEffects?.forEachIndexed { index, effect ->

                if (effect.effectType.key.isEmpty) return@forEachIndexed
                val effectTypeKey = effect.effectType.key.get()
                val img =
                    Identifier.ofVanilla("textures/${effectTypeKey.registry.path}/${effectTypeKey.value.path}.png")
                val text = Text.translatable(effect.translationKey)
                if (effect.duration == StatusEffectInstance.INFINITE) {
                    renderTimer(
                        drawContext,
                        Text.of("∞"),
                        index
                    )
                } else if (PotionOverlayConfig.blinkWhenUnderATime && (effect.duration <= 20 * PotionOverlayConfig.timeWhenStartBlinking)) {
                        if ((tickTimer % 80) > 40) {
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
                                index
                            )
                    }
                } else {
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
                        index
                    )
                }
                drawContext.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    text.append(" ").append(getRomanticNumbers(effect.amplifier + 1)),
                    PotionOverlayConfig.widgetX + 20,
                    ((PotionOverlayConfig.widgetY + MainClient.widgetScreen.widgetHeight) - 16) - (2 + (index * 20)),
                    0xFFFFFF,
                    PotionOverlayConfig.renderShadow
                )

                drawContext.drawTexture(
                    img,
                    PotionOverlayConfig.widgetX + 2,
                    ((PotionOverlayConfig.widgetY + MainClient.widgetScreen.widgetHeight) - 16) - (2 + (index * 20)),
                    0f, 0f, 16, 16, 16, 16
                )
            }
        }
    }

    private fun renderTimer(drawContext: DrawContext, text: Text, index: Int) {
        drawContext.drawText(
            MinecraftClient.getInstance().textRenderer,
            text,
            PotionOverlayConfig.widgetX  + 20,
            ((PotionOverlayConfig.widgetY + MainClient.widgetScreen.widgetHeight) - 16) - (-6 + (index * 20)),
            0xFFFFFF,
            PotionOverlayConfig.renderShadow
        )
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