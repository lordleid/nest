
package org.esa.nest.dataio.dem.srtm;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.esa.beam.framework.dataio.ProductIOPlugInManager;
import org.esa.beam.framework.dataio.ProductReaderPlugIn;

import java.util.Iterator;

/**
 * Created by Marco Peters.
 *
 * @author Marco Peters
 * @version $Revision: 1.2 $ $Date: 2008-01-28 14:40:11 $
 */
public class ReaderLoadedAsServiceTest extends TestCase {

    public void testReaderIsLoaded() {
      /*  int readerCount = 0;

        ProductIOPlugInManager plugInManager = ProductIOPlugInManager.getInstance();
        Iterator readerPlugIns = plugInManager.getReaderPlugIns("SRTM");

        while (readerPlugIns.hasNext()) {
            readerCount++;
            ProductReaderPlugIn plugIn = (ProductReaderPlugIn) readerPlugIns.next();
            System.out.println("readerPlugIn.Class = " + plugIn.getClass());
            System.out.println("readerPlugIn.Descr = " + plugIn.getDescription(null));
        }

        Assert.assertEquals(1, readerCount);      */

    }

}
