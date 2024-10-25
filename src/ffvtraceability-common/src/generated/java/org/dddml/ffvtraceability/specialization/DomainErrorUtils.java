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
        Exception e = tryGetDomainError(rawException);
        if (shouldBeRegardedAsDomainError(e)) {
            DomainError domainError = e instanceof DomainError
                    ? (DomainError) e
                    : DomainError.named("[" + e.getClass().getName() + "]", e.getMessage());
            // onDomainError.accept(domainError);
            return domainError;// throw domainError;
        } else {
            String msg = "[" + UUID.randomUUID() + "] Exception caught.";
            RuntimeException runtimeException = new RuntimeException(msg, e);
            // onOtherException.accept(runtimeException);
            return runtimeException;// throw runtimeException;
        }
    }

    /**
     * Determines whether an exception should be regarded a Domain Error.
     *
     * @param e The exception to evaluate
     * @return true if the exception should be treated as a Domain Error, false
     *         otherwise
     */
    private static boolean shouldBeRegardedAsDomainError(Exception e) {
        if (e instanceof DomainError) {
            return true;
        }
        final String[][] exceptionAndStackTraceClassNamesArray = new String[][] {
                new String[] { DomainError.class.getName() },
                // TODO: Should exceptions from java.lang package thrown in the Domain layer be
                // regarded Domain Errors?
                new String[] { "java.lang.",
                        "org.dddml.ffvtraceability.domain." },
                // Should all dynamic proxy reflection exceptions be regarded domain errors?
                // Perhaps dynamic proxy exceptions should be "unwrapped"?
                // new String[]{"java.lang.reflect.UndeclaredThrowableException",
                // "com.sun.proxy.", "org.dddml.ffvtraceability.domain."}
        };
        boolean b = false;
        for (int i = 0; i < exceptionAndStackTraceClassNamesArray.length; i++) {
            String[] exceptionAndStackTraceClassNames = exceptionAndStackTraceClassNamesArray[i];
            if (e.getClass().getName().startsWith(exceptionAndStackTraceClassNames[0])) {
                if (exceptionAndStackTraceClassNames.length == 1) {
                    // Only need to match the exception name, not the stack trace elements
                    b = true;
                    break;
                }
                if (e.getStackTrace().length >= exceptionAndStackTraceClassNames.length - 1) {
                    for (int j = 1; j < exceptionAndStackTraceClassNames.length; j++) {
                        if (!e.getStackTrace()[j - 1].getClassName().startsWith(exceptionAndStackTraceClassNames[j])) {
                            break;
                        }
                        if (j == exceptionAndStackTraceClassNames.length - 1) {
                            // All required stack trace elements have been matched
                            b = true;
                        }
                    }
                }
            }
            if (b) {
                break;
            }
        }
        return b;
    }

    /**
     * Attempts to extract a DomainError from an UndeclaredThrowableException.
     * This is useful for handling exceptions thrown by dynamic proxies.
     *
     * @param e The exception to process
     * @return The extracted DomainError if found, otherwise the original exception
     */
    private static Exception tryGetDomainError(Exception e) {
        if (e instanceof java.lang.reflect.UndeclaredThrowableException) {
            java.lang.reflect.UndeclaredThrowableException undeclaredThrowableException = (java.lang.reflect.UndeclaredThrowableException) e;
            Throwable undeclaredThrowable = undeclaredThrowableException.getUndeclaredThrowable();
            if (undeclaredThrowable instanceof java.lang.reflect.InvocationTargetException) {
                java.lang.reflect.InvocationTargetException invocationTargetException = (java.lang.reflect.InvocationTargetException) undeclaredThrowable;
                Throwable targetException = invocationTargetException.getTargetException();
                if (targetException instanceof DomainError) {
                    return (DomainError) targetException;
                }
            }
        }
        return e;
    }
}
