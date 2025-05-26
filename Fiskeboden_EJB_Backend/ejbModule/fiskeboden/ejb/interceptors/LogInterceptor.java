package fiskeboden.ejb.interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LogInterceptor {
    
	@AroundInvoke
	public Object logMethod(InvocationContext iCtx) throws Exception {
	    boolean hasLog = true;

	    if (hasLog) {
	        String className = iCtx.getTarget().getClass().getSimpleName();
	        String methodName = iCtx.getMethod().getName();
	        Object[] params = iCtx.getParameters();

	        System.out.println("Method: " + className + "." + methodName + " called with parameters: "
	                + (params != null ? Arrays.toString(params) : "[]"));

	        Object result = null;

	        try {
	            result = iCtx.proceed();

	            String resultType = (result != null ? result.getClass().getSimpleName() : "null");
	            int numberOfResults = 0;

	            if (result instanceof List<?>) {
	                numberOfResults = ((List<?>) result).size();
	            } else if (result instanceof Map<?, ?>) {
	                numberOfResults = ((Map<?, ?>) result).size();
	            }

	            System.out.println("Method: " + className + "." + methodName +
	                    " executed successfully. Result type: " + resultType +
	                    ", Number of results: " + numberOfResults +
	                    ", Detailed result: " + (result != null ? result.toString() : "null"));

	            return result;

	        } catch (Exception e) {
	            System.err.println("Error in: " + className + "." + methodName + " threw exception: " + e.getMessage());
	            throw e;
	        }

	    } else {
	        return iCtx.proceed();
	    }
	}
}