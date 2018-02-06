package com.zxj.common.beantrace.handlers;

import com.zxj.common.beantrace.VertexFieldAdder;
import com.zxj.common.beantrace.model.Attribute;
import com.zxj.common.beantrace.model.Vertex;

import java.net.URL;

class URLTypeHandler extends TypeBasedHandler<URL> {

    URLTypeHandler() {
        super(URL.class);
    }

    @Override
    protected void typedHandle(Vertex vertex, URL subject, VertexFieldAdder vertexFieldAdder) {
        vertex.getAttributes().add(new Attribute<>("url", subject.toString()));
    }
}
