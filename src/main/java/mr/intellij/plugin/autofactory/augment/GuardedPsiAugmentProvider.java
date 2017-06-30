package mr.intellij.plugin.autofactory.augment;

import com.intellij.psi.PsiElement;
import com.intellij.psi.augment.PsiAugmentProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * PsiAugment provider that is stack overflow resistant.
 *
 * The rational for such class is that {@link InjectAugmentProvider} is supposed to (sometimes) add {@code @Inject}
 * annotations on constructors, but in its {@code getAugments()} logic is check for other annotations presence, which
 * triggers another {@code getAugments()} and so on, causing stack overflow.
 *
 * This class fixes this issue by suppressing reentrant {@code getAugment()} calls.
 *
 * @param <T> The supported {@link PsiElement} (i.e. {@link com.intellij.psi.PsiAnnotation}).
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class GuardedPsiAugmentProvider<T extends PsiElement> extends PsiAugmentProvider {

    private final Class<T> supportedType;
    private boolean suppress = false;

    @NotNull
    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    protected final <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element,
                                                                   @NotNull Class<Psi> type) {

        if (suppress || supportedType != type) {
            return Collections.emptyList();
        }

        try {
            suppress = true;
            return (List<Psi>) doGetAugments(element);
        } finally {
            suppress = false;
        }
    }
    
    protected abstract List<T> doGetAugments(@NotNull PsiElement element);
}
