package com.zxj.common.beantrace.internal;

import com.zxj.common.beantrace.model.Vertex;

public interface VertexFactory {

    Vertex create(Object subject);

}
