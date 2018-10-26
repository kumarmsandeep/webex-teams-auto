package com.webexteams.auto.utils;

import com.jcabi.manifests.Manifests;

public class ManifestUtil {

	public static String getProjectTitle() {
		return getValue("projectTitle");
	}

	public static String getProjectVersion() {
		return getValue("Project-Version");
	}

	public static String getOrgName() {
		return getValue("orgName");
	}

	private static String getValue(String name) {
		if (Manifests.exists(name)) {
			return Manifests.read(name);
		}
		return null;
	}
}
