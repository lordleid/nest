package org.esa.beam.gpf.operators.standard;

import com.bc.ceres.core.ProgressMonitor;
import org.esa.beam.framework.datamodel.Band;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.gpf.Operator;
import org.esa.beam.framework.gpf.OperatorException;
import org.esa.beam.framework.gpf.Tile;
import org.esa.beam.framework.gpf.annotations.OperatorMetadata;
import org.esa.beam.framework.gpf.annotations.Parameter;
import org.esa.beam.framework.gpf.annotations.SourceProduct;
import org.esa.beam.framework.gpf.internal.JaiHelper;

import java.awt.RenderingHints;
import java.text.MessageFormat;
import java.util.HashMap;


@OperatorMetadata(alias = "JAI",
                  description = "Performs a JAI operation on bands of product.",
                  internal = true)
public class JaiOp extends Operator {

    @SourceProduct
    private Product sourceProduct;

    @Parameter
    private String[] bandNames;

    @Parameter
    private String operationName;

    //@Parameter
    private HashMap<String, Object> operationParameters;

    //@Parameter
    private RenderingHints renderingHints;


    public JaiOp() {
    }

    public JaiOp(Product sourceProduct,
                 String operationName,
                 HashMap<String, Object> operationParameters,
                 RenderingHints renderingHints) {
        this.sourceProduct = sourceProduct;
        this.operationName = operationName;
        this.operationParameters = operationParameters;
        this.renderingHints = renderingHints;
    }

    @Override
    public void initialize() throws OperatorException {
        Product targetProduct = JaiHelper.createTargetProduct(sourceProduct,
                                                              bandNames,
                                                              operationName,
                                                              operationParameters,
                                                              renderingHints);
        setTargetProduct(targetProduct);
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public HashMap<String, Object> getOperationParameters() {
        return operationParameters;
    }

    public void setOperationParameters(HashMap<String, Object> operationParameters) {
        this.operationParameters = operationParameters;
    }

    @Override
    public void computeTile(Band targetBand, Tile targetTile, ProgressMonitor pm) throws OperatorException {
        throw new IllegalStateException(MessageFormat.format("Operator ''{0}'' cannot compute tiles on its own.", getClass().getName()));
    }
}