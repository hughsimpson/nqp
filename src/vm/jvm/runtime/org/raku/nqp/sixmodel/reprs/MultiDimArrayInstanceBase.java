package org.raku.nqp.sixmodel.reprs;

import java.lang.System;

import org.raku.nqp.runtime.ExceptionHandling;
import org.raku.nqp.runtime.ThreadContext;
import org.raku.nqp.sixmodel.SerializationReader;
import org.raku.nqp.sixmodel.SerializationWriter;
import org.raku.nqp.sixmodel.SixModelObject;

public abstract class MultiDimArrayInstanceBase extends SixModelObject {
    public long[] dimensions;

    @Override
    public long[] dimensions(ThreadContext tc) {
        return dimensions;
    }

    @Override
    public void set_dimensions(ThreadContext tc, long[] dims) {
        MultiDimArrayREPRData rd = (MultiDimArrayREPRData)this.st.REPRData;
        if (rd.numDimensions == dims.length) {
            System.arraycopy(dims, 0, this.dimensions, 0, dims.length);
        }
        else {
            throw ExceptionHandling.dieInternal(tc, String.format(
                "Array type of %d dimensions cannot be initialized with %d dimensions",
                rd.numDimensions, dims.length));
        }
    }

    protected int numSlots() {
        long result = dimensions[0];
        for (int i = 1; i < dimensions.length; i++)
            result *= dimensions[i];
        return (int)result;
    }

    protected void duplicateSetDimensions(ThreadContext tc) {
        throw ExceptionHandling.dieInternal(tc,
            "MultiDimArray: can only set dimensions once");
    }

    protected int indicesToFlatIndex(ThreadContext tc, long[] indices) {
        if (indices.length == dimensions.length) {
            long multiplier = 1;
            long result     = 0;
            for (int i = dimensions.length - 1; i >= 0; i--) {
                long dim_size = dimensions[i];
                long index    = indices[i];
                if (index >= 0 && index < dim_size) {
                    result += index * multiplier;
                    multiplier *= dim_size;
                }
                else {
                    throw ExceptionHandling.dieInternal(tc, String.format(
                        "Index %d for dimension %d out of range (must be 0..%d)",
                        index, i + 1, dim_size - 1));
                }
            }
            return (int)result;
        }
        else {
            throw ExceptionHandling.dieInternal(tc, String.format(
            "Cannot access %d dimension array with %d indices",
            dimensions.length, indices.length));
        }
    }

    @Override
    public void push_boxed(ThreadContext tc, SixModelObject value) {
        throw ExceptionHandling.dieInternal(tc, "Cannot push onto a fixed dimension array");
    }
    @Override
    public void push_native(ThreadContext tc) {
        throw ExceptionHandling.dieInternal(tc, "Cannot push onto a fixed dimension array");
    }
    @Override
    public SixModelObject pop_boxed(ThreadContext tc) {
        throw ExceptionHandling.dieInternal(tc, "Cannot pop a fixed dimension array");
    }
    @Override
    public void pop_native(ThreadContext tc) {
        throw ExceptionHandling.dieInternal(tc, "Cannot pop a fixed dimension array");
    }
    @Override
    public void unshift_boxed(ThreadContext tc, SixModelObject value) {
        throw ExceptionHandling.dieInternal(tc, "Cannot unshift onto a fixed dimension array");
    }
    @Override
    public void unshift_native(ThreadContext tc) {
        throw ExceptionHandling.dieInternal(tc, "Cannot unshift onto a fixed dimension array");
    }
    @Override
    public SixModelObject shift_boxed(ThreadContext tc) {
        throw ExceptionHandling.dieInternal(tc, "Cannot shift a fixed dimension array");
    }
    @Override
    public void shift_native(ThreadContext tc) {
        throw ExceptionHandling.dieInternal(tc, "Cannot shift a fixed dimension array");
    }
    @Override
    public SixModelObject slice(ThreadContext tc, SixModelObject dest, long beginning, long end) {
        throw ExceptionHandling.dieInternal(tc, "Cannot slice a multidim array");
    }
    @Override
    public void splice(ThreadContext tc, SixModelObject from, long offset, long count) {
        throw ExceptionHandling.dieInternal(tc, "Cannot splice a fixed dimension array");
    }

    @Override
    public SixModelObject at_pos_boxed(ThreadContext tc, long index) {
        return this.at_pos_multidim_boxed(tc, new long[] { index });
    }
    @Override
    public void at_pos_native(ThreadContext tc, long index) {
        this.at_pos_multidim_native(tc, new long[] { index });
    }
    @Override
    public void bind_pos_boxed(ThreadContext tc, long index, SixModelObject value) {
        this.bind_pos_multidim_boxed(tc, new long[] { index }, value);
    }
    @Override
    public void bind_pos_native(ThreadContext tc, long index) {
        this.bind_pos_multidim_native(tc, new long[] { index });
    }
    @Override
    public void set_elems(ThreadContext tc, long count) {
        this.set_dimensions(tc, new long[] { count });
    }
    @Override
    public long elems(ThreadContext tc) {
        return this.dimensions(tc)[0];
    }

    @Override
    public abstract SixModelObject clone(ThreadContext tc);
    public abstract void serializeValues(ThreadContext tc, SerializationWriter writer);
    public abstract void deserializeValues(ThreadContext tc, SerializationReader reader);
}
