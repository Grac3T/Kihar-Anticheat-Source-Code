package net.kihar.anticheat.check;

public interface ICheck<T> {

	Class<T> getType();

	void handle(T type);

}
