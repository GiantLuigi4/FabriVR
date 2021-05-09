package com.tfc.fabrivr.mixin;

import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Matrix4f.class)
public interface Matrix4fAccessor {
	@Accessor("a00") void setA00(float val);
	@Accessor("a01") void setA01(float val);
	@Accessor("a02") void setA02(float val);
	@Accessor("a03") void setA03(float val);
	@Accessor("a10") void setA10(float val);
	@Accessor("a11") void setA11(float val);
	@Accessor("a12") void setA12(float val);
	@Accessor("a13") void setA13(float val);
	@Accessor("a20") void setA20(float val);
	@Accessor("a21") void setA21(float val);
	@Accessor("a22") void setA22(float val);
	@Accessor("a23") void setA23(float val);
}
