package mr.intellij.plugin.autofactory.inspections.misusing;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import lombok.RequiredArgsConstructor;

import static mr.intellij.plugin.autofactory.utils.PsiUtils.findAutoFactory;
import static mr.intellij.plugin.autofactory.utils.PsiUtils.hasAutoFactory;

@RequiredArgsConstructor
class AutoFactoryOnClassAndConstructorVisitor extends JavaElementVisitor {

    public static final String FAMILY_NAME = "AutoFactory";
    private static final LocalQuickFix[] QUICK_FIXES =
            new LocalQuickFix[] {
                    new RemoveAutoFactoryFromClassQuickFix(),
                    new RemoveAutoFactoryFromConstructorsQuickFix()
            };

    private final ProblemsHolder holder;

    @Override
    public void visitClass(PsiClass psiClass) {
        super.visitClass(psiClass);

        findAutoFactory(psiClass).ifPresent(autoFactory -> {
            for (PsiMethod constructor : psiClass.getConstructors()) {
                if (hasAutoFactory(constructor)) {
                    holder.registerProblem(autoFactory, AutoFactoryOnClassAndConstructorInspection.DESCRIPTION,
                            ProblemHighlightType.GENERIC_ERROR, QUICK_FIXES);
                }
            }
        });
    }
}
