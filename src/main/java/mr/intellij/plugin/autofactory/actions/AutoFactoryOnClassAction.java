package mr.intellij.plugin.autofactory.actions;

import com.google.auto.factory.AutoFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import static com.intellij.codeInsight.generation.OverrideImplementUtil.getContextClass;
import static mr.intellij.plugin.autofactory.utils.PsiUtils.*;

/**
 * Provides a way to add {@code @AutoFactory} annotation to a class.
 */
public class AutoFactoryOnClassAction extends BaseAction {

    public AutoFactoryOnClassAction() {
        super(new BaseCodeInsightActionHandler() {

            @Override
            protected boolean doInvoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
                return addAnnotation(getContextClass(project, editor, file, false));
            }

            @SuppressWarnings("ConstantConditions")
            private boolean addAnnotation(PsiClass psiClass) {
                if (psiClass == null || hasAutoFactory(psiClass)) {
                    return false;
                }

                psiClass.getModifierList().addAfter(createFormattedAnnotation(psiClass, AutoFactory.class), null);
                return true;
            }
        });
    }
}
