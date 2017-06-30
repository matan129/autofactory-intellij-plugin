package mr.intellij.plugin.autofactory.inspections.misusing;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Removes the {@literal @AutoFactory} annotation from the class.
 *
 * @see AutoFactoryOnClassAndConstructorInspection
 */
public class RemoveAutoFactoryFromClassQuickFix implements LocalQuickFix {

    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Remove @AutoFactory from class";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return AutoFactoryOnClassAndConstructorVisitor.FAMILY_NAME;
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        descriptor.getPsiElement().delete();
    }
}
