package com.zxj.common.beantrace.handlers;

import com.zxj.common.beantrace.VertexFieldAdder;
import com.zxj.common.beantrace.model.Vertex;

import java.util.Collection;

class CollectionHandler extends TypeBasedHandler<Collection<Object>> {

    public CollectionHandler() {
        super(Collection.class);
    }

    @Override
    protected void typedHandle(Vertex vertex, Collection<Object> subject, VertexFieldAdder vertexFieldAdder) {
        int i = 0;
        for (Object item : subject) {
            vertexFieldAdder.addField(vertex, i + "", item);
            i++;
        }
    }
}
