/*
 * $Id: ProductDataUByteTest.java,v 1.1 2009-04-28 14:39:33 lveci Exp $
 *
 * Copyright (C) 2002 by Brockmann Consult (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation. This program is distributed in the hope it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.esa.beam.framework.datamodel;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.esa.beam.GlobalTestConfig;
import org.esa.beam.util.SystemUtils;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;

public class ProductDataUByteTest extends TestCase {

    private FileImageInputStream _inputStream;
    private FileImageOutputStream _outputStream;

    public ProductDataUByteTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(ProductDataUByteTest.class);
    }

    @Override
    protected void setUp() {
        File outputFile = GlobalTestConfig.getBeamTestDataOutputFile("ProductData");
        outputFile.mkdirs();
        File streamFile = new File(outputFile, "stream.img");
        try {
            streamFile.createNewFile();
            _inputStream = new FileImageInputStream(streamFile);
            _outputStream = new FileImageOutputStream(streamFile);
        } catch (IOException e) {
        }
        assertNotNull(_inputStream);
        assertNotNull(_outputStream);
    }

    @Override
    protected void tearDown() {
        try {
            _inputStream.close();
            _outputStream.close();
        } catch (IOException e) {
        }
        SystemUtils.deleteFileTree(GlobalTestConfig.getBeamTestDataOutputDirectory());
    }

    public void testSingleValueConstructor() {
        ProductData instance = ProductData.createInstance(ProductData.TYPE_UINT8);
        instance.setElems(new byte[]{-1});

        assertEquals(ProductData.TYPE_UINT8, instance.getType());
        assertEquals(255, instance.getElemInt());
        assertEquals(255L, instance.getElemUInt());
        assertEquals(255.0F, instance.getElemFloat(), 0.0e-12F);
        assertEquals(255.0D, instance.getElemDouble(), 0.0e-12D);
        assertEquals("255", instance.getElemString());
        assertEquals(1, instance.getNumElems());
        Object data = instance.getElems();
        assertEquals(true, data instanceof byte[]);
        assertEquals(1, ((byte[]) data).length);
        assertEquals(true, instance.isScalar());
        assertEquals(true, instance.isInt());
        assertEquals("255", instance.toString());

        ProductData expectedEqual = ProductData.createInstance(ProductData.TYPE_UINT8);
        expectedEqual.setElems(new byte[]{-1});
        assertEquals(true, instance.equalElems(expectedEqual));

        ProductData expectedUnequal = ProductData.createInstance(ProductData.TYPE_UINT8);
        expectedUnequal.setElems(new byte[]{-2});
        assertEquals(false, instance.equalElems(expectedUnequal));

//        StreamTest
        ProductData dataFromStream = null;
        try {
            instance.writeTo(_outputStream);
            dataFromStream = ProductData.createInstance(ProductData.TYPE_UINT8);
            dataFromStream.readFrom(_inputStream);
        } catch (IOException e) {
            fail("IOException not expected");
        }
        assertEquals(true, instance.equalElems(dataFromStream));
    }

    public void testConstructor() {
        ProductData instance = ProductData.createInstance(ProductData.TYPE_UINT8, 3);
        instance.setElems(new byte[]{-1, 127, -128});

        assertEquals(ProductData.TYPE_UINT8, instance.getType());
        assertEquals(255, instance.getElemIntAt(0));
        assertEquals(127, instance.getElemIntAt(1));
        assertEquals(128, instance.getElemIntAt(2));
        assertEquals(255L, instance.getElemUIntAt(0));
        assertEquals(127L, instance.getElemUIntAt(1));
        assertEquals(128L, instance.getElemUIntAt(2));
        assertEquals(255.0F, instance.getElemFloatAt(0), 0.0e-12F);
        assertEquals(127.0F, instance.getElemFloatAt(1), 0.0e-12F);
        assertEquals(128.0F, instance.getElemFloatAt(2), 0.0e-12F);
        assertEquals(255.0D, instance.getElemDoubleAt(0), 0.0e-12D);
        assertEquals(127.0D, instance.getElemDoubleAt(1), 0.0e-12D);
        assertEquals(128.0D, instance.getElemDoubleAt(2), 0.0e-12D);
        assertEquals("255", instance.getElemStringAt(0));
        assertEquals("127", instance.getElemStringAt(1));
        assertEquals("128", instance.getElemStringAt(2));
        assertEquals(3, instance.getNumElems());
        Object data2 = instance.getElems();
        assertEquals(true, data2 instanceof byte[]);
        assertEquals(3, ((byte[]) data2).length);
        assertEquals(false, instance.isScalar());
        assertEquals(true, instance.isInt());
        assertEquals("255,127,128", instance.toString());

        ProductData expectedEqual = ProductData.createInstance(ProductData.TYPE_UINT8, 3);
        expectedEqual.setElems(new byte[]{-1, 127, -128});
        assertEquals(true, instance.equalElems(expectedEqual));

        ProductData expectedUnequal = ProductData.createInstance(ProductData.TYPE_UINT8, 3);
        expectedUnequal.setElems(new byte[]{-1, 127, -127});
        assertEquals(false, instance.equalElems(expectedUnequal));

//        StreamTest
        ProductData dataFromStream = null;
        try {
            instance.writeTo(_outputStream);
            dataFromStream = ProductData.createInstance(ProductData.TYPE_UINT8, 3);
            dataFromStream.readFrom(_inputStream);
        } catch (IOException e) {
            fail("IOException not expected");
        }
        assertEquals(true, instance.equalElems(dataFromStream));
    }

    public void testSetElemsAsString() {
        final ProductData pd = ProductData.createInstance(ProductData.TYPE_UINT8, 3);
        pd.setElems(new String[]{
                String.valueOf(Byte.MAX_VALUE  * 2 + 1),
                String.valueOf(Byte.MAX_VALUE),
                String.valueOf(0),
        });

        assertEquals(Byte.MAX_VALUE * 2 +1, pd.getElemIntAt(0));
        assertEquals(Byte.MAX_VALUE, pd.getElemIntAt(1));
        assertEquals(0, pd.getElemIntAt(2));
}
    public void testSetElemsAsString_OutOfRange() {
        final ProductData pd1 = ProductData.createInstance(ProductData.TYPE_UINT8, 1);
        try {
            pd1.setElems(new String[]{String.valueOf(Byte.MAX_VALUE  * 2 + 2)});
        } catch (Exception e) {
            assertEquals(NumberFormatException.class, e.getClass());
            assertEquals("Value out of range. The value:'256' is not an unsigned byte value.", e.getMessage());
        }

        final ProductData pd2 = ProductData.createInstance(ProductData.TYPE_UINT8, 1);
        try {
            pd2.setElems(new String[]{String.valueOf(-1)});
        } catch (Exception e) {
            assertEquals(NumberFormatException.class, e.getClass());
            assertEquals("Value out of range. The value:'-1' is not an unsigned byte value.", e.getMessage());
        }
    }
}