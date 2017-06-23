package mr.intellij.plugin.autofactory.utils;

import com.google.auto.factory.AutoFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.TestSourcesFilter;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class PsiUtils {

    public static PsiAnnotation createFormattedAnnotation(@NotNull PsiModifierListOwner psiModifierListOwner,
                                                          @NotNull Class<? extends Annotation> annotationClass) {

        PsiAnnotation psiAnnotation = createAnnotation(psiModifierListOwner, annotationClass);
        shortenReference(psiModifierListOwner, psiAnnotation);
        return psiAnnotation;
    }

    public static PsiAnnotation createAnnotation(@NotNull PsiModifierListOwner psiModifierListOwner,
                                                 @NotNull Class<? extends Annotation> annotationClass) {

        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiModifierListOwner.getProject());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiModifierListOwner, PsiClass.class);
        return elementFactory.createAnnotationFromText("@" + annotationClass.getName(), psiClass);
    }

    public static boolean hasAutoFactory(@Nullable PsiModifierListOwner owner) {
        return owner != null && isAnnotationPresent(owner.getModifierList(), AutoFactory.class);
    }

    public static Optional<PsiAnnotation> findAutoFactory(@Nullable PsiModifierListOwner owner) {
        if(owner != null && owner.getModifierList() != null) {
            return Optional.ofNullable(owner.getModifierList().findAnnotation(AutoFactory.class.getName()));
        }

        return Optional.empty();
    }

    public static boolean isAnnotationPresent(@Nullable PsiModifierList psiModifierList,
                                              @NotNull Class<? extends Annotation> annotationClass) {

        return psiModifierList != null && psiModifierList.findAnnotation(annotationClass.getName()) != null;
    }

    public static boolean isInTestFile(@NotNull PsiElement element) {
        return TestSourcesFilter.isTestSources(element.getContainingFile().getVirtualFile(), element.getProject());
    }

    private static void shortenReference(@NotNull PsiModifierListOwner psiModifierListOwner,
                                         @NotNull PsiAnnotation psiAnnotation) {

        Project project = psiModifierListOwner.getProject();
        JavaCodeStyleManager javaCodeStyleManager = JavaCodeStyleManager.getInstance(project);
        javaCodeStyleManager.shortenClassReferences(psiAnnotation);
    }
}
