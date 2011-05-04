/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * An abstract template for row mapping operations that map multiple rows to a single object.
 * This is useful when joining a one-to-many relationship where there can be multiple rows returned per root (or aggregate).
 * For example, consider the relationship: "a Presentation has one-to-many Speakers".
 * When joining with the Speaker table to build a Presentation object, multiple rows will be returned for a Presentation if it has more than one Speaker.
 * This template is useful in that case.
 * This class has been submitted for contribution to Spring JDBC; see SPR-7698.
 * @author Keith Donald
 * @param <R> the root, or aggregate, entity type
 * @param <I> the root's id property type
 */
public abstract class JoinRowMapper<R, I>  {
	
	/**
	 * Return a {@link RowMapper} that maps exactly one root object R, where there may be multiple R rows for each child in a join with a one-to-many relationship.
	 */
	public RowMapper<R> single() {
		return singleMapper;
	}

	/**
	 * Return a {@link ResultSetExtractor} that 1..n root objects R into a List where there may be multiple R rows for each joined child.
	 */
	public ResultSetExtractor<List<R>> list() {
		return listMapper;
	}

	// subclassing hooks
	
	/**
	 * Map the ID property I for the root entity out of the current row in the ResultSet.
	 */
	protected abstract I mapId(ResultSet rs) throws SQLException;
	
	/**
	 * Map root object R out of the current row in the ResultSet, including its direct properties and excluding child association properties.
	 */
	protected abstract R mapRoot(I id, ResultSet rs) throws SQLException;

	/**
	 * Map the next child object and add it to root object R. 
	 */
	protected abstract void addChild(R root, ResultSet rs) throws SQLException;

	// internal helpers
	
	private final RowMapper<R> singleMapper = new RowMapper<R> () {
		public R mapRow(ResultSet rs, int rowNum) throws SQLException {
			return map(rs);
		}
	};
	
	private final ResultSetExtractor<List<R>> listMapper = new ResultSetExtractor<List<R>>() {
		public List<R> extractData(ResultSet rs) throws SQLException, DataAccessException {
			return mapInto(new ArrayList<R>(), rs);
		}		
	};
	
	private R map(ResultSet rs) throws SQLException {
		I id = mapId(rs);
		R root = mapRoot(id, rs);
		addChild(root, rs);
		while (rs.next() && mapId(rs).equals(id)) {
			addChild(root, rs);
		}
		return root;
	}

	private <C extends Collection<R>> C mapInto(C collection, ResultSet rs) throws SQLException {
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
	
}