package mr.intellij.plugin.autofactory.inspections;

import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Base tests for any {@link BaseJavaLocalInspectionTool}s.
 */
@RequiredArgsConstructor
@RunWith(MockitoJUnitRunner.class)
public abstract class BaseInspectionTest {

    private final Class<? extends BaseJavaLocalInspectionTool> inspectionClass;
    private final Class<? extends PsiElementVisitor> visitorClass;

    @Mock private ProblemsHolder mockProblemsHolder;
    private BaseJavaLocalInspectionTool tested;

    @Before
    public void setup() throws Exception {
        tested = inspectionClass.newInstance();
    }

    @Test
    public void testBuildVisitor() throws Exception {
        PsiElementVisitor visitor = tested.buildVisitor(mockProblemsHolder, false);
        assertThat(visitor).isInstanceOf(visitorClass);
    }

    @Test
    public void testStuff() throws Exception {
        tested.isEnabledByDefault();
        tested.getDisplayName();
        tested.getGroupDisplayName();
    }
}
