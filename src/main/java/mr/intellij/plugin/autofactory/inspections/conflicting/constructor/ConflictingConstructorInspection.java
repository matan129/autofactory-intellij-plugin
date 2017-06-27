package mr.intellij.plugin.autofactory.inspections.conflicting.constructor;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Marks constructors that will have the same {@code FooFactory.create(...)} signature due to
 * {@link com.google.auto.factory.Provided} annotation. For example:
 *
 * <pre>
 *     &#064;AutoFactory
 *     class Foo {
 *
 *          public Foo() {
 *              // ...
 *          }
 *
 *          public Foo(&#064;Provided Bar injected) {
 *              // ...
 *          }
 *     }
 * </pre>
 */
public class ConflictingConstructorInspection extends BaseJavaLocalInspectionTool {

    public static final String DESCRIPTION = "@AutoFactory conflicting constructor";

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return DESCRIPTION;
    }

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new ConflictingConstructorVisitor(holder);
    }
}
