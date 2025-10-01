package com.lgcns.svcp.prod.util;

import java.util.regex.Pattern;

public class PatternUtil {
	
	public static boolean matches(Pattern pattern, String string) {
        return pattern.matcher(string).matches();
    }
}
