package mr.intellij.plugin.autofactory.inspections;

import com.intellij.codeInspection.InspectionToolProvider;
import mr.intellij.plugin.autofactory.inspections.conflicting.constructor.ConflictingConstructorInspection;
import mr.intellij.plugin.autofactory.inspections.direct.instantiation.DirectInstantiationInspection;
import mr.intellij.plugin.autofactory.inspections.misusing.AutoFactoryOnClassAndConstructorInspection;
import org.jetbrains.annotations.NotNull;

/**
 * Provides IntelliJ with the plugin's custom inspections.
 */
public class AutoFactoryInspectionsProvider implements InspectionToolProvider {

    @NotNull
    @Override
    public Class[] getInspectionClasses() {
        return new Class[] {
                DirectInstantiationInspection.class,
                AutoFactoryOnClassAndConstructorInspection.class,
                ConflictingConstructorInspection.class
        };
    }
}
