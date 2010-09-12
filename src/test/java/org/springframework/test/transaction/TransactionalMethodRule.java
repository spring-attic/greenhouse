package org.springframework.test.transaction;

import java.lang.reflect.Field;

import javax.sql.DataSource;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ReflectionUtils;

public class TransactionalMethodRule implements MethodRule {

	public Statement apply(Statement base, FrameworkMethod method, Object target) {
		if (method.getAnnotation(Transactional.class) != null) {
			return new TransactionalStatement(base, target);
		} else {
			return base;
		}
	}

	private class TransactionalStatement extends Statement {
		
		private final Statement base;

		private final Object target;
		
		public TransactionalStatement(Statement base, Object target) {
			this.base = base;
			this.target = target;
		}

		@Override
		public void evaluate() throws Throwable {
			DataSource dataSource = getDataSource();
			PlatformTransactionManager tm = new DataSourceTransactionManager(dataSource);
			TransactionStatus txStatus = tm.getTransaction(new DefaultTransactionDefinition());
			try {
				base.evaluate();
			} catch (Throwable e) {
				tm.rollback(txStatus);
				throw e;
			}
			tm.commit(txStatus);
		}
	
		private DataSource getDataSource() {
			Field field = ReflectionUtils.findField(target.getClass(), "db");
			field.setAccessible(true);
 			return (DataSource) ReflectionUtils.getField(field, target);
		}	

	}

}
