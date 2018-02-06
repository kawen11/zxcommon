package com.zxj.common.beantrace.handlers;

import com.zxj.common.beantrace.VertexFieldAdder;
import com.zxj.common.beantrace.model.Vertex;

/**
 * This class stops the propagation of the scanning  on the given object type.
 */
class TypedStopVertexHandler<T> extends TypeBasedHandler<T> {

    TypedStopVertexHandler(Class<?> handledType) {
        super(handledType);
    }

    @Override
    protected void typedHandle(Vertex vertex, T subject, VertexFieldAdder vertexFieldAdder) {
        // Intentionally left blank
    }
}
