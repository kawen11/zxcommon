package com.zxj.common.beantrace;

import com.zxj.common.beantrace.model.Vertex;

public interface VertexFieldAdder {
    void addField(Vertex vertex, String fieldName, Object item);
}
