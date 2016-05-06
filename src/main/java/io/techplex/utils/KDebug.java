package io.techplex.utils;

/**
 * Get information about method callers.
 * @author techplex
 * @source http://stackoverflow.com/questions/11306811/how-to-get-the-caller-class-in-java
 */
public class KDebug {
    public static String getCallerClassName() { 
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(KDebug.class.getName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
                return ste.getClassName();
            }
        }
        return null;
    }
	public static String getCallerCallerClassName() { 
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		String callerClassName = null;
		for (int i=1; i<stElements.length; i++) {
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(KDebug.class.getName())&& ste.getClassName().indexOf("java.lang.Thread")!=0) {
				if (callerClassName==null) {
					callerClassName = ste.getClassName();
				} else if (!callerClassName.equals(ste.getClassName())) {
					return ste.getClassName();
				}
			}
		}
		return null;
	}
	public static boolean isCalledFrom(String expect) {
		String cls = KDebug.getCallerCallerClassName();
		String out = cls.substring(cls.lastIndexOf('.')+1, cls.length());
		
		return expect.equalsIgnoreCase(out);
	}
}
