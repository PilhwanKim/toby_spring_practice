package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public int calcSum(String filepath) throws IOException {
        LineCallback sumCallback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) throws IOException {
                return Integer.valueOf(line) + value;
            }
        };
        return lineReadTemplate(filepath, sumCallback, 0);
    }

    public int calcMultiply(String filepath) throws IOException {
        LineCallback multiplyCallback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) throws IOException {
                return Integer.valueOf(line) * value;
            }
        };
        return lineReadTemplate(filepath, multiplyCallback, 1);
    }

    public Integer lineReadTemplate(String filepath, LineCallback callback, int initVal) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            String line = null;
            Integer res = initVal;
            while((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if(br != null) {
                try { br.close(); }
                catch (IOException e) { System.out.println(e.getMessage()); }
            }
        }

    }

}
