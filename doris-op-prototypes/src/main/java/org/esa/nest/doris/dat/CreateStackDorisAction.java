package org.esa.nest.doris.dat;

import org.esa.beam.framework.ui.command.CommandEvent;
import org.esa.beam.visat.actions.AbstractVisatAction;
import org.esa.nest.dat.DatContext;
import org.esa.nest.dat.plugins.graphbuilder.GraphBuilderDialog;

import java.io.File;

/**
 * Create Stack Doris Action.
 *
 */
public class CreateStackDorisAction extends AbstractVisatAction {

    @Override
    public void actionPerformed(CommandEvent event) {

        final GraphBuilderDialog dialog = new GraphBuilderDialog(new DatContext(""), "Create InSAR Stack", "CreateStackDorisOp", false);
        dialog.show();

        final File graphPath = GraphBuilderDialog.getInternalGraphFolder();
        final File graphFile =  new File(graphPath, "CreateStackDorisGraph.xml");

        dialog.LoadGraph(graphFile);
    }
}