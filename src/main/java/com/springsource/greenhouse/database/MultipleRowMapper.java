package com.springsource.greenhouse.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public abstract class MultipleRowMapper<R, I, C> {

	public R map(ResultSet rs) throws SQLException {
		R root = mapRoot(mapId(rs), rs);
		addChild(root, rs);
		while (rs.next()) {
			addChild(root, rs);			
		}
		return root;
	}

	public <L extends Collection<R>> L mapInto(L collection, ResultSet rs) throws SQLException {
		R root = null;
		I previousId = null;
		while (rs.next()) {
			I id = mapId(rs);
			if (!id.equals(previousId)) {
				root = mapRoot(id, rs);
				collection.add(root);
			}
			addChild(root, rs);
			previousId = id;
		}
		return collection;
	}

	protected abstract I mapId(ResultSet rs) throws SQLException;
	
	protected abstract R mapRoot(I id, ResultSet rs) throws SQLException;

	protected abstract void addChild(R root, ResultSet rs) throws SQLException;

}