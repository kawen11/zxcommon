package com.zxj.common.beantrace.internal;

import java.util.Collection;
import java.util.LinkedList;

import com.zxj.common.beantrace.model.Vertex;

public class CollectorVisitor implements VertexVisitor {

    private final Collection<Vertex> result = new LinkedList<>();

    @Override
    public void visit(Vertex vertex) {
        result.add(vertex);
    }

    public Collection<Vertex> getResult() {
        return result;
    }
}
