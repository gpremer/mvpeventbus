package net.premereur.mvp.core.base;

/**
 * Formats log messages.
 * 
 * @author gpremer
 * 
 */
public abstract class LogHelper {

    /**
     * Formats the given argument array taking into account that it may be null or empty or that it may contain null arguments. Arguments are expected to have a
     * suitable toString method; there is no deep inspection.
     * @param prefix prefix in case the argument array is not empty
     * @param args an argument array as used in the reflection methods
     * 
     * @return an argument representation suitable for logging.
     */
    public static String formatArguments(final String prefix, final Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(prefix);
        sb.append('(');
        for (final Object arg : args) {
            if (arg == null) {
                sb.append("null");
            } else {
                sb.append(arg.toString());
            }
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()); // remove trailing ", "
        sb.append(')');
        return sb.toString();
    }
}
