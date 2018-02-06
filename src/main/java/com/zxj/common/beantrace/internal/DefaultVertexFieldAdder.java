package com.zxj.common.beantrace.internal;

import com.zxj.common.beantrace.model.Attribute;
import com.zxj.common.beantrace.model.Edge;
import com.zxj.common.beantrace.model.Vertex;


/**
 * Populates the references and the attributes of the vertex based on the
 * passed field of the object.
 */
public class DefaultVertexFieldAdder implements com.zxj.common.beantrace.VertexFieldAdder {

    private final VertexFactory vertexFactory;

    public DefaultVertexFieldAdder(VertexFactory vertexFactory) {
        this.vertexFactory = vertexFactory;
    }

    @Override
    public void addField(Vertex vertex, String fieldName, Object item) {
        if (item == null) {
            return;
        }

        if (ReflectUtils.isPrimitive(item.getClass())) {
            vertex.getAttributes().add(new Attribute<>(fieldName, item));
            return;
        }

        vertex.getReferences().add(new Edge(
                fieldName, vertexFactory.create(item)
        ));
    }
}
