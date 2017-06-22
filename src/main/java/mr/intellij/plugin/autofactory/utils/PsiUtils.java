package mr.intellij.plugin.autofactory.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

public class PsiUtils {

    @NotNull
    public static PsiAnnotation createFormattedAnnotation(@NotNull PsiModifierListOwner psiModifierListOwner,
                                                          @NotNull Class<? extends Annotation> annotationClass) {

        PsiAnnotation psiAnnotation = createAnnotation(psiModifierListOwner, annotationClass);
        shortenReference(psiModifierListOwner, psiAnnotation);
        return psiAnnotation;
    }

    @NotNull
    public static PsiAnnotation createAnnotation(@NotNull PsiModifierListOwner psiModifierListOwner,
                                                 @NotNull Class<? extends Annotation> annotationClass) {

        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiModifierListOwner.getProject());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiModifierListOwner, PsiClass.class);
        return elementFactory.createAnnotationFromText("@" + annotationClass.getName(), psiClass);
    }

    public static boolean isAnnotationPresent(@Nullable PsiModifierList psiModifierList,
                                              @NotNull Class<? extends Annotation> annotationClass) {

        return psiModifierList != null && psiModifierList.findAnnotation(annotationClass.getName()) != null;
    }

    private static void shortenReference(@NotNull PsiModifierListOwner psiModifierListOwner,
                                         @NotNull PsiAnnotation psiAnnotation) {

        Project project = psiModifierListOwner.getProject();
        JavaCodeStyleManager javaCodeStyleManager = JavaCodeStyleManager.getInstance(project);
        javaCodeStyleManager.shortenClassReferences(psiAnnotation);
    }
}
