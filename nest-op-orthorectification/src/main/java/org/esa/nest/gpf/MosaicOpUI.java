/*
 * Copyright (C) 2011 by Array Systems Computing Inc. http://www.array.ca
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */
package org.esa.nest.gpf;

import org.esa.beam.framework.datamodel.MetadataElement;
import org.esa.beam.framework.dataop.maptransf.MapProjection;
import org.esa.beam.framework.dataop.maptransf.MapProjectionRegistry;
import org.esa.beam.framework.dataop.resamp.ResamplingFactory;
import org.esa.beam.framework.gpf.ui.BaseOperatorUI;
import org.esa.beam.framework.gpf.ui.UIValidation;
import org.esa.beam.framework.ui.AppContext;
import org.esa.nest.datamodel.AbstractMetadata;
import org.esa.nest.util.DialogUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * User interface for MosaicOp
 */
public class MosaicOpUI extends BaseOperatorUI {

    private final JList bandList = new JList();

    private final JComboBox resamplingMethod = new JComboBox(ResamplingFactory.resamplingNames);

    //private final JComboBox projectionName = new JComboBox();

    private final JTextField pixelSize = new JTextField("");
    private final JTextField sceneWidth = new JTextField("");
    private final JTextField sceneHeight = new JTextField("");
    private final JTextField feather = new JTextField("");
    private final JCheckBox averageCheckBox = new JCheckBox("Weighted Average of Overlap");
    private final JCheckBox normalizeByMeanCheckBox = new JCheckBox("Normalize By Mean");

    private boolean changedByUser = false;
    private boolean average = false;
    private boolean normalizeByMean = false;

    private double widthHeightRatio = 1;
    private double pixelSizeHeightRatio = 1;
    private final OperatorUtils.SceneProperties scnProp = new OperatorUtils.SceneProperties();

