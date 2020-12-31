package com.tfc.fabrivr.mixin;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Screen.class)
public interface GUIVRFieldsAccessor {
//	Vec3d getOpenedAt();
//	Quaternion getOpenedAngle();
}
