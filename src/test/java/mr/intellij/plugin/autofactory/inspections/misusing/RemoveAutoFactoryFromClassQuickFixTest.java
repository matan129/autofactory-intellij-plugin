package mr.intellij.plugin.autofactory.inspections.misusing;

import mr.intellij.plugin.autofactory.inspections.BaseQuickFixTest;
import org.junit.Test;

import static org.mockito.Mockito.verify;

/**
 * Tests for {@link RemoveAutoFactoryFromClassQuickFix}.
 */
public class RemoveAutoFactoryFromClassQuickFixTest extends BaseQuickFixTest {

    public RemoveAutoFactoryFromClassQuickFixTest() {
        super(RemoveAutoFactoryFromClassQuickFix.class);
    }

    @Test
    public void testApplyFix() throws Exception {
        tested.applyFix(mockProject, mockProblemDescriptor);
        verify(mockProblemDescriptor.getPsiElement()).delete();
    }
}
