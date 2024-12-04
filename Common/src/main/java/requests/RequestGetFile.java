package requests;

import command_models.FileWrapper;
import tools.Separator;


import java.util.ArrayList;
import java.util.Objects;

public class RequestGetFile extends Request {
    private ArrayList<FileWrapper> files;

    public RequestGetFile(ArrayList<FileWrapper> files) {
        super(RequestType.GET_FILE);
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestGetFile req = (RequestGetFile) o;

        return Objects.equals(this.files, req.files)
                && Objects.equals(this.getType(), req.getType());
    }

    @Override
    public String serializeToStr() {
        String str = this.getType().toString();
        for (int i = 0; i < files.size(); i++) {
            str += Separator.SEPARATOR + files.get(i).serializeToStr();
        }
        return str;
    }

    public ArrayList<FileWrapper> getFiles() {
        return files;
    }
}
