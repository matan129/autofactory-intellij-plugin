package mr.intellij.plugin.autofactory.augment;

import com.google.auto.factory.AutoFactory;
import com.intellij.psi.*;
import mr.intellij.plugin.autofactory.utils.AnnotationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Tests for {@link InjectAugmentProvider}.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(MockitoJUnitRunner.class)
public class InjectAugmentProviderTest {

    public static final Class<PsiPackage> UNSUPPORTED_TYPE = PsiPackage.class;

    @Mock private PsiElement mockPsiElement;
    @Mock private PsiModifierList mockPsiModifierList;
    @Mock private PsiAnnotation mockPsiAnnotation;
    @Mock private PsiMethod mockPsiMethod;
    @Mock private PsiClass mockPsiClass;
    private InjectAugmentProvider tested;

    @Before
    public void setup() {
        tested = new InjectAugmentProvider();
    }

    @Test
    public void testGetAugmentsForUnsupportedType() {
        List<PsiPackage> augments = tested.getAugments(mockPsiElement, UNSUPPORTED_TYPE);
        assertThat(augments).isEmpty();
    }

    @Test
    public void testDoGetAugmentsForUnsupportedType() {
        List<PsiAnnotation> augments = tested.doGetAugments(mockPsiElement);
        assertThat(augments).isEmpty();
    }

    @Test
    public void testDoGetAugmentsWithInject() {
        when(mockPsiModifierList.findAnnotation(contains("Inject"))).thenReturn(mockPsiAnnotation);
        List<PsiAnnotation> augments = tested.doGetAugments(mockPsiModifierList);
        assertThat(augments).isEmpty();
    }

    @Test
    public void testDoGetAugmentsWithBadContext() {
        when(mockPsiModifierList.getContext()).thenReturn(mockPsiElement);
        List<PsiAnnotation> augments = tested.doGetAugments(mockPsiModifierList);
        assertThat(augments).isEmpty();
    }

    @Test
    public void testDoGetAugmentsWithNonConstructor() {
        when(mockPsiMethod.isConstructor()).thenReturn(false);
        when(mockPsiModifierList.getContext()).thenReturn(mockPsiMethod);

        List<PsiAnnotation> augments = tested.doGetAugments(mockPsiModifierList);

        assertThat(augments).isEmpty();
    }

    @Test
    public void testDoGetAugmentsWithConstructorWithoutAutoFactory() {
        when(mockPsiMethod.isConstructor()).thenReturn(true);
        when(mockPsiMethod.getContainingClass()).thenReturn(mockPsiClass);
        when(mockPsiModifierList.getContext()).thenReturn(mockPsiMethod);

        List<PsiAnnotation> augments = tested.doGetAugments(mockPsiModifierList);

        assertThat(augments).isEmpty();
    }

    @Test
    @PrepareForTest(AnnotationUtils.class)
    public void testDoGetAugmentsWithConstructorWithAutoFactory() {
        when(mockPsiMethod.isConstructor()).thenReturn(true);
        when(mockPsiModifierList.getContext()).thenReturn(mockPsiMethod);
        when(mockPsiMethod.getModifierList()).thenReturn(mockPsiModifierList);
        when(mockPsiModifierList.findAnnotation(AutoFactory.class.getName())).thenReturn(mockPsiAnnotation);

        mockStatic(AnnotationUtils.class, CALLS_REAL_METHODS);
        PowerMockito.doReturn(mockPsiAnnotation).when(AnnotationUtils.class);
        AnnotationUtils.createAnnotation(any(), any());

        List<PsiAnnotation> augments = tested.doGetAugments(mockPsiModifierList);

        assertThat(augments).containsExactly(mockPsiAnnotation);
    }
}
