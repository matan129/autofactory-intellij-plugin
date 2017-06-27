package mr.intellij.plugin.autofactory.utils;

import com.google.auto.factory.Provided;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static mr.intellij.plugin.autofactory.utils.AnnotationUtils.isAnnotationPresent;

public class MethodUtils {

    /**
     * Finds a method with the given name on psiClass that has the same params as the other method.
     */
    public static Optional<PsiMethod> findMatchingMethod(String name, PsiClass psiClass, PsiMethod psiMethod) {
        return findMethodByParams(name, psiClass, getMethodParamTypesIgnoringProvided(psiMethod));
    }

    /**
     * Finds a constructor on psiClass that has the same parameters as the provided method.
     */
    public static Optional<PsiMethod> findMatchingConstructor(PsiClass psiClass, PsiMethod psiMethod) {
        return findConstructorByParams(psiClass, getMethodParamTypesIgnoringProvided(psiMethod));
    }

    public static Optional<PsiMethod> findMethodByParams(String name, PsiClass psiClass, List<PsiType> paramTypes) {
        return findMethodByParams(Arrays.stream(psiClass.getAllMethods())
                                        .filter(psiMethod -> psiMethod.getName().equals(name)),
                                  paramTypes);
    }

    public static Optional<PsiMethod> findConstructorByParams(PsiClass psiClass, List<PsiType> paramTypes) {
        return findMethodByParams(Arrays.stream(psiClass.getConstructors()), paramTypes);
    }

    private static Optional<PsiMethod> findMethodByParams(Stream<PsiMethod> methods, List<PsiType> paramTypes) {
        return methods.filter(psiMethod -> getMethodParamTypesIgnoringProvided(psiMethod).equals(paramTypes))
                      .findFirst();
    }

    public static List<PsiType> getMethodParamTypesIgnoringProvided(PsiMethod method) {
        return Arrays.stream(method.getParameterList().getParameters())
                     .filter(psiParameter -> !isAnnotationPresent(psiParameter.getModifierList(), Provided.class))
                     .map(PsiVariable::getType)
                     .collect(Collectors.toList());
    }
}
