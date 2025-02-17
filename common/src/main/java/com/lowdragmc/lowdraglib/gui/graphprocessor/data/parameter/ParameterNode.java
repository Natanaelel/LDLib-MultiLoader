package com.lowdragmc.lowdraglib.gui.graphprocessor.data.parameter;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.graphprocessor.annotation.*;
import com.lowdragmc.lowdraglib.gui.graphprocessor.data.BaseNode;
import com.lowdragmc.lowdraglib.gui.graphprocessor.data.PortData;
import com.lowdragmc.lowdraglib.gui.graphprocessor.data.PortEdge;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@LDLRegister(name = "parameter", group = "graph_processor.node.parameter")
public class ParameterNode extends BaseNode {

    @InputPort
    public Object input;
    @OutputPort
    public Object output;
    /**
     * We serialize the GUID of the exposed parameter in the graph so we can retrieve the true ExposedParameter from the graph
     */
    @Persisted
    public String parameterIdentifier;
    @Nullable
    public ExposedParameter<?> parameter;

    public ParameterNode() {
        expanded = false;
    }

    @Override
    public void setExpanded(boolean expanded) {
    }

    @Override
    public String getDisplayName() {
        return parameter == null ? super.getDisplayName() : parameter.getDisplayName();
    }

    @Override
    public void setDisplayName(String displayName) {
        if (parameter != null) parameter.setDisplayName(displayName);
        else super.setDisplayName(displayName);
    }

    @Override
    protected void enable() {
        parameter = graph.getExposedParameterFromIdentifier(parameterIdentifier);
        if (parameter == null) {
            LDLib.LOGGER.error("Property {} Can't be found !", parameterIdentifier);
            graph.removeNode(this);
            return;
        }
        output = parameter.getValue();
    }

    @CustomPortBehavior(field = "output")
    public List<PortData> getOutputPort(List<PortEdge> edges) {
        if (parameter != null && parameter.getAccessor() == ExposedParameter.ParameterAccessor.Get) {
            return List.of(new PortData()
                    .identifier("output")
                    .displayName("Value")
                    .displayType(parameter.type)
                    .tooltip(parameter.getTips())
                    .acceptMultipleEdges(true));
        }
        return Collections.emptyList();
    }

    @CustomPortBehavior(field = "input")
    public List<PortData> getInputPort(List<PortEdge> edges) {
        if (parameter != null && parameter.getAccessor() == ExposedParameter.ParameterAccessor.Set) {
            return List.of(new PortData()
                    .identifier("input")
                    .displayName("Value")
                    .displayType(parameter.type)
                    .tooltip(parameter.getTips()));
        }
        return Collections.emptyList();
    }

    @Override
    protected void process() {
        if (parameter == null) return;
        if (parameter.getAccessor() == ExposedParameter.ParameterAccessor.Get)
            output = parameter.getValue();
        else
            graph.updateExposedParameter(parameter.identifier, input);
    }

}


