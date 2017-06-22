package mr.intellij.plugin.autofactory.inspections.direct.instantiation;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.roots.TestSourcesFilter;
import com.intellij.psi.*;
import lombok.RequiredArgsConstructor;

import static mr.intellij.plugin.autofactory.utils.PsiUtils.hasAutoFactory;

@RequiredArgsConstructor
class DirectInstantiationInspectionVisitor extends JavaElementVisitor {

    private static final String DESCRIPTION_TEMPLATE = "Using #ref instead of %s Factory.create()";

    private final ProblemsHolder holder;

    @Override
    public void visitNewExpression(PsiNewExpression expression) {
        super.visitNewExpression(expression);

        PsiJavaCodeReferenceElement reference = expression.getClassReference();
        if (reference == null) {
            return;
        }

        PsiClass instantiatedClass = (PsiClass) reference.resolve();
        if (instantiatedClass == null) {
            return;
        }

        boolean hasAutoFactory = hasAutoFactory(expression.resolveConstructor()) || hasAutoFactory(instantiatedClass);
        boolean isRelevant = !isInTestFile(expression) || isInTestFile(instantiatedClass);
        if (isRelevant && hasAutoFactory) {
            String problemDescription = String.format(DESCRIPTION_TEMPLATE, instantiatedClass.getName());
            holder.registerProblem(expression, problemDescription, (LocalQuickFix) null);
        }
    }

    private boolean isInTestFile(PsiElement element) {
        return TestSourcesFilter.isTestSources(element.getContainingFile().getVirtualFile(), element.getProject());
    }
}
