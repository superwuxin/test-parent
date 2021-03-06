## Java Transaction API

## [TransactionAttribute](http://docs.oracle.com/javaee/7/api/javax/ejb/TransactionAttribute.html)

### TransactionAttributeType.REQUIRED (Default)
> If a client invokes the enterprise bean's method while the client is associated with a transaction context, the container invokes the enterprise bean's method in the client's transaction context.

> If the client invokes the enterprise bean's method while the client is not associated with a transaction context, the container automatically starts a new transaction before delegating a method call to the enterprise bean method.

调用EJB Bean时，如果客户端已经在一个事务的上下文中时，那么这个EJB Bean的事务将控制在客户端的相关事务中。

调用EJB Bean时，如果客户端不存在事务，那么EJB容器将启动一个新事务。
```java
@Test
public void testBatchSaveWithTransactionAttributeRequiredWithUx() throws Exception {
	assertEquals(0, userRepository.count());
	ux.begin();
	serviceDefault.batchSaveWithTransactionAttributeRequired(users);
	ux.commit();
	assertEquals("save success, users count equals users size", users.size(), userRepository.count());
	reflushDate();
	ux.begin();
	serviceDefault.batchSaveWithTransactionAttributeRequired(users);
	ux.rollback();// roll back batch save transaction
	assertEquals("ux has rollback, users count not change!", users.size(), userRepository.count());
}
```
### TransactionAttributeType.MANDATORY
> If a client invokes the enterprise bean's method while the client is associated with a transaction context, the container invokes the enterprise bean's method in the client's transaction context.
If there is no existing transaction, an exception is thrown.

调用EJB Bean时，如果客户端已经在一个事务的上下文中时，那么这个EJB Bean的事务将控制在客户端的相关事务中。(与REQUIRED相同)

调用EJB Bean时，如果客户端不存在，将抛出异常。
```java
@Test(expected = EJBTransactionRequiredException.class)
public void testBatchSaveWithTransactionAttributeMandatory() throws Exception {
	assertEquals(0, userRepository.count());
	try {
		serviceDefault.batchSaveWithTransactionAttributeMandatory(users);
	} finally {
		assertEquals("batch save throw exception and rollback, users count is 0", 0, userRepository.count());
	}
}
```
### TransactionAttributeType.NEVER
> The client is required to call without a transaction context, otherwise an exception is thrown.

调用Bean的客户端中不允许存在事务，否则抛出异常
```java
@Test(expected = EJBException.class)
public void testBatchSaveWithTransactionAttributeNeverWithUx() throws Exception {
	assertEquals(0, userRepository.count());
	try {
		ux.begin();
		// exception: transactions not support
		serviceDefault.batchSaveWithTransactionAttributeNever(users);
		ux.commit();
	} finally {
		assertEquals("batchSave throw exception and rollback, users count is 0", 0, userRepository.count());
	}
}
```	
### TransactionAttributeType.NOT_SUPPORTED
> The container invokes an enterprise bean method whose transaction attribute NOT_SUPPORTED with an unspecified transaction context.

> If a client calls with a transaction context, the container suspends the association of the transaction context with the current thread before invoking the enterprise bean's business method. The container resumes the suspended association when the business method has completed.

声明为`NOT_SUPPORTED`的方法不指定事务。

调用时，客户端中存在事务，容器将挂起客户端的事务待方法执行完成后恢复。

如果客户端没有关联到事务中，容器不会在运行这个方法之前启动一个新的事务。
```java
@Test
public void testBatchSaveWithTransactionAttributeNotSupportWithUx() throws Exception {
	assertEquals(0, userRepository.count());
	ux.begin();
	serviceDefault.batchSaveWithTransactionAttributeNotSupport(users);
	ux.commit();
	assertEquals("save success, users count equals users size", users.size(), userRepository.count());
	reflushDate();
	ux.begin();
	serviceDefault.batchSaveWithTransactionAttributeNotSupport(users);
	ux.rollback();
	assertEquals("can't rollback batchSave transaction, users count double than users size", users.size() * 2, userRepository.count());
}
```
### TransactionAttributeType.REQUIRES_NEW
> The container must invoke an enterprise bean method whose transaction attribute is set to REQUIRES_NEW with a new transaction context.

> If the client invokes the enterprise bean's method while the client is not associated with a transaction context, the container automatically starts a new transaction before delegating a method call to the enterprise bean business method.

> If a client calls with a transaction context, the container suspends the association of the transaction context with the current thread before starting the new transaction and invoking the method. The container resumes the suspended transaction association after the method and the new transaction have been completed.

