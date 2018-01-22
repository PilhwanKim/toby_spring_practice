package springbook.learningtest.template;

import java.io.IOException;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value) throws IOException;
}
