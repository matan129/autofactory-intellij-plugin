package mr.intellij.plugin.autofactory.inspections.direct.instantiation;

import com.intellij.codeInspection.InspectionToolProvider;

public class DirectInstantiationInspectionProvider implements InspectionToolProvider {

    @Override
    public Class[] getInspectionClasses() {
        return new Class[]{DirectInstantiationInspection.class};
    }
}
