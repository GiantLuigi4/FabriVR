package com.tfc.fabrivr.mixin_code;

import com.tfc.fabrivr.AngleHelper;
import com.tfc.fabrivr.FabriVR;
import com.tfc.fabrivr.client.FabriVRClient;
import com.tfc.fabrivr.client.FabriVROculus;
import com.tfc.fabrivr.mixin.EntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PlayerEntityMixinCode {
	public static void move(@NotNull PlayerEntity player) {
		if (player.equals(MinecraftClient.getInstance().player)) {
			BlockPos pos = new BlockPos(
					player.getX() - FabriVRClient.offX,
					player.getY(),
					player.getZ() - FabriVRClient.offZ
			);
			if (!player.world.getBlockState(pos).isFullCube(player.world, pos)) {
				player.setBoundingBox(player.getBoundingBox().offset(
						FabriVRClient.offX,
						0,
						FabriVRClient.offZ
				));
				player.moveToBoundingBoxCenter();
				for (int x = 0; x <= 1; x++) {
					float xOff = -player.getWidth() / 2f;
					if (x != 0) xOff = -xOff;
					for (int y = 1; y >= 0; y--) {
						float yOff = MathHelper.lerp(y / 1f, 0, player.getHeight());
						for (int z = 0; z <= 2; z++) {
							float zOff = -player.getWidth() / 2f;
							if (z != 0) zOff = -zOff;
							BlockPos pos1 = new BlockPos(
									player.getPos().x + xOff,
									player.getPos().y + yOff,
									player.getPos().z + zOff
							);
							BlockState state = player.world.getBlockState(pos1);
							VoxelShape shape = state.getCollisionShape(player.world, pos1);
							if (shape != null && !shape.isEmpty() && shape.getBoundingBoxes() != null) {
								for (Box box : shape.getBoundingBoxes()) {
									box = box.offset(pos1);
									if (player.getBoundingBox().intersects(box)) {
										if ((box.maxY - box.minY) - (player.getPos().y - player.getBlockPos().getY()) <= player.stepHeight && y == 0) {
											player.setBoundingBox(player.getBoundingBox().offset(
													0,
													box.maxY - box.minY - (player.getPos().y - player.getBlockPos().getY()),
													0
											));
										} else {
											player.setBoundingBox(player.getBoundingBox().offset(
													-FabriVRClient.offX,
													0,
													-FabriVRClient.offZ
											));
											FabriVRClient.offX = 0;
											FabriVRClient.offZ = 0;
										}
										player.moveToBoundingBoxCenter();
										return;
									}
								}
							}
						}
					}
				}
				
				if (FabriVROculus.headPos != null) {
					FabriVRClient.offX = FabriVRClient.trueOffX - FabriVROculus.headPos.x() * 2;
					FabriVRClient.offZ = FabriVRClient.trueOffZ - FabriVROculus.headPos.z() * 2;
					FabriVRClient.offY = (FabriVROculus.headPos.y());
					FabriVRClient.trueOffX = FabriVROculus.headPos.x() * 2;
					FabriVRClient.trueOffZ = FabriVROculus.headPos.z() * 2;
				}
			}
		}
	}
	
	public static Vec3d getCamOff(PlayerEntity entity) {
		if (entity.equals(MinecraftClient.getInstance().player) && FabriVROculus.hand2Pos != null) {
			float offX = (FabriVROculus.hand2Pos.x() * 2);
			float offY = (FabriVROculus.hand2Pos.y() * 2 - 0.5f);
			float offZ = (FabriVROculus.hand2Pos.z() * 2);
			if (offX <= 0) offX -= 0.5f;
			if (offY <= 0) offY -= 0.5f;
			if (offZ <= 0) offZ -= 0.5f;
			return new Vec3d(
					FabriVRClient.offX - offX,
					FabriVRClient.offY - offY,
					FabriVRClient.offZ - offZ
			);
		}
		return new Vec3d(
				entity.getDataTracker().get(FabriVR.OFF_X),
				entity.getDataTracker().get(FabriVR.OFF_Y),
				entity.getDataTracker().get(FabriVR.OFF_Z)
		);
	}
	
	public static Vec3d getRotation(float tickDelta, Entity entity) {
		if (MinecraftClient.getInstance().player == entity && FabriVROculus.hand2Pos != null) {
			return AngleHelper.toMotionVector(
					new Quaternion(
							-FabriVROculus.hand2Quat.x(),
							FabriVROculus.hand2Quat.y(),
							-FabriVROculus.hand2Quat.z(),
							FabriVROculus.hand2Quat.w()
					)
			).multiply(-1, -1, 1);
		}
		return ((EntityAccessor) entity)._getRotationVector(entity.getPitch(tickDelta), entity.getYaw(tickDelta));
	}
}
