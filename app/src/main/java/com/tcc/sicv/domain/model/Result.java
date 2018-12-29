package com.tcc.sicv.domain.model;

import java.util.function.Supplier;

public abstract class Result<T> {

        abstract T getOrElse(Supplier<T> other);

        private Result() {
        }

        public final static class Success<T> extends Result<T> {
            private final T value;
            public Success(T value) {
                this.value = value;
            }
            @Override
            T getOrElse(Supplier<T> other) {
                return value;
            }
        }
        public final static class Error<T> extends Result<T> {
            Throwable throwable;

            public Error(Throwable t){
                throwable = t;
            }

            @Override
            T getOrElse(Supplier<T> other) {
                return other.get();
            }
        }

}
