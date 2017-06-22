package mr.intellij.plugin.autofactory.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public abstract class BaseAction extends BaseGenerateAction {

    public BaseAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        return file.isWritable() && super.isValidForFile(project, editor, file);
    }
}
