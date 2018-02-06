package com.zxj.common.beantrace.internal;

import com.zxj.common.beantrace.model.Attribute;
import com.zxj.common.beantrace.model.Vertex;

/**
 * This decorator implements the stop-on-depth logic.
 *
 * @see com.zxj.common.beantrace.TraceConfiguration#maxDepth
 */
public class MaxDepthVertexFactoryDecorator implements VertexFactory {

    private final Integer maxDepth;
    private final VertexFactory delegate;

    private Integer depth = 1;

    public MaxDepthVertexFactoryDecorator(Integer maxDepth, VertexFactory delegate) {
        this.maxDepth = maxDepth;
        this.delegate = delegate;
    }

    @Override
    public Vertex create(Object subject) {
        if (depth >= maxDepth) {
            return makeProxy(subject);
        }
        try {
            depth++;
            return delegate.create(subject);
        } finally {
            depth--;
        }
    }

    private Vertex makeProxy(Object subject) {
        final Vertex ret = new Vertex(
                subject.getClass(),
                System.identityHashCode(subject) + ""
        );

        ret.getAttributes().add(new Attribute<>("...", "..."));
        return ret;
    }
}
