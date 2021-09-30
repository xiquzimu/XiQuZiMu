package me.xlgp.xiquzimu.ui.home;

public class Result<T> {

    private final T data;

    public Result(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    static class Succes<T> extends Result<T> {
        public Succes(T data) {
            super(data);
        }
    }

    static class Error<T> extends Result<T> {

        public Error(T data) {
            super(data);
        }
    }
}
