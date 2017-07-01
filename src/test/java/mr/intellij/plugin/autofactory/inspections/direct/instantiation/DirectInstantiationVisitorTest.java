package mr.intellij.plugin.autofactory.inspections.direct.instantiation;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNewExpression;
import mr.intellij.plugin.autofactory.utils.AnnotationUtils;
import mr.intellij.plugin.autofactory.utils.ProjectFilesUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Tests for {@link DirectInstantiationVisitor}.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(MockitoJUnitRunner.class)
@PrepareForTest({ProgressManager.class, AnnotationUtils.class, ProjectFilesUtils.class})
public class DirectInstantiationVisitorTest {

    @Mock private PsiNewExpression mockPsiNewExpression;
    @Mock private PsiJavaCodeReferenceElement mockJavaReferenceElement;
    @Mock private PsiClass mockPsiClass;
    @Mock private PsiMethod mockPsiMethod;
    @Mock private ProblemsHolder mockProblemsHolder;
    @InjectMocks private DirectInstantiationVisitor tested;

    @Before
    public void setup() throws Exception {
        // Suppress progress checks.
        mockStatic(ProgressManager.class);
    }

    @Test
    public void testWithoutClassReference() throws Exception {
        tested.visitNewExpression(mockPsiNewExpression);
        verifyZeroInteractions(mockProblemsHolder);
    }

    @Test
    public void testWithoutResolvedClassReference() throws Exception {
        when(mockPsiNewExpression.getClassReference()).thenReturn(mockJavaReferenceElement);
        tested.visitNewExpression(mockPsiNewExpression);
        verifyZeroInteractions(mockProblemsHolder);
    }

    @Test
    public void testWithoutAutoFactoryOnConstructor() throws Exception {
        when(mockPsiNewExpression.getClassReference()).thenReturn(mockJavaReferenceElement);
        when(mockJavaReferenceElement.resolve()).thenReturn(mockPsiClass);

        tested.visitNewExpression(mockPsiNewExpression);

        verifyZeroInteractions(mockProblemsHolder);
    }

    @Test
    public void testWithAutoFactoryOnConstructor() throws Exception {
        when(mockPsiNewExpression.getClassReference()).thenReturn(mockJavaReferenceElement);
        when(mockJavaReferenceElement.resolve()).thenReturn(mockPsiClass);
        when(mockPsiNewExpression.resolveConstructor()).thenReturn(mockPsiMethod);

        mockStatic(AnnotationUtils.class);
        PowerMockito.when(AnnotationUtils.class, mockPsiMethod, true).thenReturn(true);

        mockStatic(ProjectFilesUtils.class);
        PowerMockito.when(ProjectFilesUtils.class, mockPsiNewExpression).thenReturn(false);

        tested.visitNewExpression(mockPsiNewExpression);

        verify(mockProblemsHolder).registerProblem(eq(mockPsiNewExpression), anyString(), isNull());
    }

    @Test
    public void testWithAutoFactoryOnConstructorButInTestFile() throws Exception {
        when(mockPsiNewExpression.getClassReference()).thenReturn(mockJavaReferenceElement);
        when(mockJavaReferenceElement.resolve()).thenReturn(mockPsiClass);
        when(mockPsiNewExpression.resolveConstructor()).thenReturn(mockPsiMethod);

        mockStatic(AnnotationUtils.class);
        PowerMockito.when(AnnotationUtils.class, mockPsiMethod, true).thenReturn(true);

        mockStatic(ProjectFilesUtils.class);
        PowerMockito.when(ProjectFilesUtils.class, mockPsiNewExpression).thenReturn(true);

        tested.visitNewExpression(mockPsiNewExpression);

        verifyZeroInteractions(mockProblemsHolder);
    }
}
