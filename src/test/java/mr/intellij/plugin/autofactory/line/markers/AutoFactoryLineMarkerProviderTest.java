package mr.intellij.plugin.autofactory.line.markers;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.*;
import mr.intellij.plugin.autofactory.BaseFixturedTest;
import mr.intellij.plugin.autofactory.TestUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AutoFactoryLineMarkerProvider}.
 */
public class AutoFactoryLineMarkerProviderTest extends BaseFixturedTest {

    private static PsiClass samplePsiClass;

    private AutoFactoryLineMarkerProvider tested;

    @BeforeClass
    public static void setupClass() throws Exception {
        samplePsiClass = codeInsightFixture.addClass(TestUtils.loadResource(AutoFactoryLineMarkerProviderTest.class,
                                                                            "Sample.java"));
    }

    @Before
    public void setup() throws Exception {
        tested = new AutoFactoryLineMarkerProvider();
    }

    @Test
    public void testGetLineMarkerForUnsupportedElement() {
        assertThat(tested.getLineMarkerInfo(samplePsiClass)).isNull();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testGetLineMarkerForSomeMethodIdentifier() {
        ApplicationManager.getApplication().runReadAction(() -> {
            PsiIdentifier nameIdentifier = samplePsiClass.findMethodsByName("foo", false)[0].getNameIdentifier();
            LineMarkerInfo<?> lineMarkerInfo = tested.getLineMarkerInfo(nameIdentifier);
            assertThat(lineMarkerInfo).isNull();
        });
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testGetLineMarkerForAutoFactoryConstructorIdentifier() {
        ApplicationManager.getApplication().runReadAction(() -> {
            PsiIdentifier nameIdentifier = samplePsiClass.getConstructors()[0].getNameIdentifier();
            LineMarkerInfo<?> lineMarkerInfo = tested.getLineMarkerInfo(nameIdentifier);
            assertThat(lineMarkerInfo.getElement()).isEqualTo(nameIdentifier);
        });
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testGetLineMarkerForNotAutoFactoryConstructorIdentifier() {
        ApplicationManager.getApplication().runReadAction(() -> {
            PsiIdentifier nameIdentifier = samplePsiClass.getConstructors()[1].getNameIdentifier();
            LineMarkerInfo<?> lineMarkerInfo = tested.getLineMarkerInfo(nameIdentifier);
            assertThat(lineMarkerInfo).isNull();
        });
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testGetLineMarkerForIrrelevantExpression() {
        ApplicationManager.getApplication().runReadAction(() -> {
            PsiMethod foo = samplePsiClass.findMethodsByName("foo", false)[0];
            LineMarkerInfo<?> lineMarkerInfo = tested.getLineMarkerInfo(foo.getBody().getStatements()[0]);
            assertThat(lineMarkerInfo).isNull();
        });
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testGetLineMarkerForFactoryExpression() {
        ApplicationManager.getApplication().runReadAction(() -> {
            PsiMethod foo = samplePsiClass.findMethodsByName("foo", false)[0];
            PsiStatement statement = foo.getBody().getStatements()[1];
            PsiElement expression = statement.getFirstChild();
            LineMarkerInfo<?> lineMarkerInfo = tested.getLineMarkerInfo(expression);
            assertThat(lineMarkerInfo.getElement()).isEqualTo(expression);
        });
    }
}
