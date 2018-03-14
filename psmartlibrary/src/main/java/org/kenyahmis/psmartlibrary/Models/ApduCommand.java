package org.kenyahmis.psmartlibrary.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Muhoro on 3/13/2018.
 */

public class ApduCommand {

    public byte Cla;
    public byte Ins;
    public byte P1;
    public byte P2;
    public byte P3;

    public byte[] data;
    private byte[] Sw;

    public void setCommand(byte Cla, byte Ins, byte P1, byte P2, byte P3){
        this.Cla = Cla;
        this.Ins = Ins;
        this.P1 = P1;
        this.P2 = P2;
        this.P3 = P3;
    }

    public void setData(byte[] data){
        this.data = data;
    }

    public byte[] createCommand()
    {
        byte[] bytes = new byte[]{Cla, Ins, P1, P2, P3};
        if(this.data != null && this.data.length > 0){
            byte[] combined = new byte[bytes.length + this.data.length];

            System.arraycopy(bytes,0,combined,0,bytes.length);
            System.arraycopy(this.data,0,combined,bytes.length,this.data.length);
            return combined;
        }
        return bytes;
    }

    public byte[] getSw() {return this.Sw;}
    public void setSw(byte[] sw) {this.Sw = sw;}
}
