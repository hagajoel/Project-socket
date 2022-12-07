package server;

public class ReceiveFile {
    private int id;
    private byte[] data;
    private String extension;

    public ReceiveFile(int id, byte[] data, String extension) {
        setId(id);
        setData(data);
        setExtension(extension);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}
