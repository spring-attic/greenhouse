/*
 * Copyright 2012 the original author or authors.
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
package org.springframework.test.transaction;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class Transactional implements MethodRule {

	private EmbeddedDatabase database;

	public Transactional(EmbeddedDatabase database) {
		this.database = database;
	}

	public Statement apply(Statement base, FrameworkMethod method, Object target) {
		return new TransactionalStatement(base);
	}

	private class TransactionalStatement extends Statement {
		
		private final Statement next;

		public TransactionalStatement(Statement next) {
			this.next = next;
		}

		@Override
		public void evaluate() throws Throwable {
			PlatformTransactionManager tm = new DataSourceTransactionManager(database);
			TransactionStatus txStatus = tm.getTransaction(new DefaultTransactionDefinition());
			try {
				try {
					next.evaluate();
				} catch (Throwable e) {
					tm.rollback(txStatus);
					throw e;
				}
				tm.commit(txStatus);
			} finally {
				database.shutdown();				
			}
		}

	}

}