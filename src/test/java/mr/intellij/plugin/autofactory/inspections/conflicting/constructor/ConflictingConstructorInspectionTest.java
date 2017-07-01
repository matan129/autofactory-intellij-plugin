package mr.intellij.plugin.autofactory.inspections.conflicting.constructor;

import mr.intellij.plugin.autofactory.inspections.BaseInspectionTest;

/**
 * Tests for {@link ConflictingConstructorInspection}.
 */
public class ConflictingConstructorInspectionTest extends BaseInspectionTest {

    public ConflictingConstructorInspectionTest() {
        super(ConflictingConstructorInspection.class, ConflictingConstructorVisitor.class);
    }
}
