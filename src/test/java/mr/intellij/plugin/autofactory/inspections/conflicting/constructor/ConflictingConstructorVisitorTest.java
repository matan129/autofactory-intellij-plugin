package mr.intellij.plugin.autofactory.inspections.conflicting.constructor;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.*;
import mr.intellij.plugin.autofactory.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link ConflictingConstructorVisitor}.
 */
class ConflictingConstructorVisitorTest {

    private static JavaCodeInsightTestFixture codeInsightFixture;
    private static PsiClass samplePsiClass;

    @Captor private ArgumentCaptor<PsiElement> psiElementCaptor;
    @Mock private ProblemsHolder mockProblemsHolder;
    @InjectMocks private ConflictingConstructorVisitor tested;

    @BeforeAll
    public static void setupClass() throws Exception {
        TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder = IdeaTestFixtureFactory.getFixtureFactory()
                                                                                          .createFixtureBuilder("");

        IdeaProjectTestFixture projectFixture = projectBuilder.getFixture();
        codeInsightFixture = JavaTestFixtureFactory.getFixtureFactory()
                                                   .createCodeInsightFixture(projectFixture);
        codeInsightFixture.setUp();
        samplePsiClass = codeInsightFixture.addClass(TestUtils.loadResource("ClassWithConflictingConstructors.java"));
    }

    @AfterAll
    public static void teardownClass() throws Exception {
        codeInsightFixture.tearDown();
    }

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testVisitClass() {
        ApplicationManager.getApplication().runReadAction(() -> tested.visitClass(samplePsiClass));

        verify(mockProblemsHolder, times(2)).registerProblem(psiElementCaptor.capture(),
                                                             eq(ConflictingConstructorInspection.DESCRIPTION),
                                                             eq(ProblemHighlightType.GENERIC_ERROR));
    }
}
