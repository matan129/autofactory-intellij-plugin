package mr.intellij.plugin.autofactory.elements;

import com.google.auto.factory.Provided;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightMethodBuilder;
import com.intellij.psi.impl.light.LightPsiClassBuilder;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import mr.intellij.plugin.autofactory.utils.AnnotationUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Finds AutoFactory-generated classes so that code using the factories will look valid, even before compiling.
 */
public class AutoFactoryFinder extends PsiElementFinder {

    public static final String FACTORY_SUFFIX = "Factory";

    @Nullable
    @Override
    public PsiClass findClass(@NotNull String qualifiedName, @NotNull GlobalSearchScope scope) {
        if (!qualifiedName.endsWith(FACTORY_SUFFIX)) {
            // TODO support custom named factories
            return null;
        }

        // TODO build actual cache of @AutoFactory usages instead of finding the annotated class by name.
        String factoryClassName = getClassName(qualifiedName);
        String annotatedName = factoryClassName.substring(0, qualifiedName.length() - FACTORY_SUFFIX.length());
        PsiClass annotated = PsiShortNamesCache.getInstance(scope.getProject())
                                               .getClassesByName(annotatedName, scope)[0];

        LightPsiClassBuilder factoryClass = new LightPsiClassBuilder(annotated, factoryClassName);

        Arrays.stream(annotated.getConstructors())
              .filter(AnnotationUtils::hasAutoFactory)
              .forEach(constructor -> addConstructor(factoryClass, constructor));

        return factoryClass;
    }

    private void addConstructor(LightPsiClassBuilder factoryClass, PsiMethod constructor) {

        LightMethodBuilder createBuilder = new LightMethodBuilder(factoryClass, JavaLanguage.INSTANCE)
                .addModifier("public");

        LightMethodBuilder constructorBuilder = new LightMethodBuilder(factoryClass, JavaLanguage.INSTANCE)
                .setConstructor(true)
                .addModifier("public");

        for (PsiParameter parameter : constructor.getParameterList().getParameters()) {
            if (AnnotationUtils.isAnnotationPresent(parameter.getModifierList(), Provided.class)) {
                addParameterToMethod(parameter, constructorBuilder);
            } else {
                addParameterToMethod(parameter, createBuilder);
            }
        }

        factoryClass.addMethod(createBuilder);
        factoryClass.addMethod(constructorBuilder);
    }

    private String getClassName(@NotNull String qualifiedName) {
        if (qualifiedName.contains(".")) {
            String[] parts = qualifiedName.split(".");
            return parts[parts.length - 1];
        }

        return qualifiedName;
    }

    private void addParameterToMethod(PsiParameter parameter, LightMethodBuilder methodBuilder) {
        methodBuilder.addParameter(parameter.getName(), parameter.getType());
    }

    @NotNull
    @Override
    public PsiClass[] findClasses(@NotNull String qualifiedName, @NotNull GlobalSearchScope scope) {
        return PsiClass.EMPTY_ARRAY;
    }
}
