package mr.intellij.plugin.autofactory.inspections;

import com.intellij.codeInspection.InspectionToolProvider;
import mr.intellij.plugin.autofactory.inspections.direct.instantiation.DirectInstantiationInspection;
import mr.intellij.plugin.autofactory.inspections.misusing.AutoFactoryOnClassAndConstructorInspection;

public class AutoFactoryInspectionsProvider implements InspectionToolProvider {

    @Override
    public Class[] getInspectionClasses() {
        return new Class[]{DirectInstantiationInspection.class, AutoFactoryOnClassAndConstructorInspection.class};
    }
}
