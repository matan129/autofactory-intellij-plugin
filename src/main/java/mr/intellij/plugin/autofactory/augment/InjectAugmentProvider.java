package mr.intellij.plugin.autofactory.augment;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import mr.intellij.plugin.autofactory.utils.AnnotationUtils;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * Augments constructors with {@literal @AutoFactory} annotation with {@literal @Inject}, so other plugins,
 * like Guice / Dagger / etc. may pick this up and provide some additional info.
 *
 * Also, this makes the IDE suppress some annoying inspections.
 */
public class InjectAugmentProvider extends GuardedPsiAugmentProvider<PsiAnnotation> {

    protected InjectAugmentProvider() {
        super(PsiAnnotation.class);
    }

    @NotNull
    @Override
    protected List<PsiAnnotation> doGetAugments(@NotNull PsiElement element) {
        if (element instanceof PsiModifierList) {
            PsiModifierList modifierList = (PsiModifierList) element;

            if ((AnnotationUtils.isAnnotationPresent(modifierList, Inject.class, com.google.inject.Inject.class))) {
                return Collections.emptyList();
            }

            PsiElement context = modifierList.getContext();

            if (!(context instanceof PsiMethod)) {
                return Collections.emptyList();
            }

            PsiMethod psiMethod = (PsiMethod) context;

            if (!(psiMethod.isConstructor()) || !AnnotationUtils.hasAutoFactory(psiMethod, true)) {
                return Collections.emptyList();
            }

            return Collections.singletonList(AnnotationUtils.createAnnotation(modifierList.getProject(), Inject.class));
        }

        return Collections.emptyList();
    }
}
