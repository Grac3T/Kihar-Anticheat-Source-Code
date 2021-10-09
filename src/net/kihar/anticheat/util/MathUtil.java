package net.kihar.anticheat.util;

import java.util.Collection;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtil {

	public double average(Collection<Integer> numbers) {
		double average = 0.0;
		for (final int i : numbers) {
			average += i;
		}
		average /= numbers.size();

		return average;
	}

	public double stDeviation(Collection<Integer> numbers) {
		double average = MathUtil.average(numbers);

		double stdDev = 0.0;
		for (final int j : numbers) {
			stdDev += Math.pow(j - average, 2.0);
		}
		stdDev /= numbers.size();
		stdDev = Math.sqrt(stdDev);

		return stdDev;
	}
}
