package net.shade.potionoverlay.client.integration

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.ColorControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import net.shade.potionoverlay.client.MainClient
import net.shade.potionoverlay.client.util.PotionOverlayConfig
import java.awt.Color


class ModMenuIntegration : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { it ->
            YetAnotherConfigLib.createBuilder()
                .title(Text.of("Potion Overlay Config"))
                .category(ConfigCategory.createBuilder()
                    .name(Text.of("Widget"))
                    .group(OptionGroup.createBuilder()
                        .name(Text.of("Position"))
                        .option(Option.createBuilder<Int>()
                            .name(Text.of("X cordination"))
                            .description(OptionDescription.of(Text.of("The X position of the widget")))
                            .binding(
                                PotionOverlayConfig.widgetX,
                                { PotionOverlayConfig.widgetX },
                                { newVal -> PotionOverlayConfig.widgetX = newVal }
                            )
                            .controller { option: Option<Int> ->
                                IntegerSliderControllerBuilder.create(option).range(0,MinecraftClient.getInstance().window.scaledWidth - MainClient.widgetScreen.widgetWidth).step(1)
                            }
                            .build()
                        )
                        .option(Option.createBuilder<Int>()
                            .name(Text.of("Y cordination"))
                            .description(OptionDescription.of(Text.of("The Y position of the widget")))
                            .binding(
                                PotionOverlayConfig.widgetY,
                                { PotionOverlayConfig.widgetY },
                                { newVal -> PotionOverlayConfig.widgetY = newVal }
                            )
                            .controller { option: Option<Int> ->
                                IntegerSliderControllerBuilder.create(option).range(0,MinecraftClient.getInstance().window.scaledHeight - MainClient.widgetScreen.widgetHeight).step(1)
                            }
                            .build()
                        )
                        .option(Option.createBuilder<Boolean>()
                            .name(Text.of("Render Shadow"))
                            .description(OptionDescription.of(Text.of("Render the shadow of the text")))
                            .binding(
                                PotionOverlayConfig.renderShadow,
                                { PotionOverlayConfig.renderShadow },
                                { newVal -> PotionOverlayConfig.renderShadow = newVal }
                            )
                            .controller { option: Option<Boolean> ->
                                BooleanControllerBuilder.create(option).yesNoFormatter()
                            }
                            .build()
                        )
                        .option(Option.createBuilder<Color>()
                            .name(Text.of("Render Shadow"))
                            .description(OptionDescription.of(Text.of("Render the shadow of the text")))
                            .binding(
                                PotionOverlayConfig.textColor,
                                { PotionOverlayConfig.textColor },
                                { newVal -> PotionOverlayConfig.textColor = newVal }
                            )
                            .controller(ColorControllerBuilder::create)
                            .build()
                        )
                        .option(Option.createBuilder<Boolean>()
                            .name(Text.of("Render Blinking effect"))
                            .description(OptionDescription.of(Text.of("If the text should blink when under a certain time")))
                            .binding(
                                PotionOverlayConfig.blinkWhenUnderATime,
                                { PotionOverlayConfig.blinkWhenUnderATime },
                                { newVal -> PotionOverlayConfig.blinkWhenUnderATime = newVal }
                            )
                            .controller { option: Option<Boolean> ->
                                BooleanControllerBuilder.create(option).yesNoFormatter()
                            }
                            .build()
                        )
                        .option(Option.createBuilder<Int>()
                            .name(Text.of("How many seconds left to start blinking"))
                            .description(OptionDescription.of(Text.of("On how many seconds left should the text start blinking")))
                            .binding(
                                0,
                                { PotionOverlayConfig.timeWhenStartBlinking },
                                { newVal -> PotionOverlayConfig.timeWhenStartBlinking = newVal }
                            )
                            .controller { option: Option<Int> ->
                                IntegerSliderControllerBuilder.create(option).range(1,200).step(1)
                            }
                            .available(PotionOverlayConfig.blinkWhenUnderATime)
                            .build()
                        )
                        .build()
                    )
                    .build()
                )
                .build()
                .generateScreen(it)
        }
    }
}