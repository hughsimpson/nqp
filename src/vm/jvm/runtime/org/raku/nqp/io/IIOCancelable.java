package org.raku.nqp.io;

import org.raku.nqp.runtime.ThreadContext;

public interface IIOCancelable {

    void cancel(ThreadContext tc);
}
