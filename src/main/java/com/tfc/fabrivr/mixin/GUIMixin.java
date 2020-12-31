package com.tfc.fabrivr.mixin;

import com.tfc.fabrivr.AngleHelper;
import com.tfc.fabrivr.client.FabriVROculus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class GUIMixin {
	@Shadow
	public int width;
	@Shadow
	public int height;
	@Shadow
	@Nullable
	protected MinecraftClient client;
	Vec3d openedAt;
	Vec3d openedAngle;
	
	@Inject(at = @At("TAIL"), method = "<init>")
	public void init(Text title, CallbackInfo ci) {
		try {
			openedAngle = AngleHelper.toEulers(new Quaternion(
					FabriVROculus.headQuat.x(),
					FabriVROculus.headQuat.y(),
					FabriVROculus.headQuat.z(),
					FabriVROculus.headQuat.w()
			));
			openedAt = new Vec3d(
					MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().x,
					FabriVROculus.headPos.y() + MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().y,
					MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().z
			);
		} catch (Throwable ignored) {
		}
	}
	
	public Vec3d getOpenedAt() {
		return openedAt;
	}
	
	public Vec3d getOpenedAngle() {
		return openedAngle;
	}
	
	@Shadow
	public abstract void renderBackgroundTexture(int vOffset);
	
	/**
	 * @author TFC The Flying Creeper (aka GiantLuigi4)
	 * @reason needed to stop the dark square from being rendered
	 */
	@Overwrite
	public void renderBackground(MatrixStack matrices, int vOffset) {
		if (this.client.world != null) {
//			this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
		} else {
			this.renderBackgroundTexture(vOffset);
		}
	}
}
