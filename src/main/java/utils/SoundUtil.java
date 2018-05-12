package utils;

import model.WavFile;

import java.io.File;
import java.util.List;

public class SoundUtil {
    private SoundUtil() {

    }

    public static WavFile generateSound(int chunkSize, int numberOfFrames, int sampleRate, String name, List<Integer> frequencies) {
        try {
            WavFile generatedFile = saveSound(frequencies, name, numberOfFrames, sampleRate, chunkSize);
            System.out.println("Generated sound was successfully saved");
            return generatedFile;
        } catch (Exception e) {
            System.out.println("===ERROR===");
            System.out.println("Could not save sound");
            e.printStackTrace();
            System.out.println("===ERROR===");
        }
        return null;
    }

    public static int[][] chunkArrayInt(int[] array, int chunkSize) {
        int numOfChunks = (int) Math.ceil(array.length / chunkSize);
        int[][] output = new int[numOfChunks][];

        for (int i = 0; i < numOfChunks; ++i) {
            int start = i * chunkSize;
            int length = Math.min(array.length - start, chunkSize);

            int[] temp = new int[length];
            System.arraycopy(array, start, temp, 0, length);
            output[i] = temp;
        }

        return output;
    }

    public static double[][] chunkArray(double[] array, int chunkSize) {
        int numOfChunks = (int) Math.ceil(array.length / chunkSize);
        double[][] output = new double[numOfChunks][];

        for (int i = 0; i < numOfChunks; ++i) {
            int start = i * chunkSize;
            int length = Math.min(array.length - start, chunkSize);

            double[] temp = new double[length];
            System.arraycopy(array, start, temp, 0, length);
            output[i] = temp;
        }

        return output;
    }

    private static WavFile saveSound(List<Integer> frequencies, String name, int totalFrames, int sampleRate, int chunkSize) throws Exception {
        WavFile wavFile = WavFile.newWavFile(new File("src/main/resources/results/transformed_" + name), 1, totalFrames, 8, sampleRate);

        int prevFreq = 0;
        int curSoundLength = 1;
        for (int i = 0; i < frequencies.size(); i++) {
            int frequency = frequencies.get(i);
            if (prevFreq == 0) {
                prevFreq = frequency;
            } else {
                if (frequency == 0 && i != frequencies.size() - 1) {
                    curSoundLength++;
                } else {
                    int bufSize = chunkSize * curSoundLength;
                    double[] buffer = new double[bufSize];
                    double L1 = (double) sampleRate / prevFreq;
                    for (int j = 0; j < bufSize; j++) {
                        buffer[j] = Math.sin(2 * Math.PI * j / L1);
                    }
                    wavFile.writeFrames(buffer, bufSize);

                    curSoundLength = 1;
                    prevFreq = frequency;
                }
            }
        }

        return wavFile;
    }
}