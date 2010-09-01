package org.springframework.jdbc.versioned;

public class DatabaseVersion {

	private int major;
	
	public static DatabaseVersion valueOf(String string) {
		return new DatabaseVersion(Integer.valueOf(string));
	}

	public static DatabaseVersion zero() {
		return new DatabaseVersion(0);
	}

	public boolean lessThan(DatabaseVersion version) {
		return major < version.major;
	}

	private DatabaseVersion(int major) {
		this.major = major;
	}
	
	public String toString() {
		return String.valueOf(major);
	}
}
