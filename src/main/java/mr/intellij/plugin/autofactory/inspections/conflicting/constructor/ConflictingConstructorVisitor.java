package mr.intellij.plugin.autofactory.inspections.conflicting.constructor;

import com.google.common.collect.Lists;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import lombok.RequiredArgsConstructor;
import mr.intellij.plugin.autofactory.utils.AnnotationUtils;
import mr.intellij.plugin.autofactory.utils.MethodUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @see ConflictingConstructorInspection
 */
@RequiredArgsConstructor
class ConflictingConstructorVisitor extends JavaElementVisitor {

    private final ProblemsHolder holder;

    @Override
    public void visitClass(PsiClass psiClass) {
        super.visitClass(psiClass);

        boolean classHasAutoFactory = AnnotationUtils.hasAutoFactory(psiClass);
        Arrays.stream(psiClass.getConstructors())
              .filter(constructor -> classHasAutoFactory || AnnotationUtils.hasAutoFactory(constructor))
              .collect(Collectors.toMap(
                      MethodUtils::getMethodParamTypesIgnoringProvided,
                      Lists::newArrayList,
                      (list1, list2) -> {
                          list1.addAll(list2);
                          return list1;
                      }))
              .entrySet()
              .stream()
              .filter(e -> e.getValue().size() > 1)
              .map(Map.Entry::getValue)
              .flatMap(Collection::stream)
              .forEach(psiMethod -> holder.registerProblem(
                      Optional.<PsiElement>ofNullable(psiMethod.getNameIdentifier()).orElse(psiMethod),
                      ConflictingConstructorInspection.DESCRIPTION,
                      ProblemHighlightType.GENERIC_ERROR
              ));
    }
}
