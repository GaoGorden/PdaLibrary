package p.gorden.pdalibrary.unity;

/**
 * Created by Administrator on 2017-07-10.
 */

public class Fun {
    //功能ID
    private int FunId;
    //功能图片
    private int ImageId;
    //功能名称
    private String FunName;

    public Fun(int funId, int imageId, String funName) {
        FunId = funId;
        ImageId = imageId;
        FunName = funName;
    }

    public int GetFunId() {
        return FunId;
    }

    public int GetImagetId() {
        return ImageId;
    }

    public String GetFunName() {
        return FunName;
    }

    public void SetFunId(int a) {
        FunId = a;
    }

    public void SetImageId(int a) {
        ImageId = a;
    }

    public void SetFunName(String a) {
        FunName = a;
    }
}
