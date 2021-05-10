package com.tfc.fabrivr.utils.openvr;

import org.lwjgl.openvr.VR;

public enum Eye {
	LEFT(VR.EVREye_Eye_Left),
	RIGHT(VR.EVREye_Eye_Right),
	;
	
	private final int id;
	
	Eye(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static Eye valueOf(int id) {
		for (Eye value : values()) {
			if (value.id == id) return value;
		}
		return null;
	}
}
