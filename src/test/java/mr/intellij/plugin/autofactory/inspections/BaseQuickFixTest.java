package mr.intellij.plugin.autofactory.inspections;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Base tests for any {@link com.intellij.codeInspection.LocalQuickFix}es.
 */
@RequiredArgsConstructor
@RunWith(MockitoJUnitRunner.class)
public abstract class BaseQuickFixTest {

    private final Class<? extends LocalQuickFix> quickFixClass;

    @Mock protected Project mockProject;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) protected ProblemDescriptor mockProblemDescriptor;
    protected LocalQuickFix tested;

    @Before
    public void setup() throws Exception {
        tested = quickFixClass.newInstance();
    }

    @Test
    public void testStuff() throws Exception {
        tested.getName();
        tested.getFamilyName();
    }
}
