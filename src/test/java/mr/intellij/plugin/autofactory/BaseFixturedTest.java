package mr.intellij.plugin.autofactory;

import com.intellij.testFramework.fixtures.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Base test with Idea fixtures.
 */
public abstract class BaseFixturedTest {

    protected static JavaCodeInsightTestFixture codeInsightFixture;

    @BeforeClass
    public static void setupBaseFixtures() throws Exception {
        TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder = IdeaTestFixtureFactory.getFixtureFactory()
                                                                                          .createFixtureBuilder("");

        codeInsightFixture = JavaTestFixtureFactory.getFixtureFactory()
                                                   .createCodeInsightFixture(projectBuilder.getFixture());
        codeInsightFixture.setUp();
    }

    @AfterClass
    public static void teardownFixtures() throws Exception {
        codeInsightFixture.tearDown();
    }
}
