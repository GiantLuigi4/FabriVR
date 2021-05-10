package com.tfc.fabrivr.utils.openvr;

import org.lwjgl.openvr.VR;
import org.lwjgl.openvr.VRCompositor;

public enum VRCompositorError {
	NONE(VR.EVRCompositorError_VRCompositorError_None),
	REQUEST_FAILED(VR.EVRCompositorError_VRCompositorError_RequestFailed),
	INCOMPATIBLE_VERESION(VR.EVRCompositorError_VRCompositorError_IncompatibleVersion),
	NO_FOCUS(VR.EVRCompositorError_VRCompositorError_DoNotHaveFocus),
	INVALID_TEXTURE(VR.EVRCompositorError_VRCompositorError_InvalidTexture),
	NOT_SCENE_APPLICATION(VR.EVRCompositorError_VRCompositorError_IsNotSceneApplication),
	TEXTURE_ON_WRONG_DEVICE(VR.EVRCompositorError_VRCompositorError_TextureIsOnWrongDevice),
	UNSUPPORTED_FORMAT(VR.EVRCompositorError_VRCompositorError_TextureUsesUnsupportedFormat),
	SHARED_TEXTURES_UNSUPPORTED(VR.EVRCompositorError_VRCompositorError_SharedTexturesNotSupported),
	INDEX_OUT_OF_RANGE(VR.EVRCompositorError_VRCompositorError_IndexOutOfRange),
	ALREADY_SUBMITTED(VR.EVRCompositorError_VRCompositorError_AlreadySubmitted),
	INVALID_BOUNDS(VR.EVRCompositorError_VRCompositorError_InvalidBounds),
	;
	
	private final int id;
	
	VRCompositorError(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static VRCompositorError valueOf(int id) {
		for (VRCompositorError value : values()) {
			if (value.id == id) return value;
		}
		return null;
	}
}
