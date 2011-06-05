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