package com.zdm.test.junit.testcase;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zdm.test.junit.User;


/**
 * @author bill
 *	JUnit 3.X: junit.framework.Assert 
 *	JUnit 4.X: org.junit.Assert
 *	 4.1 、assertEquals([String message],Object target,Object result)

target与result不相等，中断测试方法，输出message

assertNull   断言对象为null，若不满足，方法抛出带有相应信息的AssertionFailedError异常。

assertEquals(a, b) 测试a是否等于b（a和b是原始类型数值(primitive value)或者必须为实现比较而具有equal方法）

assertEquals

断言两个对象相等，若不满足，方法抛出带有相应信息的AssertionFailedError异常。

例如计算器加法功能的测试可以使用一下验证：

Assert.assertEquals(0,result)；

4.2  assertTrue/False([String message],Boolean result)

Result为 false/true，中断测试方法，输出message

assertTrue

断言条件为真，若不满足，方法抛出带有相应信息的AssertionFailedError异常。

assertFalse(a) 测试a是否为false（假），a是一个Boolean数值。

assertFalse

断言条件为假，若不满足，方法抛出带有相应信息的AssertionFailedError异常。

4.3  assertNotNull/Null（[String message],Obejct result

Retult= = null/result!=null,中断测试方法，输出message

assertNotNull(a) 测试a是否非空，a是一个对象或者null。

assertNotNull 断言对象不为null，若不满足，方法抛出带有相应信息的AssertionFailedError异常。

4.4  assertSame/NotSame（Object target,Object result）

Traget与result 不指向/指向 同一内存地址（实例），中断测试方法，输出message

assertNotSame(a, b) 测试a和b是否没有都引用同一个对象。

assertNotSame

断言两个引用指向不同对象，若不满足，方法抛出带有相应信息的AssertionFailedError异常。

assertSame 断言两个引用指向同一个对象，若不满足，方法抛出带有相应信息AssertionFailedError异常。

4.5  fail([String message])

中断测试方法，输出message

Fail  让测试失败，并给出指定信息。
 */
public class MethodTest {

	private static User u;
	
	@Before
	public void beforeTest(){
		System.out.println("before");
	}
	
	@BeforeClass
	public static void beforeOneTest(){
		u=new User("1", "Bill", "password");
		System.out.println("@BeforeClass");
	}
	
	@AfterClass
	public static void afterOneTest(){
		System.out.println("@AfterClass");
	}
	
	@Test
	public void hTest(){
		System.out.println("htest");
		Assert.assertEquals("hello", new User().hello());		
	}
	
	@Test
	public void bFasleTest(){
		Assert.assertFalse(new User().bFasle());
		Assert.assertEquals("1User [id=1, name=Bill, pwd=password]",u.toString());
	}
	
	@Test(timeout=1)
	public void toStringTest(){
		
	}
	
	@Test(expected= IndexOutOfBoundsException.class) 
	 public void empty() {
	     new ArrayList<Object>().get(0); 
	 }
	
	@After
	public void afterTest(){
		System.out.println("after");
	}
}
