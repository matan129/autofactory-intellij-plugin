package mr.intellij.plugin.autofactory.utils;

import com.google.auto.factory.AutoFactory;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class AnnotationUtils {

    public static boolean hasAutoFactory(@Nullable PsiMethod psiMethod, boolean includeClass) {
        return psiMethod != null &&
               (hasAutoFactory(psiMethod) || (includeClass && hasAutoFactory(psiMethod.getContainingClass())));
    }

    public static boolean hasAutoFactory(@Nullable PsiModifierListOwner owner) {
        return owner != null && isAnnotationPresent(owner.getModifierList(), AutoFactory.class);
    }

    public static Optional<PsiAnnotation> findAutoFactory(@Nullable PsiModifierListOwner owner) {
        if (owner != null && owner.getModifierList() != null) {
            return Optional.ofNullable(owner.getModifierList().findAnnotation(AutoFactory.class.getName()));
        }

        return Optional.empty();
    }

    public static boolean isAnnotationPresent(@Nullable PsiModifierList psiModifierList,
                                              @NotNull Class<? extends Annotation> annotationClass) {

        return psiModifierList != null && psiModifierList.findAnnotation(annotationClass.getName()) != null;
    }
}
