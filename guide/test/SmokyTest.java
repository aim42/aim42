import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.*;
import com.gargoylesoftware.htmlunit.WebClient;


import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;


public class SmokyTest {


    final static String METHOD_GUIDE_TITLE = "Method Guide";
    final static String LICENSE_TITLE = "License";
    final static String ANALYZE_TITLE = "Analyze";

    static String userDir;
    static String buildDirPath;
    static String indexFilePath;
    static String imageDirPath;
    static String indexFileURL;

    static File indexFile;
    static File imageDir;

    static WebClient webClient;
    static HtmlPage indexPage;

    @BeforeClass
    public static void setup() throws Exception {

        userDir = System.getProperty("user.dir");
        buildDirPath = userDir + "/build/docs/";
        indexFilePath = buildDirPath + "index.html";
        imageDirPath = buildDirPath + "images/";

        indexFile = new File(indexFilePath);
        indexFileURL = "file://" + indexFilePath;

        webClient = new WebClient();

        // we are expecting a content type of text/html
        // so we cast the result to an com.gargoylesoftware.htmlunit.html.HtmlPage

        try {
            indexPage = webClient.getPage(indexFileURL);
        } catch (IOException e) {
            assertTrue("index.html should be readable by webClient, but threw exception!", false);
            e.printStackTrace();
        }
    }


    @AfterClass
    public static void tearDown() {
        webClient.closeAllWindows();
    }


    @Test
    public void doesFileExist() {

        assertTrue("index.html is supposed to live in build/docs directory",
                indexFile.exists());

        String imageDirPath = userDir + "/build/docs/images";

        File imageDir = new File(imageDirPath);
        assertTrue("image directory is supposed to live in build/docs, but cannot be found",
                imageDir.exists());

    }

    @Test
    public void doPhaseHeadingsExist() {

        assertEquals("title of index.html is not correct", METHOD_GUIDE_TITLE, indexPage.getTitleText());

        HtmlHeading2 heading2 = indexPage.getHtmlElementById("Analyze");

        assertNotNull("h2-Analyze seems to be missing", heading2);

        heading2 = indexPage.getHtmlElementById("Evaluate");
        assertNotNull("h2-Evaluate seems to be missing", heading2);

        heading2 = indexPage.getHtmlElementById("Improve");
        assertNotNull("h2-Improve seems to be missing", heading2);

        heading2 = indexPage.getHtmlElementById("Crosscutting");
        assertNotNull("h2-CrossCutting seems to be missing", heading2);

        //HtmlAnchor anchor = page.getAnchorByName("#Analyze");

    }

    // test for licence, contributions and team-information
    @Test
    public void doFormalSectionsExist() {
        HtmlHeading3 heading3;

        try {
            heading3 = indexPage.getHtmlElementById("_license");
            assertNotNull("License-section missing or misspelled", heading3);

        } catch (Exception e) {
        }
        try {
            heading3 = indexPage.getHtmlElementById("_the_team");
            assertNotNull("Team-section missing or misspelled", heading3);
        } catch (ElementNotFoundException e) {
            assertTrue("Element not found exception: team-heading", false);

        }
    }

    // test wether the local images contained in the image-subdirectory
    // are present and readable.
    @Test
    public void doImagesExist() {
        //get list of all images in html file
        final List<HtmlImage> images = (List<HtmlImage>) indexPage.getByXPath("//img");

        // the resulting images contains a list of the following form:
        // < [HtmlImage[<img src="images/aim42-logo.png" alt="aim42-logo">],
        //   [HtmlImage[<img src="https://travis-ci.org/aim42/aim42.png?branch=master" alt="unknown">],...>
        assertNotNull("List of images shall be nonNull but isn't", images);

        // we need to filter for images containing 'src="images/"'...
        String imagePath;

        for (HtmlImage hi : images) {
            imagePath = hi.getSrcAttribute();
            if (imagePath.startsWith("images/")) {
                assertFileExists( imagePath );
            }

        }

    }

    private void assertFileExists(String currentImagePath) {
        String imageFilePath = buildDirPath + currentImagePath;
        File imageFile = new File( imageFilePath );
        assertTrue( "image " + imageFilePath + " missing", imageFile.exists());

    }

    // the following code reduces the amount of log-info written by htmlunit-parser.
    static {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
    }

}
