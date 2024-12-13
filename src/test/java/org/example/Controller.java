package org.example;

import org.junit.Test;
import util.ShellRunner;

public class Controller {
    private static final String TEST_TASK_FILE = "./src/test/resources/testTask.txt";
    private static final String MVN_RUN_TEST = "./.github/scripts/test/test_union.sh";
    @Test
    public void testUnion() throws Exception {
        ShellRunner shellRunner = new ShellRunner();
        shellRunner.runShellCommand(MVN_RUN_TEST);
    }
}
