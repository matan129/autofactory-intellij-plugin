package mr.intellij.plugin.autofactory.inspections.direct.instantiation;

import com.google.auto.factory.AutoFactory;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import lombok.RequiredArgsConstructor;

import static mr.intellij.plugin.autofactory.utils.PsiUtils.isAnnotationPresent;

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

        if (hasAutoFactory(expression.resolveConstructor())
                || hasAutoFactory(instantiatedClass)) {

            String problemDescription = String.format(DESCRIPTION_TEMPLATE, instantiatedClass.getName());
            holder.registerProblem(expression, problemDescription, (LocalQuickFix) null);
        }
    }

    private boolean hasAutoFactory(PsiModifierListOwner owner) {
        return owner != null && isAnnotationPresent(owner.getModifierList(), AutoFactory.class);
    }
}
