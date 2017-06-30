package mr.intellij.plugin.autofactory.inspections.misusing;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import static mr.intellij.plugin.autofactory.utils.AnnotationUtils.findAutoFactory;

/**
 * Removes {@literal @AutoFactory} annotations from the constructors.
 *
 * @see AutoFactoryOnClassAndConstructorInspection
 */
public class RemoveAutoFactoryFromConstructorsQuickFix implements LocalQuickFix {

    @NotNull
    @Override
    public String getName() {
        return "Remove @AutoFactory from constructors";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return AutoFactoryOnClassAndConstructorVisitor.FAMILY_NAME;
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiClass parent = (PsiClass) descriptor.getPsiElement().getParent().getParent();
        for (PsiMethod constructor : parent.getConstructors()) {
            findAutoFactory(constructor).ifPresent(PsiElement::delete);
        }
    }
}
