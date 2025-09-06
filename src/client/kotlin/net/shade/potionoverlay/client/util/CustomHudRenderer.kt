package net.shade.potionoverlay.client.util

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.shade.potionoverlay.client.MainClient
const val iconWidth: Int = 16

class CustomHudRenderer {
    private var tickTimer: Int = 0
    fun render() {
        HudRenderCallback.EVENT.register { drawContext: DrawContext, _: Float ->
            tickTimer++
            val player = MinecraftClient.getInstance().player
            player?.statusEffects?.forEachIndexed { index, effect ->
                //Check if person is in F1 mode
                if (MinecraftClient.getInstance().options.hudHidden) return@forEachIndexed

                if (PotionOverlayConfig.hideHUD) return@forEachIndexed

                if (effect.effectType.key.isEmpty) return@forEachIndexed
                val effectTypeKey = effect.effectType.key.get()
                val img =
                    Identifier.of(
                        effectTypeKey.value.namespace,
                        "textures/${effectTypeKey.registry.path}/${effectTypeKey.value.path}.png"
                    )
                if (img == null) return@forEachIndexed

                val text = Text.translatable(effect.translationKey)
                if (effect.duration == StatusEffectInstance.INFINITE) {
                    renderTimer(
                        drawContext,
                        Text.of("∞"),
                        index,
                        false
                    )
                } else if (PotionOverlayConfig.blinkWhenUnderATime && (effect.duration <= (20 * PotionOverlayConfig.timeWhenStartBlinking))) {
                    if (tickTimer % 80 > 40) {
                        renderTimer(effect, drawContext, index)
                    }
                } else {
                    renderTimer(effect, drawContext, index)
                }
                val fullText = text.append(" ").append(getRomanticNumbers(effect.amplifier + 1))

                drawEffectIconAndEffectName(drawContext, img, text, fullText, index)
            }
        }
    }

    private fun drawEffectIconAndEffectName(
        drawContext: DrawContext,
        img: Identifier,
        text: Text,
        fullText: Text,
        index: Int
    ) {

        var textX = 0
        var imgX = 0

        if (PotionOverlayConfig.changeIconAndTextPosition &&
            PotionOverlayConfig.widgetX >= (MinecraftClient.getInstance().window.scaledWidth / 2)
        ) {
            imgX = PotionOverlayConfig.widgetX + (MainClient.widgetScreen.widgetWidth - iconWidth - 2)
            textX =
                PotionOverlayConfig.widgetX + (MainClient.widgetScreen.widgetWidth - iconWidth - 2) - (getTextWidth(text.string.trim()) + 2)
        } else {
            textX = PotionOverlayConfig.widgetX + 20
            imgX = PotionOverlayConfig.widgetX + 2
        }

        if (PotionOverlayConfig.widgetY >= (MinecraftClient.getInstance().window.scaledHeight / 2)) {
            val y =
                ((PotionOverlayConfig.widgetY + MainClient.widgetScreen.widgetHeight) - iconWidth) - (2 + (index * 20))
            drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                fullText,
                textX,
                y,
                PotionOverlayConfig.textColor.rgb,
                PotionOverlayConfig.renderShadow
            )

            drawContext.drawTexture(
                img,
                imgX,
                y,
                0f, 0f, 16, 16, 16, 16
            )
        } else {
            val y = PotionOverlayConfig.widgetY + ((index * 20))
            drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                fullText,
                textX,
                y,
                PotionOverlayConfig.textColor.rgb,
                PotionOverlayConfig.renderShadow
            )

            drawContext.drawTexture(
                img,
                imgX,
                y,
                0f, 0f, 16, 16, 16, 16
            )
        }
    }

    private fun renderTimer(effect: StatusEffectInstance, drawContext: DrawContext, index: Int) {
        if (effect.duration <= 20 * PotionOverlayConfig.timeWhenChangeColor) {
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
                index,
                false
            )
        }

    }


    private fun renderTimer(drawContext: DrawContext, text: Text, index: Int, changeColor: Boolean) {
        var color: Int;
        if (PotionOverlayConfig.changeColor && changeColor) {
            color = PotionOverlayConfig.timerColor.rgb
        } else {
            color = 0xFFFFFF;
        }

        val textX = if(PotionOverlayConfig.changeIconAndTextPosition &&
            PotionOverlayConfig.widgetX >= (MinecraftClient.getInstance().window.scaledWidth / 2)) {
            PotionOverlayConfig.widgetX + (MainClient.widgetScreen.widgetWidth - iconWidth - 2) - (getTextWidth(text.string.trim()) + 2)
        } else {
            PotionOverlayConfig.widgetX + 20
        }

        if (PotionOverlayConfig.widgetY >= (MinecraftClient.getInstance().window.scaledHeight / 2)) {
            drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                text,
                textX,
                ((PotionOverlayConfig.widgetY + MainClient.widgetScreen.widgetHeight) - 16) - (-6 + (index * 20)),
                color,
                PotionOverlayConfig.renderShadow
            )
        } else {
            drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                text,
                textX,
                PotionOverlayConfig.widgetY + (8 + (index * 20)),
                color,
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

    private fun getTextWidth(text: String?): Int {
        val client = MinecraftClient.getInstance()
        val textRenderer: TextRenderer = client.textRenderer

        return textRenderer.getWidth(text)
    }
}