    @Override
    public JComponent CreateOpTab(String operatorName, Map<String, Object> parameterMap, AppContext appContext) {

        //final String[] projectionsValueSet = getProjectionsValueSet();
        //Arrays.sort(projectionsValueSet);
        //for(String name : projectionsValueSet) {
        //    projectionName.addItem(name);
        //}

        initializeOperatorUI(operatorName, parameterMap);
        final JComponent panel = createPanel();
        initParameters();

        //projectionName.setSelectedItem(IdentityTransformDescriptor.NAME);
        averageCheckBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    average = (e.getStateChange() == ItemEvent.SELECTED);
                }
        });

        normalizeByMeanCheckBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    normalizeByMean = (e.getStateChange() == ItemEvent.SELECTED);
                }
        });

        pixelSize.addKeyListener(new TextAreaKeyListener());
        sceneWidth.addKeyListener(new TextAreaKeyListener());
        sceneHeight.addKeyListener(new TextAreaKeyListener());

        return new JScrollPane(panel);
    }

    @Override
    public void initParameters() {

        OperatorUIUtils.initBandList(bandList, getBandNames());

        resamplingMethod.setSelectedItem(paramMap.get("resamplingMethod"));
        //projectionName.setSelectedItem(paramMap.get("projectionName"));

        Double pixSize = (Double)paramMap.get("pixelSize");
        if(pixSize == null) pixSize = 0.0;
        Integer width = (Integer)paramMap.get("sceneWidth");
        if(width == null) width = 0;
        Integer height = (Integer)paramMap.get("sceneHeight");
        if(height == null) height = 0;
        Integer featherVal = (Integer)paramMap.get("feather");
        if(featherVal == null) featherVal = 0;

        if(!changedByUser && sourceProducts != null) {
            try {
                OperatorUtils.computeImageGeoBoundary(sourceProducts, scnProp);

                final MetadataElement absRoot = AbstractMetadata.getAbstractedMetadata(sourceProducts[0]);
                final double rangeSpacing = AbstractMetadata.getAttributeDouble(absRoot, AbstractMetadata.range_spacing);
                final double azimuthSpacing = AbstractMetadata.getAttributeDouble(absRoot, AbstractMetadata.azimuth_spacing);
                final double minSpacing = Math.min(rangeSpacing, azimuthSpacing);
                pixSize = minSpacing;
                
                OperatorUtils.getSceneDimensions(minSpacing, scnProp);

                width = scnProp.sceneWidth;
                height = scnProp.sceneHeight;
                widthHeightRatio = width / (double)height;
                pixelSizeHeightRatio = pixSize / (double) height;

                long dim = (long)width*(long)height;
                while(dim > Integer.MAX_VALUE) {
                    width -= 1000;
                    height = (int)(width / widthHeightRatio);
                    dim = (long)width*(long)height;
                }
            } catch(Exception e) {
                width = 0;
                height = 0;
            }
        }

        pixelSize.setText(String.valueOf(pixSize));
        sceneWidth.setText(String.valueOf(width));
        sceneHeight.setText(String.valueOf(height));
        feather.setText(String.valueOf(featherVal));

        average = (Boolean)paramMap.get("average");
        averageCheckBox.getModel().setPressed(average);

        normalizeByMean = (Boolean)paramMap.get("normalizeByMean");
        normalizeByMeanCheckBox.getModel().setPressed(normalizeByMean);

    }

    @Override
    public UIValidation validateParameters() {

        return new UIValidation(UIValidation.State.OK, "");
    }

    @Override
    public void updateParameters() {

        OperatorUIUtils.updateBandList(bandList, paramMap, OperatorUIUtils.SOURCE_BAND_NAMES);
        paramMap.put("resamplingMethod", resamplingMethod.getSelectedItem());
        //paramMap.put("projectionName", projectionName.getSelectedItem());
        paramMap.put("pixelSize", Double.parseDouble(pixelSize.getText()));
        paramMap.put("sceneWidth", Integer.parseInt(sceneWidth.getText()));
        paramMap.put("sceneHeight", Integer.parseInt(sceneHeight.getText()));
        paramMap.put("feather", Integer.parseInt(feather.getText()));

        paramMap.put("average", average);
        paramMap.put("normalizeByMean", normalizeByMean);
    }

    private JComponent createPanel() {

        final JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = DialogUtils.createGridBagConstraints();

        contentPane.add(new JLabel("Source Bands:"), gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        contentPane.add(new JScrollPane(bandList), gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy++;
        DialogUtils.addComponent(contentPane, gbc, "Resampling Method:", resamplingMethod);
        //gbc.gridy++;
        //DialogUtils.addComponent(contentPane, gbc, "Map Projection:", projectionName);
        gbc.gridy++;
        DialogUtils.addComponent(contentPane, gbc, "Pixel Size (m):", pixelSize);
        gbc.gridy++;
        DialogUtils.addComponent(contentPane, gbc, "Scene Width (pixels)", sceneWidth);
        gbc.gridy++;
        DialogUtils.addComponent(contentPane, gbc, "Scene Height (pixels)", sceneHeight);
        gbc.gridy++;
        DialogUtils.addComponent(contentPane, gbc, "Feather (pixels)", feather);
        
        gbc.gridy++;
        contentPane.add(averageCheckBox, gbc);
        gbc.gridy++;
        contentPane.add(normalizeByMeanCheckBox, gbc);

        DialogUtils.fillPanel(contentPane, gbc);

        return contentPane;
    }

    private static String[] getProjectionsValueSet() {
        final MapProjection[] projections = MapProjectionRegistry.getProjections();
        final String[] projectionsValueSet = new String[projections.length];
        for (int i = 0; i < projectionsValueSet.length; i++) {
            projectionsValueSet[i] = projections[i].getName();
        }
        return projectionsValueSet;
    }

    private class TextAreaKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
        }
        public void keyReleased(KeyEvent e) {
            try {
                changedByUser = true;
                if(e.getComponent() == pixelSize) {
                    final double pixSize = Double.parseDouble(pixelSize.getText());
                    OperatorUtils.getSceneDimensions(pixSize, scnProp);

                    sceneWidth.setText(String.valueOf(scnProp.sceneWidth));
                    sceneHeight.setText(String.valueOf(scnProp.sceneHeight));
                } else if(e.getComponent() == sceneWidth) {
                    final int height = (int)(Integer.parseInt(sceneWidth.getText()) / widthHeightRatio);
                    sceneHeight.setText(String.valueOf(height));
                    pixelSize.setText(String.valueOf(height * pixelSizeHeightRatio));
                } else if(e.getComponent() == sceneHeight) {
                    final int width = (int)(Integer.parseInt(sceneHeight.getText()) / widthHeightRatio);
                    sceneWidth.setText(String.valueOf(width));
                    pixelSize.setText(String.valueOf(width * pixelSizeHeightRatio));
                }
            } catch(Exception ex) {
                //
            }
        }
        public void keyTyped(KeyEvent e) {
        }
    }
}