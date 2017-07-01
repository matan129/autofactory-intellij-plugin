package mr.intellij.plugin.autofactory;

import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class TestUtils {

    public static String loadResource(Class<?> ctx, String name) throws IOException {
        return Resources.toString(ctx.getResource(name), Charset.defaultCharset());
    }
}
