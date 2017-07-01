package mr.intellij.plugin.autofactory.inspections.direct.instantiation;

import mr.intellij.plugin.autofactory.inspections.BaseInspectionTest;

/**
 * Tests for {@link DirectInstantiationInspection}.
 */
public class DirectInstantiationInspectionTest extends BaseInspectionTest {

    public DirectInstantiationInspectionTest() {
        super(DirectInstantiationInspection.class, DirectInstantiationVisitor.class);
    }
}
