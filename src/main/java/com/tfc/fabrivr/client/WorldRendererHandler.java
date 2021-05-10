package com.tfc.fabrivr.client;

import com.tfc.fabrivr.mixin.PlayerRendererAccessor;
import com.tfc.fabrivr.utils.openvr.Tracking;
import com.tfc.fabrivr.utils.openvr.VRCompositor;
import com.tfc.fabrivr.utils.translation.Angle;
import com.tfc.fabrivr.utils.translation.Joml2MC;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
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
//		matrices.translate(Tracking.head.position.x * height, -Tracking.head.position.y * height, Tracking.head.position.z * height);
		matrices.translate(0, 0 ,0.1f);
		matrices.translate(0, -Tracking.head.position.y * height, 0);
//		{
//			Quaternionf quaternionf = Tracking.head.rotation;
//			Quaternion quaternion = new Quaternion(quaternionf.x, -quaternionf.y, quaternionf.z, quaternionf.w);
//			quaternion.normalize();
//			matrices.multiply(quaternion);
//		}
		matrices.translate(Tracking.head.position.x * height, 0, Tracking.head.position.z * height);
//		Vec3d angle = Angle.toEulers(quaternion); // TODO: make this only happen on oculus
//		matrices.multiply(
//				new Quaternion(
//						(float) angle.x,
//						-(float) angle.y,
//						(float) angle.z,
//						false
//				)
//		);
	}
	
	// TODO: create a slim variation of the arms and use "AbstractClientPlayerEntity.getModelName" to choose which model to render
	private static final ModelPart leftArm;
	private static final ModelPart rightArm;
	static {
		leftArm = new ModelPart(64, 64, 0, 0);
		leftArm.mirror = true;
		leftArm.setPivot(0.0F, 0.0F, 0.0F);
		leftArm.setTextureOffset(32, 48).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		
		rightArm = new ModelPart(64, 64, 0, 0);
		rightArm.setPivot(0.0F, 0.0F, 0.0F);
		rightArm.setTextureOffset(40, 16).addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
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
			
			matrices.push();
			{
				float height = MinecraftClient.getInstance().cameraEntity.getHeight();
				height /= 1.7;
				matrices.translate(-Tracking.leftHand.position.x * height, Tracking.leftHand.position.y, -Tracking.leftHand.position.z * height);
				{
					Quaternionf quaternionf = Tracking.leftHand.rotation;
					Quaternion quaternion = new Quaternion(-quaternionf.x, quaternionf.y, -quaternionf.z, quaternionf.w);
					quaternion.normalize();
					matrices.multiply(quaternion);
					matrices.multiply(new Quaternion(90, 180, 0, true));
				}
				matrices.translate(0, -0.125, 0);
				matrices.scale(0.5f, 0.5f, 0.5f);
				leftArm.render(
						matrices, MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers().getBuffer(RenderLayer.getEntitySolid(MinecraftClient.getInstance().player.getSkinTexture())),
						LightmapTextureManager.pack(0,15), OverlayTexture.DEFAULT_UV
				);
			}
			matrices.pop();
			
			matrices.push();
			{
				float height = MinecraftClient.getInstance().cameraEntity.getHeight();
				height /= 1.7;
				matrices.translate(-Tracking.rightHand.position.x * height, Tracking.rightHand.position.y, -Tracking.rightHand.position.z * height);
				{
					Quaternionf quaternionf = Tracking.rightHand.rotation;
					Quaternion quaternion = new Quaternion(-quaternionf.x, quaternionf.y, -quaternionf.z, quaternionf.w);
					matrices.multiply(quaternion);
					quaternion.normalize();
					matrices.multiply(new Quaternion(90, 180, 0, true));
				}
				matrices.translate(0, -0.125, 0);
				matrices.scale(0.5f, 0.5f, 0.5f);
				rightArm.render(
						matrices, MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers().getBuffer(RenderLayer.getEntitySolid(MinecraftClient.getInstance().player.getSkinTexture())),
						LightmapTextureManager.pack(0,15), OverlayTexture.DEFAULT_UV
				);
			}
			matrices.pop();
		}
	}
}
