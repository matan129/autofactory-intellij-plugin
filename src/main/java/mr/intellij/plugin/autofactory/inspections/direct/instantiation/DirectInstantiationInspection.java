package mr.intellij.plugin.autofactory.inspections.direct.instantiation;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Warns when using {@code 'new Foo(...)'} if the matching constructor has {@literal @AutoFactory}.
 */
public class DirectInstantiationInspection extends BaseJavaLocalInspectionTool {

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "Invoking 'new' instead of the @AutoFactory method";
    }

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return GroupNames.ENCAPSULATION_GROUP_NAME;
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new DirectInstantiationVisitor(holder);
    }
}
