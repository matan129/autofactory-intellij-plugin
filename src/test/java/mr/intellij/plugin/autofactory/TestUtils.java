package mr.intellij.plugin.autofactory;

import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.Charset;

public class TestUtils {

    public static String loadResource(String name) throws IOException {
        return Resources.toString(Resources.getResource(name), Charset.defaultCharset());
    }
}
