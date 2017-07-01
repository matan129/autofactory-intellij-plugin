package mr.intellij.plugin.autofactory.inspections.misusing;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import mr.intellij.plugin.autofactory.inspections.BaseQuickFixTest;
import mr.intellij.plugin.autofactory.utils.AnnotationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Tests for {@link RemoveAutoFactoryFromConstructorsQuickFix}.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(MockitoJUnitRunner.class)
public class RemoveAutoFactoryFromConstructorQuickFixTest extends BaseQuickFixTest {

    @Mock private PsiClass mockPsiClass;
    @Mock private PsiMethod mockPsiMethod;
    @Mock private PsiMethod mockPsiMethodWithAutoFactory;
    @Mock private PsiAnnotation mockPsiAnnotation;

    public RemoveAutoFactoryFromConstructorQuickFixTest() {
        super(RemoveAutoFactoryFromConstructorsQuickFix.class);
    }

    @Test
    @PrepareForTest(AnnotationUtils.class)
    public void testApplyFix() throws Exception {
        when(mockProblemDescriptor.getPsiElement().getParent().getParent()).thenReturn(mockPsiClass);
        when(mockPsiClass.getConstructors()).thenReturn(new PsiMethod[]{mockPsiMethod, mockPsiMethodWithAutoFactory});

        mockStatic(AnnotationUtils.class);
        PowerMockito.doReturn(Optional.of(mockPsiAnnotation)).when(AnnotationUtils.class);
        AnnotationUtils.findAutoFactory(mockPsiMethodWithAutoFactory);

        tested.applyFix(mockProject, mockProblemDescriptor);

        verify(mockPsiAnnotation).delete();
    }
}
