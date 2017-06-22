package mr.intellij.plugin.autofactory.inspections.direct.instantiation;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiNewExpression;
import lombok.RequiredArgsConstructor;

import static mr.intellij.plugin.autofactory.utils.PsiUtils.hasAutoFactory;
import static mr.intellij.plugin.autofactory.utils.PsiUtils.isInTestFile;

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
}
