package some.fake.name;

@com.google.auto.factory.AutoFactory
class ClassWithConflictingConstructors {

    public ClassWithConflictingConstructors() {
    }

    public ClassWithConflictingConstructors(@com.google.auto.factory.Provided String someArg) {
    }
}
