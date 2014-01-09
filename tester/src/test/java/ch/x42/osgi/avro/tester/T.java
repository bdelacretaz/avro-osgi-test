package ch.x42.osgi.avro.tester;

import java.io.File;

/** Test setup */
class T {
    static final String BUNDLE_FILENAME = "bundle.filename";
    static final String TEST_BUNDLES_VERSION_PROP = "testbundles.version";
    
    static final File BUNDLE_FILE = new File(System.getProperty(BUNDLE_FILENAME, "BUNDLE_FILENAME_NOT_SET"));
    static final String TEST_BUNDLES_VERSION = System.getProperty(TEST_BUNDLES_VERSION_PROP, "TEST_BUDNLES_VERSION_NOT_SET");

}
