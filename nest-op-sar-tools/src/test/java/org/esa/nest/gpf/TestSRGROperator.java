package org.esa.nest.gpf;

import junit.framework.TestCase;
import org.esa.beam.framework.gpf.OperatorSpi;
import org.esa.beam.framework.gpf.GPF;
import org.esa.beam.framework.gpf.Operator;
import org.esa.beam.framework.datamodel.*;
import org.esa.beam.framework.dataop.maptransf.Datum;
import org.esa.nest.datamodel.AbstractMetadata;

import java.util.Arrays;

import com.bc.ceres.core.ProgressMonitor;

/**
 * Unit test for SRGROperator.
 */
public class TestSRGROperator extends TestCase {

    private OperatorSpi spi;

    @Override
    protected void setUp() throws Exception {
        spi = new SRGROp.Spi();
        GPF.getDefaultInstance().getOperatorSpiRegistry().addOperatorSpi(spi);
    }

    @Override
    protected void tearDown() throws Exception {
        GPF.getDefaultInstance().getOperatorSpiRegistry().removeOperatorSpi(spi);
    }

    /**
     * Tests SRGR operator with a 4x16 "DETECTED" test product.
     */
    public void testSRGROperator() throws Exception {

        Product sourceProduct = createTestProduct(16, 4);

        SRGROp op = (SRGROp)spi.createOperator();
        assertNotNull(op);
        op.setSourceProduct(sourceProduct);
        op.setNumOfRangePoints(6);
        op.setSourceBandName("band1");
        
        // get targetProduct: execute initialize()
        Product targetProduct = op.getTargetProduct();
        assertNotNull(targetProduct);

        Band band = targetProduct.getBandAt(0);
        assertNotNull(band);

        // readPixels: execute computeTiles()
        float[] floatValues = new float[28];
        band.readPixels(0, 0, 7, 4, floatValues, ProgressMonitor.NULL);

        // compare with expected outputs:
        float[] expectedValues = {1.0f, 3.0221484f, 5.0534487f, 7.0752897f, 9.097291f, 11.119023f, 13.130011f, 17.0f,
        19.022148f, 21.053448f, 23.075289f, 25.09729f, 27.119024f, 29.13001f, 33.0f, 35.02215f, 37.053448f, 39.07529f,
        41.09729f, 43.119022f, 45.13001f, 49.0f, 51.02215f, 53.053448f, 55.07529f, 57.09729f, 59.119022f, 61.13001f};
        assertTrue(Arrays.equals(expectedValues, floatValues));

        // compare updated metadata
        MetadataElement abs = targetProduct.getMetadataRoot().getElement("Abstracted Metadata");
        MetadataAttribute attr = abs.getAttribute(AbstractMetadata.srgr_flag);
        assertTrue(attr.getData().getElemBoolean());
    }


    /**
     * Creates a 4-by-16 test product as shown below:
     *  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16
     * 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32
     * 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48
     * 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63 64
     */
    private Product createTestProduct(int w, int h) {

        Product testProduct = new Product("source", "ASA_APS_1P", w, h);

        // create a Band: band1
        Band band1 = testProduct.addBand("band1", ProductData.TYPE_INT32);
        band1.setSynthetic(true);
        int[] intValues = new int[w * h];
        for (int i = 0; i < w * h; i++) {
            intValues[i] = i + 1;
        }
        band1.setData(ProductData.createInstance(intValues));

        // create abstracted metadata
        MetadataElement abs = new MetadataElement("Abstracted Metadata");

        abs.addAttribute(new MetadataAttribute(AbstractMetadata.MISSION,
                ProductData.createInstance("ENVISAT"), false));

        abs.addAttribute(new MetadataAttribute(AbstractMetadata.PASS,
                ProductData.createInstance("DESCENDING"), false));

        abs.addAttribute(new MetadataAttribute(AbstractMetadata.srgr_flag,
                ProductData.createInstance(new byte[] {0}), false));

        abs.addAttribute(new MetadataAttribute(AbstractMetadata.range_spacing,
                ProductData.createInstance(new float[] {7.0F}), false));

        abs.addAttribute(new MetadataAttribute(AbstractMetadata.azimuth_spacing,
                ProductData.createInstance(new float[] {4.0F}), false));

        // create incidence angle tie point grid
        float[] incidence_angle = new float[w*h];
        Arrays.fill(incidence_angle, 30.0f);
        testProduct.addTiePointGrid(new TiePointGrid("incident_angle", w, h, 0, 0, 1, 1, incidence_angle));

        // create lat/lon tie point grids
        float[] lat = new float[w*h];
        float[] lon = new float[w*h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = y*w + x;
                lon[i] = 13.20f + x/10000.0f;
                lat[i] = 51.60f + y/10000.0f;
            }
        }
        TiePointGrid latGrid = new TiePointGrid("latitude", w, h, 0, 0, 1, 1, lat);
        TiePointGrid lonGrid = new TiePointGrid("longitude", w, h, 0, 0, 1, 1, lon);
        testProduct.addTiePointGrid(latGrid);
        testProduct.addTiePointGrid(lonGrid);

        testProduct.getMetadataRoot().addElement(abs);

        // create geoCoding
        testProduct.setGeoCoding(new TiePointGeoCoding(latGrid, lonGrid));
        return testProduct;
    }
}