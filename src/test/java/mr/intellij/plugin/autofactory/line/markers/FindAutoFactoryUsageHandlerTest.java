package mr.intellij.plugin.autofactory.line.markers;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiIdentifier;
import mr.intellij.plugin.autofactory.BaseFixturedTest;
import mr.intellij.plugin.autofactory.TestUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link FindAutoFactoryUsagesHandler}.
 */
public class FindAutoFactoryUsageHandlerTest extends BaseFixturedTest {

    private static PsiClass samplePsiClass;

    private AutoFactoryLineMarkerProvider provider;
    private FindAutoFactoryUsagesHandler tested;

    @BeforeClass
    public static void setupClass() throws Exception {
        samplePsiClass = codeInsightFixture.addClass(TestUtils.loadResource("Sample.java"));
    }

    @Before
    public void setup() throws Exception {
        provider = new AutoFactoryLineMarkerProvider();
        tested = new FindAutoFactoryUsagesHandler();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testNavigate() {
        ApplicationManager.getApplication().runReadAction(() -> {
            PsiIdentifier nameIdentifier = samplePsiClass.getConstructors()[0].getNameIdentifier();
            LineMarkerInfo<?> lineMarkerInfo = provider.getLineMarkerInfo(nameIdentifier);

//            codeInsightFixture.
        });
    }
}
