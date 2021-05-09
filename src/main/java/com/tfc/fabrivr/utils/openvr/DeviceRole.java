package com.tfc.fabrivr.utils.openvr;

import org.lwjgl.openvr.VR;

public enum DeviceRole {
	LEFT_HAND(VR.ETrackedControllerRole_TrackedControllerRole_LeftHand),
	RIGHT_HAND(VR.ETrackedControllerRole_TrackedControllerRole_RightHand),
	TREADMILL(VR.ETrackedControllerRole_TrackedControllerRole_Treadmill),
	INVALID(VR.ETrackedControllerRole_TrackedControllerRole_Invalid),
	;
	
	private final int type;
	
	public int getID() {
		return type;
	}
	
	DeviceRole(int type) {
		this.type = type;
	}
	
	public static DeviceRole valueOf(int role) {
		for (DeviceRole value : values()) {
			if (value.type == role) return value;
		}
		return null;
	}
}
