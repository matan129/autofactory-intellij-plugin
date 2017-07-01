package mr.intellij.plugin.autofactory.inspections.misusing;


import mr.intellij.plugin.autofactory.inspections.BaseInspectionTest;

/**
 * Tests for {@link AutoFactoryOnClassAndConstructorInspection}.
 */
public class AutoFactoryOnClassAndConstructorInspectionTest extends BaseInspectionTest {

    public AutoFactoryOnClassAndConstructorInspectionTest() {
        super(AutoFactoryOnClassAndConstructorInspection.class, AutoFactoryOnClassAndConstructorVisitor.class);
    }
}
