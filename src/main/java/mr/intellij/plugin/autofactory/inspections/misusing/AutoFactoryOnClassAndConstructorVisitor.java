package mr.intellij.plugin.autofactory.inspections.misusing;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import lombok.RequiredArgsConstructor;
import mr.intellij.plugin.autofactory.utils.AnnotationUtils;

import java.util.Arrays;

import static mr.intellij.plugin.autofactory.utils.AnnotationUtils.findAutoFactory;

/**
 * @see AutoFactoryOnClassAndConstructorInspection
 */
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

        findAutoFactory(psiClass)
                .ifPresent(autoFactory -> Arrays.stream(psiClass.getConstructors())
                                                .filter(AnnotationUtils::hasAutoFactory)
                                                .findAny()
                                                .ifPresent(constructor -> holder.registerProblem(
                                                        autoFactory,
                                                        AutoFactoryOnClassAndConstructorInspection.DESCRIPTION,
                                                        ProblemHighlightType.GENERIC_ERROR,
                                                        QUICK_FIXES
                                                ))
                );
    }
}
