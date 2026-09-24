package com.skirazavr.panoramic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import com.mojang.blaze3d.platform.InputConstants;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod(value = "panoramic", dist = Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public class Panoramic {
	public static final KeyMapping PANORAMA_KEY = new KeyMapping("key.panoramic.create", InputConstants.Type.KEYBOARD,
		InputConstants.KEY_F9, KeyMapping.Category.MISC);

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		while (PANORAMA_KEY.consumeClick()) {
			if (mc.level == null || mc.player == null) {
				return;
			}
			Path folder = mc.gameDirectory.toPath().resolve("panorama");
			try {
				Files.createDirectories(folder);
			} catch (IOException e) {
				return;
			}
			mc.grabPanoramixScreenshot(folder.toFile());
			Component message = Component.translatable("link.panoramic.open")
					.setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withUnderlined(true)
							.withClickEvent(new ClickEvent.OpenFile(folder.toAbsolutePath())));
			mc.player.sendSystemMessage(Component.translatable("message.panoramic.saved").append(message));
		}
	}

	@SubscribeEvent
	public static void registerBindings(RegisterKeyMappingsEvent event) {
		event.register(PANORAMA_KEY);
	}
}
