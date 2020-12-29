package com.tfc.fabrivr;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class FabriVR implements ModInitializer {
	public static final TrackedData<Float> OFF_X = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	public static final TrackedData<Float> OFF_Y = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	public static final TrackedData<Float> OFF_Z = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	
	@Override
	public void onInitialize() {
	}
}
