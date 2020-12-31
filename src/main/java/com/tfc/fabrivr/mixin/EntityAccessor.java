package com.tfc.fabrivr.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
	@Accessor("pos")
	void setEntityPos(Vec3d newPos);
	
	@Invoker("getRotationVector")
	Vec3d _getRotationVector(float pitch, float yaw);
}
