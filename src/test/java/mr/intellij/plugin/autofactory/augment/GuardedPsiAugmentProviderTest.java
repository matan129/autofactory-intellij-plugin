package mr.intellij.plugin.autofactory.augment;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link GuardedPsiAugmentProvider}.
 */
@RunWith(MockitoJUnitRunner.class)
public class GuardedPsiAugmentProviderTest {

    public static final Class<PsiElement> SUPPORTED_TYPE = PsiElement.class;
    public static final Class<PsiPackage> UNSUPPORTED_TYPE = PsiPackage.class;

    @Mock private PsiElement mockPsiElement;
    private GuardedPsiAugmentProvider tested;

    @Before
    public void setup() {
        tested = spy(new GuardedPsiAugmentProvider<PsiElement>(SUPPORTED_TYPE) {
            @Override
            protected List<PsiElement> doGetAugments(@NotNull PsiElement element) {
                return Collections.emptyList();
            }
        });
    }

    @Test
    public void testSuppression() {
        when(tested.doGetAugments(mockPsiElement)).then(invocation -> {
            tested.getAugments(invocation.getArgument(0), SUPPORTED_TYPE);
            return invocation.callRealMethod();
        });

        tested.getAugments(mockPsiElement, SUPPORTED_TYPE);

        verify(tested).doGetAugments(mockPsiElement);
        verifyNoMoreInteractions(tested, mockPsiElement);
    }

    @Test
    public void testCallWithUnsupportedType() {
        tested.getAugments(mockPsiElement, UNSUPPORTED_TYPE);

        verify(tested, never()).doGetAugments(any(PsiElement.class));
        verifyNoMoreInteractions(tested, mockPsiElement);
    }
}
