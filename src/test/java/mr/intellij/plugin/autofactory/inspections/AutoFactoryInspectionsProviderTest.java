package mr.intellij.plugin.autofactory.inspections;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Sanity checks for {@link AutoFactoryInspectionsProvider}.
 */
public class AutoFactoryInspectionsProviderTest {

    private AutoFactoryInspectionsProvider tested;

    @Before
    public void setup() {
        tested = new AutoFactoryInspectionsProvider();
    }

    @Test
    public void testGetInspectionClasses() {
        assertThat(tested.getInspectionClasses()).hasSize(3);
    }
}
