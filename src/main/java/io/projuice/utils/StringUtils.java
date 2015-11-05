package io.projuice.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringUtils {

	public static List<String> transformAll(Function<String, String> transformation, List<String> original) {
		return original.stream().map(transformation).collect(Collectors.toList());
	}

	public static List<String> prefixAll(String prefix, List<String> original) {
		return transformAll(prefix(prefix), original);
	}

	public static List<String> postfixAll(String postfix, List<String> original) {
		return transformAll(postfix(postfix), original);
	}

	public static Function<String, String> prefix(final String prefix) {
		return item -> {
			return prefix + item;
		};
	}

	public static Function<String, String> postfix(final String postfix) {
		return item -> {
			return item + postfix;
		};
	}

	public static Function<String, String> toUpperCase = item -> {
		if (item == null) {
			return null;
		}
		return item.toUpperCase();
	};

	public static Function<String, String> toLowerCase = item -> {
		if (item == null) {
			return null;
		}
		return item.toUpperCase();
	};

}
