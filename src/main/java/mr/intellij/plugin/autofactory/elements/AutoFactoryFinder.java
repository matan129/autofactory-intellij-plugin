package mr.intellij.plugin.autofactory.elements;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightMethod;
import com.intellij.psi.impl.light.LightModifierList;
import com.intellij.psi.impl.light.LightPsiClassBase;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        PsiClass annotated = PsiShortNamesCache.getInstance(scope.getProject())
                                               .getClassesByName(qualifiedName.substring(0, qualifiedName.length() - FACTORY_SUFFIX.length()), scope)[0];
        String[] parts;
        if (qualifiedName.contains(".")) {
            parts = qualifiedName.split(".");
        } else {
            parts = new String[] {qualifiedName};
        }
        String factoryClassName = parts[parts.length - 1];

        return new LightPsiClassBase(annotated.getManager(), JavaLanguage.INSTANCE, factoryClassName) {
            @NotNull
            @Override
            public PsiModifierList getModifierList() {
                return new LightModifierList(getManager(), getLanguage(), "public");
            }

            @Nullable
            @Override
            public PsiReferenceList getExtendsList() {
                // TODO
                return null;
            }

            @Nullable
            @Override
            public PsiReferenceList getImplementsList() {
                // TODO
                return null;
            }

            @NotNull
            @Override
            public PsiField[] getFields() {
                return PsiField.EMPTY_ARRAY;
            }

            @NotNull
            @Override
            public PsiMethod[] getMethods() {
                PsiElementFactory elementFactory = PsiElementFactory.SERVICE.getInstance(getProject());
                return new PsiMethod[] {new LightMethod(getManager(), elementFactory.createMethod("create", elementFactory.createType(annotated)), this)};
            }

            @NotNull
            @Override
            public PsiClass[] getInnerClasses() {
                return PsiClass.EMPTY_ARRAY;
            }

            @NotNull
            @Override
            public PsiClassInitializer[] getInitializers() {
                return PsiClassInitializer.EMPTY_ARRAY;
            }

            @Override
            public PsiElement getScope() {
                return null;
            }

            @Nullable
            @Override
            public PsiClass getContainingClass() {
                return null;
            }

            @Nullable
            @Override
            public PsiTypeParameterList getTypeParameterList() {
                return null;
            }
        };
    }

    @NotNull
    @Override
    public PsiClass[] findClasses(@NotNull String qualifiedName, @NotNull GlobalSearchScope scope) {
        return PsiClass.EMPTY_ARRAY; // TODO -- needed when multiple class with the same are declared in different packages.
    }
}
