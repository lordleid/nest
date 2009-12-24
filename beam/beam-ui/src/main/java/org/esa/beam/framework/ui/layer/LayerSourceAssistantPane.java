package org.esa.beam.framework.ui.layer;

import com.bc.ceres.glayer.LayerContext;
import org.esa.beam.framework.ui.AppContext;
import org.esa.beam.framework.ui.layer.LayerSource;
import org.esa.beam.framework.ui.assistant.AssistantPane;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;

/**
 * <i>Note: This API is not public yet and may significantly change in the future. Use it at your own risk.</i>
 */
public class LayerSourceAssistantPane extends AssistantPane implements LayerSourcePageContext {

    private final AppContext appContext;
    private final Map<String, Object> properties;
    private LayerSource layerSource;

    public LayerSourceAssistantPane(Window parent, String title, AppContext appContext) {
        super(parent, title);
        this.appContext = appContext;
        properties = new HashMap<String, Object>();
    }


    ///// Implementation of LayerSourcePageContext

    @Override
    public AppContext getAppContext() {
        return appContext;
    }

    @Override
    public LayerContext getLayerContext() {
        return appContext.getSelectedProductSceneView().getLayerContext();
    }

    @Override
    public void setLayerSource(LayerSource layerSource) {
        this.layerSource = layerSource;
    }

    @Override
    public LayerSource getLayerSource() {
        return layerSource;
    }

    @Override
    public Object getPropertyValue(String key) {
        return properties.get(key);
    }

    @Override
    public void setPropertyValue(String key, Object value) {
        properties.put(key, value);
    }
}