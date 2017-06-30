package mr.intellij.plugin.autofactory.augment;

import com.intellij.psi.PsiElement;
import com.intellij.psi.augment.PsiAugmentProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class GuardedPsiAugmentProvider<T extends PsiElement> extends PsiAugmentProvider {

    private boolean suppress = false;

    private final Class<T> supportedType;

    @NotNull
    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    protected final <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element,
                                                                   @NotNull Class<Psi> type) {

        if (suppress || supportedType != type) {
            return super.getAugments(element, type);
        }

        try (Suppressor ignored = new Suppressor()) {
            return (List<Psi>) doGetAugments(element);
        }
    }

    protected abstract List<T> doGetAugments(@NotNull PsiElement element);

    private class Suppressor implements AutoCloseable {

        public Suppressor() {
            suppress = true;
        }

        @Override
        public void close() throws Exception {
            suppress = false;
        }
    }
}
