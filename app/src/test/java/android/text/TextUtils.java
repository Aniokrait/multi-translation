package android.text;

/**
 * reference: <a href="https://stackoverflow.com/questions/50911878/java-lang-runtimeexception-method-isempty-in-android-text-textutils-not-mocked">stackoverflow</a>
 */
public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}
