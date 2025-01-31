package org.dddml.ffvtraceability.specialization;

import java.util.UUID;

public class DomainErrorUtils {

    /**
     * Converts exceptions that can be regarded Domain Errors to DomainError,
     * and other exceptions to RuntimeException.
     *
     * @param rawException The original exception
     * @return A RuntimeException (either DomainError or a wrapped RuntimeException)
     */
    public static RuntimeException convertException(Exception rawException) {
        if (rawException == null) {
            return new RuntimeException("Null exception provided");
        }

        Exception unwrappedException = unwrapException(rawException);
        if (isDomainException(unwrappedException)) {
            DomainError domainError = unwrappedException instanceof DomainError
                    ? (DomainError) unwrappedException
                    : DomainError.named("[" + unwrappedException.getClass().getName() + "]",
                    unwrappedException.getMessage() != null ? unwrappedException.getMessage() : "No message");
            return domainError;
        } else {
            String msg = "[" + UUID.randomUUID() + "] Exception caught.";
            return new RuntimeException(msg, unwrappedException);
        }
    }

    /**
     * Determines if an exception represents a domain exception based on its type and stack trace.
     * An exception is considered a domain exception if it is either:
     * 1. An instance of DomainError
     * 2. A java.lang exception thrown from the domain layer
     * 3. Matches specific exception patterns defined in the configuration
     *
     * @param e The exception to evaluate
     * @return true if the exception represents a domain exception, false otherwise
     */
    private static boolean isDomainException(Exception e) {
        if (e == null) {
            return false;
        }

        if (e instanceof DomainError) {
            return true;
        }

        final String[][] exceptionAndStackTraceClassNamesArray = new String[][]{
                new String[]{DomainError.class.getName()},
                // TODO: Should exceptions from java.lang package thrown in the Domain layer be regarded Domain Errors?
                new String[]{"java.lang.",
                        "org.dddml.ffvtraceability.domain."},
                // Should all dynamic proxy reflection exceptions be regarded domain errors?
                // Perhaps dynamic proxy exceptions should be "unwrapped"?
                // new String[]{"java.lang.reflect.UndeclaredThrowableException",
                // "com.sun.proxy.", "org.dddml.ffvtraceability.domain."}
        };

        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace == null) {
            return false;
        }

        String className = e.getClass().getName();
        boolean matched = false;
        for (String[] exceptionAndStackTraceClassNames : exceptionAndStackTraceClassNamesArray) {
            if (exceptionAndStackTraceClassNames.length == 0) {
                continue;
            }

            if (className.startsWith(exceptionAndStackTraceClassNames[0])) {
                if (exceptionAndStackTraceClassNames.length == 1) {
                    matched = true;
                    break;
                }

                if (stackTrace.length >= exceptionAndStackTraceClassNames.length - 1) {
                    boolean allMatched = true;
                    for (int j = 1; j < exceptionAndStackTraceClassNames.length; j++) {
                        StackTraceElement element = stackTrace[j - 1];
                        if (element == null || element.getClassName() == null ||
                                !element.getClassName().startsWith(exceptionAndStackTraceClassNames[j])) {
                            allMatched = false;
                            break;
                        }
                    }
                    if (allMatched) {
                        matched = true;
                        break;
                    }
                }
            }
        }
        return matched;
    }

    /**
     * Unwraps an exception by extracting the underlying cause from proxy-related wrappers.
     * This method handles common proxy and reflection wrappers:
     * - UndeclaredThrowableException from dynamic proxies
     * - InvocationTargetException from reflection calls
     *
     * @param e The wrapped exception
     * @return The unwrapped exception, or the original exception if no unwrapping is needed
     */
    private static Exception unwrapException(Exception e) {
        if (e == null) {
            return null;
        }

        if (e instanceof java.lang.reflect.UndeclaredThrowableException undeclaredThrowableException) {

            Throwable undeclaredThrowable = undeclaredThrowableException.getUndeclaredThrowable();
            if (undeclaredThrowable == null) {
                return e;
            }

            if (undeclaredThrowable instanceof java.lang.reflect.InvocationTargetException invocationTargetException) {

                Throwable targetException = invocationTargetException.getTargetException();
                if (targetException instanceof DomainError) {
                    return (DomainError) targetException;
                }
            }
        }
        return e;
    }
}
