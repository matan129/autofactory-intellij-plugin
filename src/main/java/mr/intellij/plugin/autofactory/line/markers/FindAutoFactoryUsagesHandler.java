package mr.intellij.plugin.autofactory.line.markers;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.find.actions.ShowUsagesAction;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.ui.awt.RelativePoint;
import mr.intellij.plugin.autofactory.utils.MethodUtils;

import java.awt.event.MouseEvent;

/**
 * Provides find-usages popup for constructors. It show the usages of the generated factory method.
 */
public class FindAutoFactoryUsagesHandler implements GutterIconNavigationHandler<PsiElement> {

    private static final String FACTORY_METHOD_NAME = "create";
    private static final String FACTORY_CLASS_NAME_SUFFIX = "Factory";

    @Override
    public void navigate(MouseEvent mouseEvent, PsiElement nameIdentifier) {
        PsiMethod constructor = (PsiMethod) nameIdentifier.getContext();
        if (constructor == null || constructor.getContainingClass() == null) {
            return;
        }

        Project project = nameIdentifier.getProject();
        String factoryClassName = constructor.getContainingClass().getQualifiedName() + FACTORY_CLASS_NAME_SUFFIX;
        PsiClass factoryClass = JavaPsiFacade.getInstance(project)
                                             .findClass(factoryClassName, GlobalSearchScope.everythingScope(project));

        if (factoryClass == null) {
            return;
        }

        MethodUtils.findMatchingMethod(FACTORY_METHOD_NAME, factoryClass, constructor)
                   .ifPresent(psiMethod ->
                                      ((ShowUsagesAction) ActionManager.getInstance().getAction(ShowUsagesAction.ID))
                                              .startFindUsages(psiMethod,
                                                               new RelativePoint(mouseEvent),
                                                               PsiUtilBase.findEditor(constructor), 100));
    }
}
