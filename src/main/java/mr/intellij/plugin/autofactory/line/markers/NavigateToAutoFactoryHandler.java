package mr.intellij.plugin.autofactory.line.markers;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.util.PsiTypesUtil;
import mr.intellij.plugin.autofactory.utils.MethodUtils;

import java.awt.event.MouseEvent;
import java.util.Optional;

/**
 * Navigates to the relevant constructor upon marker click.
 */
public class NavigateToAutoFactoryHandler implements GutterIconNavigationHandler<PsiMethodCallExpression> {

    @Override
    public void navigate(MouseEvent mouseEvent, PsiMethodCallExpression expression) {
        PsiMethod createMethod = (PsiMethod) expression.getMethodExpression().resolve();
        if (createMethod == null) {
            return;
        }

        PsiClass instantiated = PsiTypesUtil.getPsiClass(createMethod.getReturnType());
        if (instantiated == null) {
            return;
        }

        Optional<PsiMethod> constructor = MethodUtils.findMatchingConstructor(instantiated, createMethod);

        if (constructor.isPresent()) {
            navigateToElement(constructor.get());
        } else {
            navigateToElement(instantiated);
        }
    }

    private void navigateToElement(PsiElement element) {
        Optional.of(element)
                .map(PsiElement::getNavigationElement)
                .filter(navElement -> navElement instanceof Navigatable)
                .map(navElement -> ((Navigatable) navElement))
                .filter(Navigatable::canNavigate)
                .ifPresent(navElement -> navElement.navigate(true));
    }
}
