package com.tfc.fabrivr.mixin;

import com.tfc.fabrivr.mixin_code.GUIMixinCode;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GenericContainerScreen.class, Screen.class, FurnaceScreen.class, TitleScreen.class, GameMenuScreen.class, InventoryScreen.class})
public class UniversalGUIMixin {
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V")
	public void onRenderPre(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		GUIMixinCode.renderPre(matrices);
	}
	
	@Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V")
	public void onRenderPost(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		GUIMixinCode.renderPost(matrices);
	}
}
