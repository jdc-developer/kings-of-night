package jdc.kings.state.objects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Action<T, K> {
	
	private Method action;
	private K declaringClass;
	private T actionParameter;
	
	public Method getAction() {
		return action;
	}
	
	public void setAction(Method action) {
		this.action = action;
	}
	
	public K getDeclaringClass() {
		return declaringClass;
	}

	public void setDeclaringClass(K declaringClass) {
		this.declaringClass = declaringClass;
	}

	public T getActionParameter() {
		return actionParameter;
	}
	
	public void setActionParameter(T actionParameter) {
		this.actionParameter = actionParameter;
	}
	
	public void callAction() {
		try {
			action.invoke(declaringClass, actionParameter);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Action() {
		super();
	}

	public Action(Method action, K declaringClass, T actionParameter) {
		super();
		this.action = action;
		this.declaringClass = declaringClass;
		this.actionParameter = actionParameter;
	}
	
	
}
