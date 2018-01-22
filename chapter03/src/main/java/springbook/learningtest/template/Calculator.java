package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public int calcSum(String filepath) throws IOException {
        BufferedReaderCallback sumCallback = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer sum  = 0;
                String line = null;
                while((line = br.readLine()) != null) {
                    sum += Integer.valueOf(line);
                }
                return sum;
            }
        };
        return fileReadTempate(filepath, sumCallback);
    }

    public int calcMultiply(String filepath) throws IOException {
        BufferedReaderCallback multiflyCallback = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer multiply  = 1;
                String line = null;
                while((line = br.readLine()) != null) {
                    multiply *= Integer.valueOf(line);
                }
                return multiply;
            }
        };
        return fileReadTempate(filepath, multiflyCallback);
    }

    public int fileReadTempate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            Integer ret = callback.doSomethingWithReader(br);
            return ret;
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
