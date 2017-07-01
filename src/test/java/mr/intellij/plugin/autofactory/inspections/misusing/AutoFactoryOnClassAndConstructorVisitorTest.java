package mr.intellij.plugin.autofactory.inspections.misusing;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import mr.intellij.plugin.autofactory.utils.AnnotationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Tests for {@link AutoFactoryOnClassAndConstructorVisitor}.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(MockitoJUnitRunner.class)
@PrepareForTest({ProgressManager.class, AnnotationUtils.class})
public class AutoFactoryOnClassAndConstructorVisitorTest {

    @Mock private ProblemsHolder mockProblemsHolder;
    @Mock private PsiClass mockPsiClass;
    @Mock private PsiAnnotation mockPsiAnnotation;
    @Mock private PsiMethod mockPsiMethod;
    @Captor private ArgumentCaptor<LocalQuickFix> quickFixesCaptor;
    @InjectMocks private AutoFactoryOnClassAndConstructorVisitor tested;

    @Before
    public void setup() throws Exception {
        // Suppress progress checks.
        mockStatic(ProgressManager.class);
    }

    @Test
    public void testVisitWithoutAutofactory() {
        mockStatic(AnnotationUtils.class);
        tested.visitClass(mockPsiClass);
        verifyZeroInteractions(mockProblemsHolder);
    }

    @Test
    public void testVisitWithAutofactory() {
        when(mockPsiClass.getConstructors()).thenReturn(new PsiMethod[] {mockPsiMethod});

        mockStatic(AnnotationUtils.class);
        doReturn(Optional.of(mockPsiAnnotation)).when(AnnotationUtils.class);
        AnnotationUtils.findAutoFactory(mockPsiClass);

        doReturn(true).when(AnnotationUtils.class);
        AnnotationUtils.hasAutoFactory(mockPsiMethod);

        tested.visitClass(mockPsiClass);

        verify(mockProblemsHolder).registerProblem(eq(mockPsiAnnotation),
                                                   anyString(),
                                                   eq(ProblemHighlightType.GENERIC_ERROR),
                                                   quickFixesCaptor.capture());

        List<LocalQuickFix> quickFixes = quickFixesCaptor.getAllValues();
        assertThat(quickFixes).hasSize(2);
        assertThat(quickFixes.get(0)).isInstanceOf(RemoveAutoFactoryFromClassQuickFix.class);
        assertThat(quickFixes.get(1)).isInstanceOf(RemoveAutoFactoryFromConstructorsQuickFix.class);
    }
}
