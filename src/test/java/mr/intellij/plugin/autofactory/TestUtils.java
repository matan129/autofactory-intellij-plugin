package mr.intellij.plugin.autofactory;

import com.google.common.io.Resources;
import com.intellij.util.ReflectionUtil;

import java.io.IOException;
import java.nio.charset.Charset;

public class TestUtils {

    public static String loadPackageResource(String name) throws IOException {
        Class<?> ctx = ReflectionUtil.findCallerClass(2);
        return loadResource(ctx, name);
    }

    private static String loadResource(Class<?> ctx, String name) throws IOException {
        return Resources.toString(ctx.getResource(name), Charset.defaultCharset());
    }
}
