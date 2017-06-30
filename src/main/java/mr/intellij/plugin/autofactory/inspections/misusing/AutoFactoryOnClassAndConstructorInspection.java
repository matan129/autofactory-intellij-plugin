package mr.intellij.plugin.autofactory.inspections.misusing;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Marks usages of {@literal @AutoFactory} on both constructors(s) and the class as errors.
 */
public class AutoFactoryOnClassAndConstructorInspection extends BaseJavaLocalInspectionTool {

    public static final String DESCRIPTION = "@AutoFactory on both class and constructor(s)";

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
        return new AutoFactoryOnClassAndConstructorVisitor(holder);
    }
}
