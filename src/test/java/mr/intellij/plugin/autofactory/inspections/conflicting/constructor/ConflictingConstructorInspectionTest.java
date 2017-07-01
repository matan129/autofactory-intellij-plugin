package mr.intellij.plugin.autofactory.inspections.conflicting.constructor;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ConflictingConstructorInspection}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConflictingConstructorInspectionTest {

    @Mock private ProblemsHolder mockProblemsHolder;
    private ConflictingConstructorInspection tested;

    @Before
    public void setup() {
        tested = new ConflictingConstructorInspection();
    }

    @Test
    public void testBuildVisitor() {
        PsiElementVisitor visitor = tested.buildVisitor(mockProblemsHolder, false);
        assertThat(visitor).isInstanceOf(ConflictingConstructorVisitor.class);
    }
}
