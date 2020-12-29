package com.tfc.fabrivr.mixin;

import com.tfc.fabrivr.mixin_code.EntityCode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.collection.ReusableStream;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

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

////	@Inject(at = @At("RETURN"), method = "getCameraPosVec(F)Lnet/minecraft/util/math/Vec3d;")
////	public void getCameraVec(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
////		Entity entity = (Entity)(Object)this;
////		if (entity instanceof PlayerEntity) {
////			double d = MathHelper.lerp(tickDelta, entity.prevX, entity.getX());
////			double e = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + (double)entity.getStandingEyeHeight();
////			double f = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ());
////			cir.setReturnValue(new Vec3d(d, e, f).add(this.getOffset()));
////		}
////	}
//
//	@Shadow
//	public boolean noClip;
//	@Shadow
//	private EntityDimensions dimensions;
//	@Shadow
//	public World world;
//
//	@Shadow
//	private Box entityBounds;
//
//	@Shadow
//	public abstract void setPos(double x, double y, double z);
//
//	@Shadow
//	public static Vec3d adjustSingleAxisMovementForCollisions(Vec3d movement, Box entityBoundingBox, WorldView world, ShapeContext context, ReusableStream<VoxelShape> collisions) {
//		return null;
//	}
//
//	@Shadow
//	public static Vec3d adjustMovementForCollisions(@Nullable Entity entity, Vec3d movement, Box entityBoundingBox, World world, ShapeContext context, ReusableStream<VoxelShape> collisions) {
//		return null;
//	}
//
//	@Shadow
//	protected boolean onGround;
//	@Shadow
//	public float stepHeight;
//
//	@Shadow
//	public static double squaredHorizontalLength(Vec3d vector) {
//		return 0;
//	}
//
//	@Shadow
//	public boolean inanimate;
//
//	@Shadow
//	public abstract Vec3d getPos();
	
	@Shadow private Vec3d pos;
	
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
			return new Vec3d(this.getX(), this.getEyeY(), this.getZ());
		} else {
			double d = MathHelper.lerp(tickDelta, this.prevX, this.getX());
			double e = MathHelper.lerp(tickDelta, this.prevY, this.getY()) + (double) this.getStandingEyeHeight();
			double f = MathHelper.lerp(tickDelta, this.prevZ, this.getZ());
			return new Vec3d(d, e, f);
		}
	}

////	/**
////	 * @author TFC The Flying Creeper (aka GiantLuigi4)
////	 * @reason cir failed to work
////	 */
////	@Overwrite
////	public boolean isInsideWall() {
////		if (this.noClip) {
////			return false;
////		} else {
////			float g = this.dimensions.width * 0.8F;
////			Box box = Box.method_30048(g, 0.10000000149011612D, g).offset(this.getX(), this.getEyeY(), this.getZ());
////			Entity entity = (Entity) (Object) this;
////			if (entity instanceof PlayerEntity)
////				box = box.offset(this.getCameraPosVec(1).add(-this.getX(), -this.getEyeY(), -this.getZ()));
////			return this.world.getBlockCollisions(entity, box, (blockState, blockPos) -> blockState.shouldSuffocate(this.world, blockPos)).findAny().isPresent();
////		}
////	}
//
//	/**
//	 * @author TFC The Flying Creeper (aka GiantLuigi4)
//	 * @reason cir failed to work
//	 */
//	@Overwrite
//	public Box getBoundingBox() {
//		return EntityCode.getBoundingBox((Entity) (Object) this, getVRCamOffset(), this.entityBounds);
//	}
//
//	/**
//	 * @author TFC The Flying Creeper (aka GiantLuigi4)
//	 * @reason cir failed to work
//	 */
//	@Overwrite
//	public boolean isInsideWall() {
//		if (this.noClip || (Entity) (Object) this instanceof PlayerEntity) {
//			return false;
//		} else {
//			float f = 0.1F;
//			float g = this.dimensions.width * 0.8F;
//			Box box = Box.method_30048((double) g, 0.10000000149011612D, (double) g).offset(this.getX(), this.getEyeY(), this.getZ());
//			return this.world.getBlockCollisions((Entity) (Object) this, box, (blockState, blockPos) -> {
//				return blockState.shouldSuffocate(this.world, blockPos);
//			}).findAny().isPresent();
//		}
//	}
//
//	/**
//	 * @author TFC The Flying Creeper (aka GiantLuigi4)
//	 * @reason cir failed to work, make it so the player isn't pushed out of wall that they aren't in
//	 */
//	@Overwrite
//	@Environment(EnvType.CLIENT)
//	public boolean method_30632(BlockPos blockPos, BlockState blockState) {
//		VoxelShape voxelShape = blockState.getCollisionShape(this.world, blockPos, ShapeContext.of((Entity) (Object) this));
//		VoxelShape voxelShape2 = voxelShape.offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());
//		Box box = this.getBoundingBox();
//		if ((Entity) (Object) this instanceof PlayerEntity) box = box.offset(getVRCamOffset());
//		return VoxelShapes.matchesAnywhere(voxelShape2, VoxelShapes.cuboid(box), BooleanBiFunction.AND);
//	}
//
//	/**
//	 * @author TFC The Flying Creeper (aka GiantLuigi4)
//	 * @reason cir failed to work
//	 */
//	@Overwrite
//	public void moveToBoundingBoxCenter() {
//		EntityCode.moveToBoxCenter((Entity) (Object) this, getVRCamOffset());
//	}

	
	
	public Vec3d getVRCamOffset() {
		return new Vec3d(0, 1, 0);
	}
}
