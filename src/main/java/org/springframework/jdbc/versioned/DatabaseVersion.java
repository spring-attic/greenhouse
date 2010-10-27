package org.springframework.jdbc.versioned;

public class DatabaseVersion {

	private int major;
	
	public static DatabaseVersion valueOf(String string) {
		return new DatabaseVersion(Integer.valueOf(string));
	}

	public static DatabaseVersion zero() {
		return new DatabaseVersion(0);
	}

	public boolean equals(Object o) {
		if (!(o instanceof DatabaseVersion)) {
			return false;
		}
		DatabaseVersion version = (DatabaseVersion) o;
		return major == version.major;
	}
	
	public int hashCode() {
		return major * 29;
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
