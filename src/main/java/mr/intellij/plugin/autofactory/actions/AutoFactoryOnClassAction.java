package mr.intellij.plugin.autofactory.actions;

import com.google.auto.factory.AutoFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiModifierList;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.intellij.codeInsight.generation.OverrideImplementUtil.getContextClass;
import static mr.intellij.plugin.autofactory.utils.PsiUtils.createFormattedAnnotation;
import static mr.intellij.plugin.autofactory.utils.PsiUtils.isAnnotationPresent;

/**
 * Provides a way to add {@code @AutoFactory} annotation to a class.
 */
public class AutoFactoryOnClassAction extends BaseAction {

    public AutoFactoryOnClassAction() {
        super(new BaseCodeInsightActionHandler() {

            @Override
            protected boolean doInvoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
                return Optional.ofNullable(getContextClass(project, editor, file, false))
                        .map(this::addAnnotation)
                        .isPresent();
            }

            @SuppressWarnings("ConstantConditions")
            private boolean addAnnotation(@NotNull PsiClass psiClass) {
                PsiModifierList psiModifierList = psiClass.getModifierList();

                if (isAnnotationPresent(psiModifierList, AutoFactory.class)) {
                    return false;
                }

                psiModifierList.addAfter(createFormattedAnnotation(psiClass, AutoFactory.class), null);
                return true;
            }
        });
    }
}
