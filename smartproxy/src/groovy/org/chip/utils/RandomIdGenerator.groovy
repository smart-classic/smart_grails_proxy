package org.chip.utils

class RandomIdGenerator {
	static String uSet="1234567890abcdefghijklmnopqrstuvwxyz"
	static Random random=new Random()
	public static String generateString(int length)
	{
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++){
			sb.append(uSet.charAt(random.nextInt(uSet.length())));
		}
		return sb.toString()
	}
}