设置为`REQUIRES_NEW`的方法必须运行在一个新的事务中。

调用时，如果客户端不存在事务，容器将自动创建一个新事务，Bean的方法将运行在这个新事务中。

如果客户端已经运行在一个事务中，在执行之前容器将会挂起客户端的事务，并在Bean的方法运行结束事务提交后恢复客户端的事务。
```java
@Test
public void testBatchSaveWithTransactionAttributeRequestNewWithUx() throws Exception {
	assertEquals(0, userRepository.count());
	ux.begin();
	serviceDefault.batchSaveWithTransactionAttributeRequestNew(users);
	ux.commit();
	assertEquals("save success, users count equals users size", users.size(), userRepository.count());
	reflushDate();
	ux.begin();
	serviceDefault.batchSaveWithTransactionAttributeRequestNew(users);
	em.persist(new User("defualt_01", 30, "F", "08678899876", Calendar.getInstance()));
	// roll back this entityManager transaction, but can't roll back batchSave transaction
	ux.rollback();
	assertEquals("rollback client transaction, but can't roll back batchSave transaction", users.size() * 2, userRepository.count());
}
```

### TransactionAttributeType.SUPPORTS
> If the client calls with a transaction context, the container performs the same steps as described in the REQUIRED case.

> If the client calls without a transaction context, the container performs the same steps as described in the NOT_SUPPORTED case.

> The SUPPORTS transaction attribute must be used with caution. This is because of the different transactional semantics provided by the two possible modes of execution. Only enterprise beans that will execute correctly in both modes should use the SUPPORTS transaction attribute.

调用时，如果客户端已经在一个事务的上下文中时，那么这个EJB Bean的事务将控制在客户端的相关事务中。(与REQUIRED相同)

如果客户端没有关联到事务中，容器不会在运行这个方法之前启动一个新的事务。(与NOT_SUPPORTED相同)
```java
@Test
public void testBatchSaveWithTransactionAttributeSupportsWithUx() throws Exception {
	assertEquals(0, userRepository.count());
	ux.begin();
	serviceDefault.batchSaveWithTransactionAttributeSupports(users);
	ux.commit();
	assertEquals("save success, users count equals users size", users.size(), userRepository.count());
	reflushDate();
	ux.begin();
	serviceDefault.batchSaveWithTransactionAttributeSupports(users);
	ux.rollback();
	assertEquals("rollback batchSave transaction success, users count not change", users.size(), userRepository.count());
}
```	
### [TransactionAttribute Test Case](https://github.com/wuxii/test-parent/blob/master/ee-parent/jta-ee/src/test/java/org/moon/test/ee/service/UserServiceTransactionAttributeTest.java)

## Transaction Timeout Test

[Tomee TransactionManager Configuration](http://tomee.apache.org/transactionmanager-config.html)

## Summary

TransactionAttributeType
<ul>
<li>MANDATORY:Bean的强制运行在客户端的事务。
<li>NEVER：客户端中不能存在事务。
<li>NOT_SUPPORTED：如果Bean运行在客户端事务中，客户端的事务回滚也回滚不了服务端的事务。
<li>REQUIRED：如果Bean运行在客户端事务中，客户端的事务回滚了，服务的也跟着回滚。
<li>REQUIRES_NEW:与NOT_SUPPORT相同
<li>SUPPORTS：与REQUIRED相同
</ul>

一方面来说，EJB Bean的事务是具有传递性的，客户端的事务可以传递给服务端，从而达到客户端控制全局事务的作用。
另一方面，服务端能规定客户端该如何使用事务来调用服务端的方法

如一个嵌套多层的方法：
	
	methodA(){
		methodB();
	}
	
	methodB(){
		methodC();
	}
	
	methodC(){
		// do work
	}

方法均为默认`REQUIRED`

客户端不存在事务，client->创建(A-Transaction)->methodA->methodB->methodC，三个方法都是运行在methodA的(A-Transaction)事务上。

客户端存在事务则，client(Client-Transaction)->methodA->methodB->methodC，三个方法都是运行在客户端(Client-Transaction)事务上。

`REQUIRES_NEW`

客户端不存在事务

client->创建(A-Transaction)->methodA->挂起(A-Transaction)->创建(B-Transaction)->methodB->挂起(B-Transaction)->创建(C-Transaction)->methodC->提交(C-Transaction)->恢复(B-Transaction)->提交(B-Transaction)->恢复(A-Transaction)->提交(A-Transaction)->返回client


