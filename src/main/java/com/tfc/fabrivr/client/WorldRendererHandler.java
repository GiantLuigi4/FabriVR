package com.tfc.fabrivr.client;

import com.tfc.fabrivr.mixin.PlayerRendererAccessor;
import com.tfc.fabrivr.utils.openvr.Tracking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.joml.Quaternionf;

public class WorldRendererHandler {
	public static void setupHeadTranslation(MatrixStack matrices) {
		Tracking.update();
		Vec3d vector3d = MinecraftClient.getInstance().cameraEntity.getCameraPosVec(MinecraftClient.getInstance().getTickDelta());
		vector3d = vector3d.subtract(0, MathHelper.lerp(
				MinecraftClient.getInstance().getTickDelta(),
				MinecraftClient.getInstance().cameraEntity.lastRenderY,
				MinecraftClient.getInstance().cameraEntity.getPos().y
		), 0);
		matrices.translate(0, vector3d.y, 0);
		float height = MinecraftClient.getInstance().cameraEntity.getHeight();
		height /= 1.7;
		Quaternionf quaternionf = Tracking.head.rotation;
		Quaternion quaternion = new Quaternion(-quaternionf.x, quaternionf.y, quaternionf.z, quaternionf.w);
		matrices.translate(Tracking.head.position.x * height, -Tracking.head.position.y * height, Tracking.head.position.z * height);
		matrices.multiply(quaternion);
	}
	
	public static void renderHands(MatrixStack matrices) {
		EntityRenderer<?> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(MinecraftClient.getInstance().getCameraEntity());
		if (renderer instanceof PlayerEntityRenderer) {
			matrices.translate(
					-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().x,
					-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().y,
					-MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().z
			);
			matrices.translate(
					MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), MinecraftClient.getInstance().cameraEntity.lastRenderX, MinecraftClient.getInstance().cameraEntity.getX()),
					MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), MinecraftClient.getInstance().cameraEntity.lastRenderY, MinecraftClient.getInstance().cameraEntity.getY()),
					MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), MinecraftClient.getInstance().cameraEntity.lastRenderZ, MinecraftClient.getInstance().cameraEntity.getZ())
			);
			
			matrices.translate(Tracking.rightHand.position.x, Tracking.rightHand.position.y, Tracking.rightHand.position.z);
			((PlayerRendererAccessor)((PlayerEntityRenderer) renderer)).doRenderArm(
					matrices, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(),
					LightmapTextureManager.pack(
							//TODO: make this get the light based off where the hand is instead of where the player is
							MinecraftClient.getInstance().world.getLightLevel(LightType.BLOCK, MinecraftClient.getInstance().cameraEntity.getBlockPos()),
							MinecraftClient.getInstance().world.getLightLevel(LightType.SKY, MinecraftClient.getInstance().cameraEntity.getBlockPos())
					), (AbstractClientPlayerEntity) MinecraftClient.getInstance().cameraEntity,
					((PlayerEntityRenderer) renderer).getModel().rightArm, ((PlayerEntityRenderer) renderer).getModel().rightSleeve
			);
		}
	}
}
