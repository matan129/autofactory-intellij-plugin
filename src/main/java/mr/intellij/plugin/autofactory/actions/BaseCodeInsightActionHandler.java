package mr.intellij.plugin.autofactory.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.command.undo.UndoUtil.markPsiFileForUndo;

public abstract class BaseCodeInsightActionHandler implements CodeInsightActionHandler {

    @Override
    public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if(file.isWritable()) {
            if(doInvoke(project, editor, file)) {
                markPsiFileForUndo(file);
            }
        }
    }

    protected abstract boolean doInvoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file);
}
