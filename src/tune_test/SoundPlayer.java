package tune_test;

import javax.sound.sampled.*;

public class SoundPlayer {
    public static void playSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResourceAsStream(filePath));
            
            AudioFormat sourceFormat = audioInputStream.getFormat();
            AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sourceFormat.getSampleRate(),
                16, // 변경할 비트 수
                sourceFormat.getChannels(),
                sourceFormat.getChannels() * 2,
                sourceFormat.getSampleRate(),
                false
            );

            AudioInputStream convertedInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
            
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, convertedInputStream.getFormat());
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            
            line.open(convertedInputStream.getFormat());
            line.start();

            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesRead = -1;

            while ((bytesRead = convertedInputStream.read(buffer, 0, bufferSize)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

            line.drain();
            line.close();
            convertedInputStream.close();
            audioInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
