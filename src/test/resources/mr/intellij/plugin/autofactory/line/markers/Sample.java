package some.fake.name;

class Sample {

    @com.google.auto.factory.AutoFactory
    public Sample() {
    }

    public Sample(String s) {
    }

    public void foo() {
        System.out.println("Foo!");
        new SampleFactory().create();
    }

    @javax.annotation.Generated(
            value = "com.google.auto.factory.processor.AutoFactoryProcessor",
            comments = "https://github.com/google/auto/tree/master/factory"
    )
    private static final class SampleFactory {

        public Sample create() {
            return new Sample();
        }
    }
}
