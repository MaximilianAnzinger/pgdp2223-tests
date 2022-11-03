package pgdp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LineInputStream extends InputStream {
    private final String[] lines;
    private int currentLine = 0;
    private ByteArrayInputStream byteStream;

    public LineInputStream(String[] lines) {
        this.lines = lines;
        this.nextByteStream();
    }

    private void nextByteStream() {
        if(currentLine < this.lines.length) {
            String line = this.lines[currentLine] + "\n";
            this.byteStream = new ByteArrayInputStream(line.getBytes());
            currentLine++;
        }
    }

    @Override
    public int read() throws IOException {
        if(this.byteStream == null) {
            return -1;
        }

        if(this.byteStream.available() == 0) {
            this.nextByteStream();
            return -1;
        }
        return this.byteStream.read();
    }
}


