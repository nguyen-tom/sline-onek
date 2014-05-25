package com.sgroup.skara.util;


public class StringMatcher {
	
	private final static char KOREAN_UNICODE_START = 'a';
	private final static char KOREAN_UNICODE_END = 'z';
	private final static char KOREAN_UNIT = '까' - '가';
	private final static char[] UNICODE_INITIAL = {'A', 'Ă', 'Â', 'Á', 'Ạ', 'Ã', 'À', 'Ấ','Ậ', 'Ầ', 'Ẫ', 'Ắ', 'Ặ', 'Ẵ', 'Ằ', 'Ả', 'Ẳ', 'Ẩ',
		                                           'B', 
		                                           'C',
		                                           'D', 'Đ',
		                                           'E', 'Ê', 'Ẻ', 'Ẽ', 'Ẹ', 'È', 'Ể', 'Ể', 'Ề', 'Ế', 'Ễ', 'Ệ',
		                                           'F', 
		                                           'G', 
		                                           'H', 
		                                           'I', 'Ỉ', 'Ì', 'Ị', 'Ĩ', 'Í',
		                                           'K', 
		                                           'L', 
		                                           'M', 
		                                           'N', 
		                                           'O','Ô', 'Ơ', 'Ỏ', 'Ò', 'Ó', 'Õ', 'Ọ', 'Ổ', 'Ồ', 'Ỗ', 'Ộ', 'Ố', 'Ở', 'Ờ', 'Ớ', 'Ỡ', 'Ợ',
		                                           'P',
		                                           'Q',
		                                           'R',
		                                           'S',
		                                           'T',
		                                           'U','Ư', 'Ử', 'Ừ', 'Ữ', 'Ứ', 'Ự', 'Ù', 'Ủ', 'Ú', 'Ù', 'Ũ',
		                                           'V',
		                                           'W',
		                                           'X',
		                                           'Y', 'Ý', 'Ỷ', 'Ỳ', 'Ỹ', 'Ỷ',
		                                           'Z'};
	public static char[][] unicode = {{'A', 'Ă', 'Â', 'Á', 'Ạ', 'Ã', 'À', 'Ấ','Ậ', 'Ầ', 'Ẫ', 'Ắ', 'Ặ', 'Ẵ', 'Ằ', 'Ả', 'Ẳ', 'Ẩ'}, 
		             {'D', 'Đ'}, 
		{'E', 'Ê', 'Ẻ', 'Ẽ', 'Ẹ', 'È', 'Ể', 'Ể', 'Ề', 'Ế', 'Ễ', 'Ệ'}, 
		{'I', 'Ỉ', 'Ì', 'Ị', 'Ĩ', 'Í'}, 
		{'O', 'Ô', 'Ơ', 'Ỏ', 'Ò', 'Ó', 'Õ', 'Ọ', 'Ổ', 'Ồ', 'Ỗ', 'Ộ', 'Ố', 'Ở', 'Ờ', 'Ớ', 'Ỡ', 'Ợ'}, 
		{'U', 'Ư', 'Ử', 'Ừ', 'Ữ', 'Ứ', 'Ự', 'Ù', 'Ủ', 'Ú', 'Ù', 'Ũ'}, 
		{'Y', 'Ý', 'Ỷ', 'Ỳ', 'Ỹ', 'Ỷ'}};
	
	public static boolean match(String value, String keyword) {
		if (value == null || keyword == null)
			return false;
		if (keyword.length() > value.length())
			return false;
		
			if (isInitialSound(keyword.charAt(0))) {
				if (keyword.charAt(0) == value.charAt(0)) {
					return true;
				}
			}
		
		return false;
	}
	
	private static boolean isKorean(char c) {
		if (c >= KOREAN_UNICODE_START && c <= KOREAN_UNICODE_END)
			return true;
		return false;
	}
	
	private static boolean isInitialSound(char c) {
		for (char i : UNICODE_INITIAL) {
			if (c == i)
				return true;
		}
		return false;
	}
	
}