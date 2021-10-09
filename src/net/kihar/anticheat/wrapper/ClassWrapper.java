package net.kihar.anticheat.wrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ClassWrapper<T> {

	private final Class<?>[] wrappedClasses;

	protected List<Field> fields = new ArrayList<>();
	protected List<Method> methods = new ArrayList<>();

	protected T instance;

	public ClassWrapper(T instance, Class<?>... wrappedClasses) {
		this.instance = instance;
		this.wrappedClasses = wrappedClasses;

		this.setupFields();
	}

	private void setupFields() {
		Arrays.stream(this.wrappedClasses).forEach(c -> this.fields.addAll(Arrays.asList(c.getDeclaredFields())));
		Arrays.stream(this.wrappedClasses).forEach(c -> this.methods.addAll(Arrays.asList(c.getDeclaredMethods())));
	}

	protected Field findField(String name) {
		return this.fields.stream()
				.filter(f -> f.getName().equalsIgnoreCase(name.intern()))
				.findFirst()
				.orElse(null);
	}

	protected Method findMethod(String name) {
		return this.methods.stream()
				.filter(m -> m.getName().equalsIgnoreCase(name.intern()))
				.findFirst()
				.orElse(null);
	}

	protected void setField(int index, Object object) {
		this.fields.get(index).setAccessible(true);
		try {
			this.fields.get(index).set(this.instance, object);
		} catch (Exception e) {
			//
		}
		this.fields.get(index).setAccessible(false);
	}

	protected void setField(String name, Object object) {
		Field field = this.findField(name);
		if (field == null) {
			return;
		}

		field.setAccessible(true);
		try {
			field.set(this.instance, object);
		} catch (Exception e) {
			//
		}
		field.setAccessible(false);
	}

	protected Object getField(int index) {
		Object returning = null;

		this.fields.get(index).setAccessible(true);
		try {
			returning = this.fields.get(index).get(this.instance);
		} catch (Exception e) {
			//
		}
		this.fields.get(index).setAccessible(false);

		return returning;
	}

	protected Object getField(String name) {
		Field field = this.findField(name);
		if (field == null) {
			return null;
		}

		Object returning = null;

		field.setAccessible(true);
		try {
			returning = field.get(this.instance);
		} catch (Exception e) {
			//
		}
		field.setAccessible(false);

		return returning;
	}

	protected Object callMethod(String name, Object... params) {
		Method method = this.findMethod(name);
		if (method == null) {
			return null;
		}

		method.setAccessible(true);
		try {
			return method.invoke(this.instance, params);
		} catch (Exception e) {
			//
		}
		method.setAccessible(false);

		return null;
	}
}
