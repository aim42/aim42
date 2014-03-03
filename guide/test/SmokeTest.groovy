import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test


class SmokeTest extends GroovyTestCase {

    final static String METHOD_GUIDE_TITLE = "Method Guide"
    final static String LICENSE_TITLE = "License"
    final static String ANALYZE_TITLE = "Analyze"




//    @Before
//    public void setup() throws Exception {
//
//        String buildDocsDir = System.getProperty("user.dir") + "/build/docs/"
//        indexFilePath = buildDocsDir + "index.html"
//        imageDirPath  = buildDocsDir + "images/"
//
//        indexFile = new File(indexFilePath)
//
//        //webClient = new WebClient()
//    }

    // @After
    // public void tearDown() {
    //    webClient.closeAllWindows()
    // }


    @Test
    public void testMethodGuideFileExist() {

        String userDir = System.getProperty("user.dir")


        String indexFilePath =  userDir + "/build/docs/index.html"
        File indexFile = new File( indexFilePath )

        assertTrue("index.html is supposed to live in build/docs directory",
                indexFile.exists())


        String imageDirPath  = userDir + "/build/docs/images"

        def imageDir = new File( imageDirPath )
        assertTrue("image directory is supposed to live in build/docs, but cannot be found",
                imageDir.exists())


    }


    @Test
    public void testHomePage() throws Exception {


        String userDir = System.getProperty("user.dir")
        String indexFileURL = "file://" + userDir + "/build/docs/index.html"


        final WebClient webClient = new WebClient()

        // we are expecting a content type of text/html
        // so we cast the result to an com.gargoylesoftware.htmlunit.html.HtmlPage
        final HtmlPage page = webClient.getPage( indexFileURL )

        assertEquals("title of index.html is not correct", METHOD_GUIDE_TITLE, page.titleText)
    }

}
