package org.esa.nest.dataio.ceos.radarsat;

import com.bc.ceres.core.ProgressMonitor;
import org.esa.beam.framework.datamodel.ProductData;
import org.esa.nest.dataio.ceos.CEOSImageFile;
import org.esa.nest.dataio.ceos.CeosFileReader;
import org.esa.nest.dataio.ceos.IllegalCeosFormatException;
import org.esa.nest.dataio.ceos.ers.ERSImageFDR;
import org.esa.nest.dataio.ceos.records.ImageRecord;

import javax.imageio.stream.ImageInputStream;
import java.io.IOException;

/*
 * $Id: RadarsatImageFile.java,v 1.5 2008-07-23 19:47:17 lveci Exp $
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


/**
 * This class represents an image file of an Avnir-2 product.
 *
 * @author Marco Peters
 * @version $Revision: 1.5 $ $Date: 2008-07-23 19:47:17 $
 */
class RadarsatImageFile extends CEOSImageFile {

    private final ERSImageFDR _imageFDR;
    private final int _imageNumber;

    private static String mission = "radarsat";
    private static String image_recordDefinitionFile = "image_file.xml";
    private static String image_recordDefinition = "image_record.xml";

    public RadarsatImageFile(final ImageInputStream imageStream) throws IOException,
                                                                      IllegalCeosFormatException {
        _ceosReader = new CeosFileReader(imageStream);
        _imageFDR = new ERSImageFDR(_ceosReader, -1, mission, image_recordDefinitionFile);
        _ceosReader.seek(_imageFDR.getAbsolutPosition(_imageFDR.getRecordLength()));
        _imageRecords = new ImageRecord[_imageFDR.getAttributeInt("Number of lines per data set")];
        _imageRecords[0] = new ImageRecord(_ceosReader, -1, mission, image_recordDefinition);
        _imageRecordLength = _imageRecords[0].getRecordLength();
        _startPosImageRecords = _imageRecords[0].getStartPos();
        _imageNumber = 1;
    }

    public String getBandName() {
        return RadarsatConstants.BANDNAME_PREFIX + _imageNumber;
    }

    public String getBandDescription() {
        return "";
    }

    public int getBandIndex() {
        return _imageNumber;
    }

    public int getRasterWidth() {
        return _imageFDR.getAttributeInt("Number of pixels per line per SAR channel");//getNumImagePixelsPerLine();
    }

    public int getRasterHeight() {
        return _imageFDR.getAttributeInt("Number of lines per data set");
    }

    public static String getGeophysicalUnit() {
        return RadarsatConstants.GEOPHYSICAL_UNIT;
    }


 /*
    public int getTotalMillisInDayOfLine(final int y) throws IOException,
                                                             IllegalCeosFormatException {
        return getImageRecord(y).getScanStartTimeMillisAtDay();
    }

    public int getMicrosecondsOfLine(final int y) throws IOException,
                                                         IllegalCeosFormatException {
        return getImageRecord(y).getScanStartTimeMicros();
    }   */

    public void readBandRasterData(final int sourceOffsetX, final int sourceOffsetY,
                                   final int sourceWidth, final int sourceHeight,
                                   final int sourceStepX, final int sourceStepY,
                                   final int destOffsetX, final int destOffsetY,
                                   final int destWidth, final int destHeight,
                                   final ProductData destBuffer, ProgressMonitor pm) throws IOException,
                                                                                            IllegalCeosFormatException
    {
        final int sourceMaxY = sourceOffsetY + sourceHeight - 1;
        ImageRecord imageRecord;

        pm.beginTask("Reading band '" + getBandName() + "'...", sourceMaxY - sourceOffsetY);
        try {
            final byte[] srcLine = new byte[sourceWidth];
            final byte[] destLine = new byte[destWidth];
            for (int y = sourceOffsetY; y <= sourceMaxY; y += sourceStepY) {
                if (pm.isCanceled()) {
                    break;
                }

                // Read source line
                imageRecord = getImageRecord(y);
                _ceosReader.seek(imageRecord.getImageDataStart() + sourceOffsetX);
                _ceosReader.readB1(srcLine);

                // Copy source line into destination buffer
                final int currentLineIndex = (y - sourceOffsetY) * destWidth;
                if (sourceStepX == 1) {

                    System.arraycopy(srcLine, 0, destBuffer.getElems(), currentLineIndex, destWidth);
                } else {
                    copyLine(srcLine, destLine, sourceStepX);

                    System.arraycopy(destLine, 0, destBuffer.getElems(), currentLineIndex, destWidth);
                }

                pm.worked(1);
            }

        } finally {
            pm.done();
        }
    }

    public void readBandRasterDataSLC(final int sourceOffsetX, final int sourceOffsetY,
                                   final int sourceWidth, final int sourceHeight,
                                   final int sourceStepX, final int sourceStepY,
                                   final int destOffsetX, final int destOffsetY,
                                   final int destWidth, final int destHeight,
                                   final ProductData destBuffer, boolean oneOf2,
                                   ProgressMonitor pm) throws IOException, IllegalCeosFormatException
    {
        final int sourceMaxY = sourceOffsetY + sourceHeight - 1;
        ImageRecord imageRecord;

        int x = sourceOffsetX * 2;

        pm.beginTask("Reading band '" + getBandName() + "'...", sourceMaxY - sourceOffsetY);
        try {
            final byte[] srcLine = new byte[sourceWidth * 2];
            final byte[] destLine = new byte[destWidth];
            for (int y = sourceOffsetY; y <= sourceMaxY; y += sourceStepY) {
                if (pm.isCanceled()) {
                    break;
                }

                // Read source line
                imageRecord = getImageRecord(y);
                _ceosReader.seek(imageRecord.getImageDataStart() + x);
                _ceosReader.readB1(srcLine);

                // Copy source line into destination buffer
                final int currentLineIndex = (y - sourceOffsetY) * destWidth;
                if (oneOf2)
                    copyLine1Of2(srcLine, destLine, sourceStepX);
                else
                    copyLine2Of2(srcLine, destLine, sourceStepX);

                System.arraycopy(destLine, 0, destBuffer.getElems(), currentLineIndex, destWidth);

                pm.worked(1);
            }

        } finally {
            pm.done();
        }
    }

}