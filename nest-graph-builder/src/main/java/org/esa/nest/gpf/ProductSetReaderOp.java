package org.esa.nest.gpf;

import org.esa.beam.framework.datamodel.*;
import org.esa.beam.framework.gpf.Operator;
import org.esa.beam.framework.gpf.OperatorException;
import org.esa.beam.framework.gpf.OperatorSpi;
import org.esa.nest.gpf.ProductSetReaderOpUI;
import org.esa.beam.framework.gpf.annotations.OperatorMetadata;
import org.esa.beam.framework.gpf.annotations.Parameter;
import org.esa.beam.framework.gpf.annotations.TargetProduct;
import org.esa.beam.framework.dataio.ProductIO;

import java.io.File;
import java.io.IOException;

/**
 * ProductSet Operator to be replaced by ReadOp
 */
@OperatorMetadata(alias="ProductSet-Reader")
public class ProductSetReaderOp extends Operator {

    @TargetProduct
    private Product targetProduct;
    @Parameter(description = "The file from which the data product is read.")
    private File defaultFile;
    @Parameter
    private String[] fileList;

    /**
     * Default constructor. The graph processing framework
     * requires that an operator has a default constructor.
     */
    public ProductSetReaderOp() {
    }

    /**
     * Initializes this operator and sets the one and only target product.
     * <p>The target product can be either defined by a field of type {@link org.esa.beam.framework.datamodel.Product} annotated with the
     * {@link org.esa.beam.framework.gpf.annotations.TargetProduct TargetProduct} annotation or
     * by calling {@link #setTargetProduct} method.</p>
     * <p>The framework calls this method after it has created this operator.
     * Any client code that must be performed before computation of tile data
     * should be placed here.</p>
     *
     * @throws org.esa.beam.framework.gpf.OperatorException
     *          If an error occurs during operator initialisation.
     * @see #getTargetProduct()
     */
    @Override
    public void initialize() throws OperatorException {
        try {
            targetProduct = ProductIO.readProduct(defaultFile, null);
        } catch(IOException e) {
            throw new OperatorException(e);
        }
    }

    /**
     * The SPI is used to register this operator in the graph processing framework
     * via the SPI configuration file
     * {@code META-INF/services/org.esa.beam.framework.gpf.OperatorSpi}.
     * This class may also serve as a factory for new operator instances.
     * @see org.esa.beam.framework.gpf.OperatorSpi#createOperator()
     * @see org.esa.beam.framework.gpf.OperatorSpi#createOperator(java.util.Map, java.util.Map)
     */
    public static class Spi extends OperatorSpi {
        public Spi() {
            super(ProductSetReaderOp.class);
            this.setOperatorUI(ProductSetReaderOpUI.class);
        }
    }
}