package mr.intellij.plugin.autofactory.utils;

import com.intellij.openapi.roots.TestSourcesFilter;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class ProjectFilesUtils {

    public static boolean isInTestFile(@NotNull PsiElement element) {
        return TestSourcesFilter.isTestSources(element.getContainingFile().getVirtualFile(), element.getProject());
    }
}
