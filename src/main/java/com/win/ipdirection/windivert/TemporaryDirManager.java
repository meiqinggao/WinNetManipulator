
package com.win.ipdirection.windivert;

import java.io.File;
import java.io.IOException;

/**
 * Interface useful to cover all code by tests
 */
public interface TemporaryDirManager {

    File createTempDir() throws IOException;
}
