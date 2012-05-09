package org.chip.utils

class RandomIdGenerator {
	static String uSet="1234567890abcdefghijklmnopqrstuvwxyz"
	static String alphaSet="abcdefghijklmnopqrstuvwxyz"
	static Random random=new Random()
	public static String generateString(int length)
	{
		StringBuilder sb = new StringBuilder(length);
		sb.append(alphaSet.charAt(random.nextInt(alphaSet.length())))
		for (int i = 1; i < length; i++){
			sb.append(uSet.charAt(random.nextInt(uSet.length())));
		}
		return sb.toString()
	}
}
