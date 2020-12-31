package com.tfc.fabrivr.mixin;

import com.tfc.fabrivr.FabriVR;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract double getX();

	@Shadow
	public abstract double getEyeY();

	@Shadow
	public abstract double getZ();

	@Shadow
	public double prevX;
	@Shadow
	public double prevY;
	
	@Shadow
	public abstract double getY();
	
	@Shadow
	public abstract float getStandingEyeHeight();
	
	@Shadow
	public double prevZ;
	
	@Shadow
	public abstract DataTracker getDataTracker();
	
	@Shadow
	protected abstract Vec3d getRotationVector(float pitch, float yaw);
	
	@Shadow
	public abstract float getPitch(float tickDelta);
	
	@Shadow
	public abstract float getYaw(float tickDelta);
	
	/**
	 * @author TFC The Flying Creeper (aka GiantLuigi4)
	 * @reason cir failed to work
	 */
	@Overwrite
	public final Vec3d getCameraPosVec(float tickDelta) {
		Entity entity = (Entity) (Object) this;
		if (entity instanceof PlayerEntity) {
			double d = MathHelper.lerp(tickDelta, entity.prevX, entity.getX());
			double e = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + (double) entity.getStandingEyeHeight();
			double f = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ());
			return new Vec3d(d, e, f).add(this.getVRCamOffset());
		}
		if (tickDelta == 1.0F) {
			return new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
		} else {
			double d = MathHelper.lerp(tickDelta, entity.prevX, entity.getX());
			double e = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + (double) entity.getStandingEyeHeight();
			double f = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ());
			return new Vec3d(d, e, f);
		}
	}
	
	/**
	 * @author TFC The Flying Creeper (aka GiantLuigi4)
	 * @reason cir failed to work
	 */
	@Overwrite
	public final Vec3d getRotationVec(float tickDelta) {
		return getRotation(tickDelta);
	}
	
	public Vec3d getVRCamOffset() {
		return new Vec3d(
				this.getDataTracker().get(FabriVR.OFF_X),
				this.getDataTracker().get(FabriVR.OFF_Y),
				this.getDataTracker().get(FabriVR.OFF_Z)
		);
	}
	
	public Vec3d getRotation(float tickDelta) {
		return this.getRotationVector(this.getPitch(tickDelta), this.getYaw(tickDelta));
	}
}
