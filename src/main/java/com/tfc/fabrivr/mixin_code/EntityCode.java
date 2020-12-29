package com.tfc.fabrivr.mixin_code;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class EntityCode {
	public static void moveToBoxCenter(Entity entity, Vec3d offset) {
		Box box = entity.getBoundingBox();
		if (entity instanceof PlayerEntity) {
			if (!entity.world.isClient) {
				entity.noClip = true;
			}
			box = box.offset(offset.multiply(-1f));
		}
		entity.setPos((box.minX + box.maxX) / 2.0D, box.minY, (box.minZ + box.maxZ) / 2.0D);
	}
	
	public static Box getBoundingBox(Entity entity, Vec3d offset, Box entityBounds) {
//		if (entity instanceof PlayerEntity) return entityBounds.offset(offset.multiply(1f));
		return entityBounds;
	}
}
