package mr.intellij.plugin.autofactory.line.markers;

import com.google.auto.factory.processor.AutoFactoryProcessor;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Generated;
import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static mr.intellij.plugin.autofactory.utils.AnnotationUtils.hasAutoFactory;

/**
 * Marks AutoFactory-enabled constructors and relevant factory calls (like {@code FooFactory.create(..)}).
 */
public class AutoFactoryLineMarkerProvider implements LineMarkerProvider {

    private static final Icon FACTORY_ICON = IconLoader.getIcon("/icons/factory.png");
    private static final String FACTORY_METHOD_NAME = "create";
    private static final String GENERATED_VALUE_NAME = "value";

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (element instanceof PsiIdentifier && element.getContext() instanceof PsiMethod) {
            PsiMethod method = (PsiMethod) element.getContext();

            if (method.isConstructor()) {
                return markConstructor(method);
            }

        } else if (element instanceof PsiMethodCallExpression) {
            return markMethodCallExpression((PsiMethodCallExpression) element);
        }

        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {
    }

    private LineMarkerInfo<?> markConstructor(PsiMethod constructor) {
        return Optional.of(constructor)
                       .filter(c -> hasAutoFactory(c, true))
                       .map(PsiNameIdentifierOwner::getNameIdentifier)
                       .map(element -> createMarker(element, new FindAutoFactoryUsagesHandler()))
                       .orElse(null);
    }

    private LineMarkerInfo<?> markMethodCallExpression(PsiMethodCallExpression expression) {
        return Optional.ofNullable((PsiMethod) expression.getMethodExpression().resolve())
                       .filter(psiMethod -> psiMethod.getName().equals(FACTORY_METHOD_NAME))
                       .map(PsiMethod::getContainingClass)
                       .map(PsiClass::getModifierList)
                       .map(psiModifierList -> psiModifierList.findAnnotation(Generated.class.getName()))
                       .map(generatedAnnotation -> generatedAnnotation.findDeclaredAttributeValue(GENERATED_VALUE_NAME))
                       .filter(value -> value instanceof PsiLiteralExpression)
                       .map(value -> (PsiLiteralExpression) value)
                       .filter(literal -> String.valueOf(literal.getValue())
                                                .equals(AutoFactoryProcessor.class.getName()))
                       .map(literal -> createMarker(expression, new NavigateToAutoFactoryHandler()))
                       .orElse(null);
    }

    private static <T extends PsiElement> LineMarkerInfo<T> createMarker(T element,
                                                                         GutterIconNavigationHandler<T> navHandler) {

        return new LineMarkerInfo<>(element, element.getTextRange(), FACTORY_ICON, Pass.LINE_MARKERS, null, navHandler,
                                    GutterIconRenderer.Alignment.LEFT);
    }
}
